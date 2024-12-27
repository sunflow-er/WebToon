package com.masonk.apps

import android.content.Context
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.masonk.apps.WebViewFragment.Companion.SHARED_PREFERENCE
import com.masonk.apps.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnTabNameChanged {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // SHARED_PREFERENCE(WEB_HISTORY) 파일에 저장된 탭 이름 정보 가져오기
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)
        val tab0_name = sharedPreferences.getString("tab0_name", "fri")
        val tab1_name = sharedPreferences.getString("tab1_name", "sat")
        val tab2_name = sharedPreferences.getString("tab2_name", "sun")

        // ViewPager2 Adpater 설정
        binding.viewPager2.adapter = ViewPagerAdapter(this)

        // TabLayout - ViewPager2 연결
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position -> // tab : TabLayout.Tab 객체, position : 현재 탭의 위치
            // 각 탭의 텍스트 설정
            tab.text = when (position) {
                0 -> tab0_name
                1 -> tab1_name
                else -> tab2_name
            }
        }.attach()
    }

    // 액티비티에서 뒤로가기 감지 시 실행
    override fun onBackPressed() {
        // 현재 프래그먼트 정보 받기
        val currentFragment = supportFragmentManager.fragments[binding.viewPager2.currentItem]
        
        if (currentFragment is WebViewFragment) { // 현재 프래그먼트가 WebViewFragment이면
            if (currentFragment.canGoBack()) { // 웹뷰 상에서 뒤로갈 수 있으면
                currentFragment.goBack() // 뒤로가기(웹뷰)
            } else { // 웹뷰 상에서 뒤로갈 수 없으면
                super.onBackPressed() // 뒤로가기(액티비티)
            }
        } else { // WebViewFragment가 아니면
            super.onBackPressed() // 뒤로가기(액티비티)
        }
    }
    
    // OnNameChanged 인터페이스 함수 구현
    // 탭 이름 변경 반영
    override fun nameChanged(position: Int, name: String) {
        val tab = binding.tabLayout.getTabAt(position)
        tab?.text = name
    }
}