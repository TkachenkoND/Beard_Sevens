package ram.hesokio.srawber.presentation.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.facebook.applinks.AppLinkData
import com.facebook.internal.Utility
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.onesignal.OneSignal
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ram.hesokio.srawber.databinding.MainActivityBinding
import ram.hesokio.srawber.domain.CheckNetwork
import ram.hesokio.srawber.domain.MyOneSignal
import ram.hesokio.srawber.domain.Parsing
import ram.hesokio.srawber.domain.WorkWithSharedPref
import ram.hesokio.srawber.presentation.view_model.MainViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModel<MainViewModel>()

    lateinit var binding: MainActivityBinding

    val parsing = Parsing()
    var fullLink: String? = null

    private val checkInet = CheckNetwork(this)
    private lateinit var workWithSharedPref: WorkWithSharedPref
    private val myOneSignal = MyOneSignal()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        workWithSharedPref = WorkWithSharedPref(this)

        viewModel.getFullLinkFromDataBase()

        if (checkInet.checkForInternet()) {
            initObserver()
        } else
            startGameView()
    }

    private fun workWithApps() {
        val conversionDataListener = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
                Log.d("AppLog", data.toString())
                AppLinkData.fetchDeferredAppLinkData(this@MainActivity) {
                    Log.d("AppLog", it?.targetUri.toString())
                    fullLink = parsing.concatName(
                        data = data,
                        deep = it?.targetUri.toString(),
                        gadid = getAppId(),
                        af_id = AppsFlyerLib.getInstance()
                            .getAppsFlyerUID(this@MainActivity),
                        application_id = this@MainActivity.packageName,
                        link = workWithSharedPref.getLink()!!
                    )
                    Log.d("AppLog", fullLink!!)

                    viewModel.saveFullLinkInDataBase(fullLink!!)

                    startWebView(fullLink!!)
                    myOneSignal.workWithOneSignal(data, it?.targetUri.toString())
                }
            }

            override fun onConversionDataFail(error: String?) {
                Log.e(Utility.LOG_TAG, "error onAttributionFailure :  $error")
            }

            override fun onAppOpenAttribution(data: MutableMap<String, String>?) {
                data?.map {
                    Log.d(Utility.LOG_TAG, "onAppOpen_attribute: ${it.key} = ${it.value}")
                }
            }

            override fun onAttributionFailure(error: String?) {
                Log.e(Utility.LOG_TAG, "error onAttributionFailure :  $error")
            }
        }
        AppsFlyerLib.getInstance().init(
            workWithSharedPref.getAppsKey()!!,
            conversionDataListener,
            applicationContext
        )
        AppsFlyerLib.getInstance().start(this)

    }

    private fun getAppId(): String {
        var advertisingId = ""

        GlobalScope.launch {
            val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(this@MainActivity)
            advertisingId = adInfo.id.toString()
            OneSignal.setExternalUserId(advertisingId)
        }
        return advertisingId
    }

    private fun initObserver() {
        lifecycleScope.launch {
            viewModel.fullLink.observe(this@MainActivity) {
                if (it == "null" || it.isNullOrEmpty()) {
                    workWithApps()
                } else {
                    startWebView(it)
                }
            }
        }
    }

    private fun startGameView() {
        OneSignal.sendTag("key1", "bot")

        Toast.makeText(this, "запускаємо гру !!", Toast.LENGTH_SHORT).show()
        //startActivity(Intent(this, ActivityProgressBar::class.java))
        //finish()
    }

    private fun startWebView(url: String) {
        Toast.makeText(this, "запускаємо вебку!!", Toast.LENGTH_SHORT).show()

        if (url.isNotEmpty()) {
            startActivity(Intent(this, ViewWebka::class.java).putExtra("url", url))
            finish()
        }
    }
}