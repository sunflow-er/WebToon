package com.masonk.apps

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.masonk.apps.databinding.FragmentWebviewBinding

class WebViewFragment(
    private val position: Int, // ViewPager에서의 position
    private val webUrl: String // 로드할 URL 정보
) : Fragment() {
    private lateinit var binding: FragmentWebviewBinding

    // 탭 이름의 변경됨에 따라 이를 반영할 객체
    var listener: OnTabNameChanged? = null

    // SharedPreference 파일 이름 상수 (static-final)
    companion object {
        const val SHARED_PREFERENCE = "WEB_HISTORY"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWebviewBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // webView에 webUrl 띄우기
        binding.webView.apply {
            // 프로그래스바 바인딩 객체, 데이터 저장 함수 전달
            webViewClient = WebtoonClient(binding.progressCircular) { url: String ->

                // SHARED_PREFERENCE(WEB_HISTORY)라는 이름의 SharedPreference파일 열기
                activity?.getSharedPreferences(
                    SHARED_PREFERENCE, // SharedPreference 파일 이름
                    Context.MODE_PRIVATE // 현재 앱에서만 접근 가능
                )?.edit { // SharedPreference 편집
                    // 키-값 형태로 데이터 저장
                    putString(
                        "tab$position" // 키
                        , url // 값
                    )
                }
            }
            settings.javaScriptEnabled = true
            loadUrl(webUrl)
        }

        // 가장 최근의 웹툰 상세화면으로 이동
        binding.backToLastButton.setOnClickListener {
            // SHARED_PREFERENCE(WEB_HISTORY)라는 이름의 SharedPreference파일을 열기
            val sharedPreference =
                activity?.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)

            // tab$position 키에 해당하는 URL 값 얻기
            val url = sharedPreference?.getString("tab$position", "")

            if (url.isNullOrEmpty()) { // URL 값이 없을 경우
                // 토스트 메시지(에러) 띄우기
                Toast.makeText(
                    context,
                    getString(R.string.back_to_last_error_message),
                    Toast.LENGTH_SHORT
                ).show()
            } else { // URL 값이 있을 경우
                // 해당 URL 로드
                binding.webView.loadUrl(url)
            }
        }

        // Dialog를 통해 탭레이아웃의 탭 이름 변경
        binding.changeTabNameButton.setOnClickListener {
            // 탭 이름 변경을 위한 dialog 띄우기
            val dialog = AlertDialog.Builder(requireContext())
            val editText = EditText(requireContext())

            dialog.setView(editText)
            dialog.setPositiveButton(getString(R.string.save)) { _, _ ->
                activity?.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)?.edit {
                    // 새로운 탭 이름 정보를 Sharedpreference에 저장
                    putString("tab${position}_name", editText.text.toString())

                    // 새로운 탭 이름 반영
                    listener?.nameChanged(position, editText.text.toString())
                }
            }
            dialog.setNegativeButton(getString(R.string.cancel)) { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            dialog.show()
        }
    }

    // 웹뷰 상에서 뒤로가기 할 수 있는지 판단
    fun canGoBack(): Boolean {
        return binding.webView.canGoBack()
    }

    // 웹뷰 상에서 뒤로가기
    fun goBack() {
        binding.webView.goBack()
    }
}