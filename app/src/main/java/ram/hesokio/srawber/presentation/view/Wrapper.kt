package ram.hesokio.srawber.presentation.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import ram.hesokio.srawber.databinding.ActStartsBinding
import ram.hesokio.srawber.presentation.view_model.MainViewModel

class Wrapper : AppCompatActivity() {

    private val viewModel by viewModel<MainViewModel>()
    private lateinit var binding: ActStartsBinding

    private lateinit var webView: WebView
    private var messageAb: ValueCallback<Array<Uri?>>? = null
    private var callback: ValueCallback<Uri>? = null
    private val resultCode = 1

    private val imageTitle = "Image Chooser"
    private val image1 = "image/*"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActStartsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getFullLinkFromDataBase()
        viewModel.getFlagFromDataBase()

        initWebView()

        initFullLinkObserver()
    }

    private fun initFullLinkObserver() {
        viewModel.fullLink.observe(this) {
            if (!it.isNullOrEmpty() && it.contains("beardsevenss.monster")) {
                Log.d("FullLink", it)
                webView.loadUrl(it)
            }
        }
    }

    private fun initWebView() {
        webView = binding.mswView

        webView.webViewClient = LocalClient()
        webView.settings.javaScriptEnabled = true
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        webView.settings.domStorageEnabled = true
        webView.settings.loadWithOverviewMode = false

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
            }

            //For Android API >= 21 (5.0 OS)
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri?>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                messageAb = filePathCallback
                selectImageIfNeed()
                return true
            }

            override fun onCreateWindow(
                view: WebView?, isDialog: Boolean,
                isUserGesture: Boolean, resultMsg: Message
            ): Boolean {
                val newWebView = WebView(applicationContext)
                newWebView.settings.javaScriptEnabled = true
                newWebView.webChromeClient = this
                newWebView.settings.javaScriptCanOpenWindowsAutomatically = true
                newWebView.settings.domStorageEnabled = true
                newWebView.settings.setSupportMultipleWindows(true)
                val transport = resultMsg.obj as WebView.WebViewTransport
                transport.webView = newWebView
                resultMsg.sendToTarget()
                return true
            }
        }
    }

    private fun selectImageIfNeed() {
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = image1
        startActivityForResult(
            Intent.createChooser(i, imageTitle),
            resultCode
        )
    }

    private inner class LocalClient : WebViewClient() {

        override fun onReceivedError(
            view: WebView?,
            errorCode: Int,
            description: String?,
            failingUrl: String?
        ) {
            super.onReceivedError(view, errorCode, description, failingUrl)
            if (errorCode == -2) {

            }
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            viewModel.flag.observe(this@Wrapper) {
                if (it == "0" && !url?.contains("trident")!! && !url.contains("beardsevenss.monster"))
                    viewModel.saveFullLinkInDataBase(url, "1")
            }
        }

    }
}

