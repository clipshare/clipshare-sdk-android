package tv.clipshare.example

import android.app.Application

class Application: Application() {

    override fun onCreate() {
        super.onCreate()

        com.snapscreen.mobile.Snapscreen.setup(this, "your-client-id", "your-client-secret", com.snapscreen.mobile.Environment.TEST)

        tv.clipshare.mobile.ClipShare.setup(tv.clipshare.mobile.Environment.TEST)
    }
}