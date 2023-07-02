package com.example.mobileapplication

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment


class SettingsFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var spinner: Spinner
    private lateinit var colorSpinner: Spinner
    private lateinit var addLocalEditText: EditText
    private lateinit var addLocalButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // SharedPreferences 초기화
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)

        // Spinner 초기화
        spinner = view.findViewById(R.id.local_spinner) ?: return
        colorSpinner = view.findViewById(R.id.color_spinner)
        val colorArray = arrayOf("Red", "Blue", "Green")

        // EditText 및 Button 초기화
        addLocalEditText = view.findViewById(R.id.add_local)
        addLocalButton = view.findViewById(R.id.add_local_button)

        // areaName의 초깃값 설정
        (requireActivity() as MainActivity).tempFragment.areaName = "여의도"

        // Spinner에 표시될 지역 목록 생성
        val locations = mutableListOf<String>()
        locations.add("여의도")
        locations.add("서울역")
        locations.add("강남역")
        locations.add("북서울꿈의숲")
        // 추가적인 지역 목록을 여기에 추가할 수 있습니다.

        // 저장된 지역 목록 가져오기
        val savedLocations = sharedPreferences.getStringSet("locations", mutableSetOf())?.toMutableSet()
        locations.addAll(savedLocations ?: mutableSetOf())

        // ArrayAdapter를 사용하여 Spinner와 데이터 연결
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, locations)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // 저장된 지역 값을 가져와서 Spinner에 설정
        val savedLocation = sharedPreferences.getString("location", "")
        if (!savedLocation.isNullOrEmpty()) {
            val index = locations.indexOf(savedLocation)
            spinner.setSelection(index)
        }
// 지역 선택 시 SharedPreferences에 저장
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLocation = parent?.getItemAtPosition(position).toString()

                // 선택된 지역 값을 SharedPreferences에 저장
                val editor = sharedPreferences.edit()
                editor.putString("location", selectedLocation)
                editor.apply()

                // MainActivity의 tempFragment 인스턴스에 접근하여 areaName 설정
                (requireActivity() as MainActivity).tempFragment.areaName = selectedLocation
                (requireActivity() as MainActivity).dangerFragment.areaName = selectedLocation
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무것도 선택되지 않았을 때의 동작
            }
        }

        // 지역 추가 버튼 클릭 이벤트 처리
        addLocalButton.setOnClickListener {
            val newLocation = addLocalEditText.text.toString().trim()

            // 새로운 지역 추가
            if (newLocation.isNotEmpty()) {
                locations.add(newLocation)

                // 저장된 지역 목록 업데이트
                val editor = sharedPreferences.edit()
                editor.putStringSet("locations", locations.toSet())
                editor.apply()

                // Spinner 업데이트
                adapter.notifyDataSetChanged()
                spinner.setSelection(locations.indexOf(newLocation))

                // EditText 초기화
                addLocalEditText.text.clear()
            }
        }

        colorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedColor = parent.getItemAtPosition(position).toString()
                val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("selectedColor", selectedColor)
                editor.apply()

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case when nothing is selected
            }
        }
    }

}