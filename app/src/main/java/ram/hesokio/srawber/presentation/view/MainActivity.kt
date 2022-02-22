package ram.hesokio.srawber.presentation.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.commit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.facebook.applinks.AppLinkData
import com.facebook.internal.Utility
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.onesignal.OneSignal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ram.hesokio.srawber.R
import ram.hesokio.srawber.databinding.MainActivityBinding
import ram.hesokio.srawber.domain.CheckNetwork
import ram.hesokio.srawber.domain.MyOneSignal
import ram.hesokio.srawber.domain.Parsing
import ram.hesokio.srawber.domain.WorkWithSharedPref
import ram.hesokio.srawber.presentation.view.game_view.progress_bar.FProgress
import ram.hesokio.srawber.presentation.view_model.MainViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModel<MainViewModel>()

    lateinit var binding: MainActivityBinding

    private lateinit var parsing: Parsing
    var fullLink: String? = null

    private val checkInet = CheckNetwork(this)
    private lateinit var workWithSharedPref: WorkWithSharedPref
    private val myOneSignal = MyOneSignal()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        workWithSharedPref = WorkWithSharedPref(this)
        parsing = Parsing(this)

        viewModel.getFullLinkFromDataBase()

        if (checkInet.checkForInternet()) {
            initObserver()
        } else
            startGameView()
    }

    private fun workWithApps(appId: String) {
        Log.d("AppLog", appId)

        val conversionDataListener = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {

                AppLinkData.fetchDeferredAppLinkData(this@MainActivity) {
                    fullLink = parsing.concatName(
                        data = data,
                        deep = it?.targetUri.toString(),
                        gadid = appId,
                        af_id = AppsFlyerLib.getInstance()
                            .getAppsFlyerUID(this@MainActivity),
                        link = workWithSharedPref.getLink()!!
                    )
                    Log.d("AppLog", fullLink!!)

                    OneSignal.setExternalUserId(appId)
                    viewModel.saveFullLinkInDataBase(fullLink!!, "0")
                    myOneSignal.workWithOneSignal(data, it?.targetUri.toString())

                    startWebView()
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

    private fun getAppId(): LiveData<String> {
        val advertisingId = MutableLiveData<String>()

        lifecycleScope.launch(Dispatchers.IO) {
            val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(this@MainActivity)
            advertisingId.postValue(adInfo.id.toString())
        }
        return advertisingId
    }

    private fun initObserver() {
        viewModel.fullLink.observe(this@MainActivity) {
            if (it == "null" || it.isNullOrEmpty()) {
                getAppId().observe(this@MainActivity) { appId ->
                    if (!appId.isNullOrEmpty())
                        workWithApps(appId)
                }
            } else {
                startWebView()
            }
        }
    }

    private fun startGameView() {
        OneSignal.sendTag("key1", "bot")

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.frgmContainer, FProgress())
        }
    }

    private fun startWebView() {
        startActivity(Intent(this, Wrapper::class.java))
        finish()
    }
}