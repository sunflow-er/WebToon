package com.masonk.apps

import android.graphics.Bitmap
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.core.view.isVisible

class WebtoonClient(
    private val progressCircular: ProgressBar, // 프로그래스바 객체
    private val saveData : (String) -> Unit, // 데이터 저장 함수 객체
) : WebViewClient() {

    // URL 로딩을 가로채 이를 제어하거나 다른 동작을 수행
    override fun shouldOverrideUrlLoading(
        view: WebView?, // 현재 URL를 로드하려는 WebView 객체
        request: WebResourceRequest? // 로드하려는 URL에 대한 요청 정보를 담고 있는 WebResourceRequest 객체
    ): Boolean {

        // 로드하는 URL이 웹툰 상세페이지일 경우, URL데이터를 SharedPreference에 저장
        if (request != null && request.url.toString().contains("comic.naver.com/webtoon/detail")) {
            // 데이터 저장 함수
            saveData(request.url.toString())
        }

        return super.shouldOverrideUrlLoading(view, request)
    }

    // 웹 페이지 로딩이 시작될 때 호출
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)

        // 프로그래스바 보이도록
        progressCircular.isVisible = true
    }

    // 웹 페이지 로딩이 완료될 때 호출
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)

        // 프로그래스바 안보이도록
        progressCircular.isVisible = false
    }

}