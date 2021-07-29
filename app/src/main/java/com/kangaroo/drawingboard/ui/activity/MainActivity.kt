package com.kangaroo.drawingboard.ui.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.gyf.immersionbar.ktx.immersionBar
import com.kangraoo.basektlib.ui.BActivity
import com.kangraoo.basektlib.widget.toolsbar.LibToolBarOptions
import com.kangraoo.basektlib.widget.toolsbar.OnLibToolBarListener
import kotlinx.android.synthetic.main.activity_main.*
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarListener
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarOptions
import com.kangraoo.basektlib.tools.launcher.LibActivityLauncher
import android.app.Activity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeremyliao.liveeventbus.LiveEventBus
import com.kangaroo.drawingboard.R
import com.kangaroo.drawingboard.data.model.params.TokenPostParams
import com.kangaroo.drawingboard.data.source.AppRepository
import com.kangaroo.drawingboard.tools.MqttUtil
import com.kangaroo.drawingboard.tools.UStore
import com.kangraoo.basektlib.data.DataResult
import com.kangraoo.basektlib.data.succeeded
import com.kangraoo.basektlib.tools.encryption.MessageDigestUtils
import com.kangraoo.basektlib.tools.json.HJson
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.tip.Tip
import com.kangraoo.basektlib.widget.dialog.LibCheckDialog
import com.qdedu.baselibcommon.ui.activity.WebPageActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * 自动生成：by WaTaNaBe on 2021-07-22 14:25
 * #首页#
 */
class MainActivity : BActivity(){

    companion object{

        fun startFrom(activity: Activity) {
            LibActivityLauncher.instance
                .launch(activity, MainActivity::class.java)
        }

    }

    override fun getLayoutId() = R.layout.activity_main



    override fun onViewCreated(savedInstanceState: Bundle?) {
        immersionBar {
            statusBarDarkFont(true)
            statusBarColor(R.color.theme)
        }

        val libToolBarOptions = CommonToolBarOptions()
        libToolBarOptions.titleString = "互动画板"
        libToolBarOptions.background = R.color.theme
        libToolBarOptions.titleColor = R.color.white
        libToolBarOptions.setNeedNavigate(false)
        setToolBar(R.id.toolbar, libToolBarOptions, object : CommonToolBarListener(){})
        launch {
            val user = UStore.getUser()
            showProgressingDialog("加载数据中")
            var data = AppRepository.instance.tokenPost(
                TokenPostParams(
                    username = user!!.name, password = MessageDigestUtils.sha1(
                        user!!.pass
                    )
                )
            )
            if(data.succeeded){
                if (data is DataResult.Success) {
                    user.token = data.data.access_token
                    UStore.putUser(user)
                    withContext(Dispatchers.IO){
                        MqttUtil.mqttService()
                        dismissProgressDialog()
                    }
                } else {
                    dismissProgressDialog()
                    showToastMsg(Tip.Error, "加载失败")
                }
            }else{
                dismissProgressDialog()
                showToastMsg(Tip.Error, "加载失败")
            }

        }

    }

    override fun onDestroy() {
//        MqttUtil.message(MqttUtil.DeRen, UStore.getUser()?.name?:"")
        MqttUtil.unsubscribe()
        super.onDestroy()
    }



}
