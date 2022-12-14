package com.iceka.whatsappclone.weather

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.iceka.whatsappclone.R

class WeatherWebview : AppCompatActivity()

{

    private lateinit var webView: WebView
    private lateinit var lottieJson: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_webview)
        lottieJson = findViewById(R.id.lottie)
        webView = findViewById(R.id.webview)
        webView.settings.javaScriptEnabled = true
        loadWebView("https://weatherwalay.com/tvas")

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebView(url: String) {
        webView.loadUrl(url)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.databaseEnabled = true
        webView.settings.setSupportMultipleWindows(true)
        webView.settings.loadsImagesAutomatically = true;
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                //dialog?.dismiss()
                webView.visibility = View.VISIBLE
                lottieJson.visibility = View.GONE

            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
               // dialog?.show()
                webView.visibility = View.GONE
                lottieJson.visibility = View.VISIBLE
            }
        }
    }
 }
