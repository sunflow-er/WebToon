package com.masonk.apps

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.masonk.apps.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.webView.apply {
            // 기본 브라우저가 아닌 WebView 내에서 열리도록 함
            webViewClient = WebViewClient()
            
            // 웹 페이지에서 JavaScript를 사용할 수 있도록 함 ->  사용자 상호작용 처리
            settings.javaScriptEnabled = true

            // WebView에 로드할 URL 설정
            loadUrl("https://google.com")
        }

    }
}