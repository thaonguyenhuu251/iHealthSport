package com.htnguyen.ihealth.view.main

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.htnguyen.ihealth.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityWebViewBinding
    override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.webview.webViewClient = WebViewClient()
        binding.webview.loadUrl("https://www.facebook.com/PhanAnhHaUI")
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}