package ram.hesokio.srawber.app

import android.app.Application
import com.onesignal.OneSignal
import io.realm.Realm
import io.realm.RealmConfiguration
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ram.hesokio.srawber.di.appModule
import ram.hesokio.srawber.di.dbModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initRealm()
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@App)
            modules(
                listOf(
                    dbModule,
                    appModule
                )
            )
        }

        OneSignal.initWithContext(applicationContext)
        OneSignal.setAppId("b4a9c84a-a307-453d-af0b-83eb0567beb7")  //THIS FIELD IN EVERY PROJECT IS UNIQUE !!!
    }

    private fun initRealm() {
        Realm.init(this@App)
        val config = RealmConfiguration.Builder()
            .name("data.db")
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config);
    }
}