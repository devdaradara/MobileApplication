package com.example.mobileapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.StringReader
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

class TempFragment : Fragment() {

    private val apiUrl = "http://openapi.seoul.go.kr:8088/5166474a646a697939387a72714e67/xml/citydata/1/5/"
    public var areaName: String = "여의도"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment의 레이아웃 초기화 등 필요한 작업 수행
        return inflater.inflate(R.layout.fragment_temp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 데이터 요청
        fetchData()
    }

    private fun fetchData() {
        GlobalScope.launch(Dispatchers.Main) {
            val url = URL("$apiUrl$areaName")
            val xmlData = withContext(Dispatchers.IO) { url.readText() }
            val seoulData = parseXmlData(xmlData)

            // 데이터 처리 및 화면 업데이트
            updateUI(seoulData)
        }
    }

    private fun parseXmlData(xmlData: String): SeoulData {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val inputSource = InputSource(StringReader(xmlData))
        val document = builder.parse(inputSource)

        val rootElement = document.documentElement
        val listTotalCount = rootElement.getElementsByTagName("list_total_count").item(0).textContent.toInt()
        val areaName = rootElement.getElementsByTagName("AREA_NM").item(0).textContent

        val weatherSttsElement = rootElement.getElementsByTagName("WEATHER_STTS").item(0) as Element
        val weatherStts = parseWeatherStts(weatherSttsElement)

        return SeoulData(listTotalCount, areaName, weatherStts)
    }

    private fun parseWeatherStts(weatherSttsElement: Element): WeatherStts {
        val weatherSttsNode = weatherSttsElement.getElementsByTagName("WEATHER_STTS").item(0) as Element
        val temp = weatherSttsNode.getElementsByTagName("TEMP").item(0).textContent.toDouble()
        val precipitation = weatherSttsNode.getElementsByTagName("PRECIPITATION").item(0).textContent
        val precptType = weatherSttsNode.getElementsByTagName("PRECPT_TYPE").item(0).textContent
        val pcpMsg = weatherSttsNode.getElementsByTagName("PCP_MSG").item(0).textContent
//        val uvIndexLvl = weatherSttsNode.getElementsByTagName("UV_INDEX_LVL").item(0).textContent.toDouble().toInt()
        val uvIndex = weatherSttsNode.getElementsByTagName("UV_INDEX").item(0).textContent
        val uvMsg = weatherSttsNode.getElementsByTagName("UV_MSG").item(0).textContent
        val pm25Index = weatherSttsNode.getElementsByTagName("PM25_INDEX").item(0).textContent
        val pm25 = weatherSttsNode.getElementsByTagName("PM25").item(0).textContent.toInt()
        val pm10Index = weatherSttsNode.getElementsByTagName("PM10_INDEX").item(0).textContent
        val pm10 = weatherSttsNode.getElementsByTagName("PM10").item(0).textContent.toInt()
        val airIdx = weatherSttsNode.getElementsByTagName("AIR_IDX").item(0).textContent
//        val airIdxMvl = weatherSttsNode.getElementsByTagName("AIR_IDX_MVL").item(0).textContent.toInt()
        val airIdxMain = weatherSttsNode.getElementsByTagName("AIR_IDX_MAIN").item(0).textContent
        val airMsg = weatherSttsNode.getElementsByTagName("AIR_MSG").item(0).textContent

        return WeatherStts(
            temp, precipitation, precptType, pcpMsg, 0, uvIndex, uvMsg,
            pm25Index, pm25, pm10Index, pm10, airIdx, 0, airIdxMain, airMsg
        )
    }


    private fun updateUI(seoulData: SeoulData) {
        val localTextView = view?.findViewById<TextView>(R.id.local)
        val tempTextView = view?.findViewById<TextView>(R.id.temp)
        val uvTextView = view?.findViewById<TextView>(R.id.value_uv)
        val uvAlertTextView = view?.findViewById<TextView>(R.id.alert_uv)
        val dustTextView = view?.findViewById<TextView>(R.id.value_dust)
        val dustAlertTextView = view?.findViewById<TextView>(R.id.alert_dust)
        val rainTextView = view?.findViewById<TextView>(R.id.value_rain)
        val rainAlertTextView = view?.findViewById<TextView>(R.id.alert_rain)

        localTextView?.text = seoulData.areaName
        tempTextView?.text = seoulData.weatherStts.temp.toString()
        uvTextView?.text = seoulData.weatherStts.uvIndex
        uvAlertTextView?.text = seoulData.weatherStts.uvMsg
        dustTextView?.text = seoulData.weatherStts.pm25Index
        dustAlertTextView?.text = seoulData.weatherStts.airMsg
        rainTextView?.text = seoulData.weatherStts.precipitation
        rainAlertTextView?.text = seoulData.weatherStts.pcpMsg
    }

}