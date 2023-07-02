package com.example.mobileapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var tempFragment: TempFragment
    lateinit var dangerFragment: DangerFragment
    lateinit var mapFragment: com.google.android.gms.maps.SupportMapFragment

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TempFragment 초기화
        tempFragment = TempFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_con, tempFragment)
            .commit()
        dangerFragment = DangerFragment()
        mapFragment = com.google.android.gms.maps.SupportMapFragment()

        var bnv_main = findViewById(R.id.bnv_main) as BottomNavigationView

        bnv_main.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.item_temp -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fl_con, tempFragment).commit()
                    }
                    R.id.item_dnager -> {
                        // 다른 프래그먼트 화면으로 이동하는 기능
                        val BlankFragment1 = DangerFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fl_con, BlankFragment1).commit()
                    }
                    R.id.item_map -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fl_con, mapFragment).commit()
                    }
                    R.id.item_user -> {
                        val BlankFragment3 = SettingsFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fl_con, BlankFragment3).commit()
                    }
                }
                true
            }
            selectedItemId = R.id.item_temp
        }
    }
}
