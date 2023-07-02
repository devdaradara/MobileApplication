package com.example.mobileapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.IOException
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.math.log

data class DangerItem(
    val warnVal: String,
    val warnStress: String,
    val announceTime: String,
    val command: String,
    val cancelYn: String,
    val warnMsg: String,
    val imageResId: Int // New property for the image resource ID
)

data class NewsItem(
    val warnVal: String,
    val warnStress: String,
    val announceTime: String,
    val command: String,
    val cancelYn: String,
    val warnMsg: String
)

class DangerFragment : Fragment() {

    private lateinit var dangerList: MutableList<DangerItem>
    private lateinit var alertList: MutableList<NewsItem>
    private lateinit var adapter: DangerAdapter

    private val apiUrl = "http://openapi.seoul.go.kr:8088/5166474a646a697939387a72714e67/xml/citydata/1/5/"
    public var areaName: String = "여의도"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_danger, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dangerList = mutableListOf()
        alertList = mutableListOf()
        adapter = DangerAdapter() // 어댑터 초기화
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView) // RecyclerView ID에 맞게 수정
        recyclerView.adapter = adapter // 어댑터 연결
        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // RecyclerView에 적절한 레이아웃 매니저 설정
        fetchData()
    }

    private fun fetchData() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL("$apiUrl$areaName")
                val connection = url.openConnection()
                connection.connectTimeout = 5000 // Adjust the timeout if needed
                val inputStream = connection.getInputStream()
                val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                val document = documentBuilder.parse(inputStream)
                parseXmlData(document)
                updateAletrUI()
                updateUI()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun parseXmlData(document: Document) {
        val areaNode = document.getElementsByTagName("AREA_NM").item(0)
        val areaName = areaNode.textContent

        val fcst24HoursList = document.getElementsByTagName("FCST24HOURS")
        for (i in 0 until fcst24HoursList.length) {
            val fcst24HoursNode = fcst24HoursList.item(i) as Element

            val fcstDtNode = fcst24HoursNode.getElementsByTagName("FCST_DT").item(0)
            val fcstDt = fcstDtNode.textContent

            val tempNode = fcst24HoursNode.getElementsByTagName("TEMP").item(0)
            val temp = tempNode.textContent

            val precipitationNode = fcst24HoursNode.getElementsByTagName("PRECIPITATION").item(0)
            val precipitation = precipitationNode.textContent

            val precptTypeNode = fcst24HoursNode.getElementsByTagName("PRECPT_TYPE").item(0)
            val precptType = precptTypeNode.textContent

            val rainChanceNode = fcst24HoursNode.getElementsByTagName("RAIN_CHANCE").item(0)
            val rainChance = rainChanceNode.textContent

            val skySttsNode = fcst24HoursNode.getElementsByTagName("SKY_STTS").item(0)
            val skyStts = skySttsNode.textContent

            val imageResId = when (skyStts) {
                "맑음" -> R.drawable.sunny
                "흐림" -> R.drawable.cloud
                "비" -> R.drawable.suncloud
                else -> R.drawable.sunny
            }

            val dangerItem = DangerItem(fcstDt, temp, precipitation, precptType, rainChance, skyStts, imageResId)
            dangerList.add(dangerItem)
        }

        val newsList = document.getElementsByTagName("NEWS_LIST")
        for (i in 0 until newsList.length) {
            val newsListNode = newsList.item(i) as Element

            val warnValNode = newsListNode.getElementsByTagName("WARN_VAL").item(0)
            val warnVal = warnValNode.textContent

            val warnStressNode = newsListNode.getElementsByTagName("WARN_STRESS").item(0)
            val warnStress = warnStressNode.textContent

            val announceTimeNode = newsListNode.getElementsByTagName("ANNOUNCE_TIME").item(0)
            val announceTime = announceTimeNode.textContent

            val commandNode = newsListNode.getElementsByTagName("COMMAND").item(0)
            val command = commandNode.textContent

            val cancelYnNode = newsListNode.getElementsByTagName("CANCEL_YN").item(0)
            val cancelYn = cancelYnNode.textContent

            val warnMsgNode = newsListNode.getElementsByTagName("WARN_MSG").item(0)
            val warnMsg = warnMsgNode.textContent

            val newsItem = NewsItem(warnVal, warnStress, announceTime, command, cancelYn, warnMsg)
            alertList.add(newsItem)
        }
    }
    private fun updateAletrUI() {
        val typeTextView = view?.findViewById<TextView>(R.id.danger_warn_type)
        val timeTextView = view?.findViewById<TextView>(R.id.danger_warn_time)
        val msgTextView = view?.findViewById<TextView>(R.id.danger_warn_msg)

        requireActivity().runOnUiThread {
            typeTextView?.text = alertList[0].warnVal + " " + alertList[0].warnStress
            timeTextView?.text = alertList[0].announceTime
            msgTextView?.text = alertList[0].warnMsg
        }
    }

    private fun updateUI() {
        val timeInfoList = dangerList.map { it.warnVal }
        val tempInfoList = dangerList.map { it.warnStress }
        val rainInfoList = dangerList.map { it.announceTime }
        val rainChanceInfoList = dangerList.map { it.command }
        val imageResIdList = dangerList.map { it.imageResId }

        requireActivity().runOnUiThread {
            adapter.setData(timeInfoList, tempInfoList, rainInfoList, rainChanceInfoList, imageResIdList)
        }
    }
}