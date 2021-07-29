package com.kangaroo.drawingboard.app

import android.content.Context
import androidx.multidex.MultiDex
import com.kangaroo.drawingboard.BuildConfig
import com.kangaroo.drawingboard.R
import com.kangraoo.basektlib.app.ActivityLifeManager
import com.kangraoo.basektlib.app.BaseLibActivityLifecycleCallbacks
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.app.SConfiger
import com.kangraoo.basektlib.app.init.DownLoadInit
import com.kangraoo.basektlib.app.init.IInit
import com.kangraoo.basektlib.tools.log.LogConfig
import com.kangraoo.basektlib.tools.tip.Tip
import com.kangraoo.basektlib.tools.tip.TipToast
import com.qdedu.baselibcommon.app.init.ArouterInit
import java.util.concurrent.Executors


const val LOAD_NEW_PATH = "loadNewPatch"
const val LOAD_NEW_PATH_TIME = 4320000L


class App : SApplication() {

    override fun configer(): SConfiger = SConfiger.build {
        //crash 采用bugly
        logConfig = LogConfig.build {
            tag = "APP"
        }
        appSafeCode = "APP_CODE_123456"
        consoleOutwindow = false
        applog = R.mipmap.ic_launcher
        debugStatic = BuildConfig.IS_DEV
    }



    override fun init() {

    }

    override fun onTerminate() {
        super.onTerminate()
    }

    override fun appInit(): IInit {
        return AppInit(ArouterInit(DownLoadInit(super.appInit())))
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        /**
         * 加入热修复后提前到热修复的application中
         */
        MultiDex.install(this)
    }

    override fun getActivityLifecycleCallbacks(): MutableList<ActivityLifecycleCallbacks> {
        var list = super.getActivityLifecycleCallbacks()
        list.add(BaseLibActivityLifecycleCallbacks())
        return list
    }

}
