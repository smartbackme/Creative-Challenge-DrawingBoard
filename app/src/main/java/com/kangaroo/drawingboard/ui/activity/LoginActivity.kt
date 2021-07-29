package com.kangaroo.drawingboard.ui.activity

import PASSWORD
import android.R.attr.password
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.core.content.ContextCompat
import com.gyf.immersionbar.ktx.hideStatusBar
import com.gyf.immersionbar.ktx.immersionBar
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.exceptions.HyphenateException
import com.kangaroo.drawingboard.R
import com.kangaroo.drawingboard.data.model.UserModel
import com.kangaroo.drawingboard.tools.UStore
import com.kangaroo.drawingboard.ui.presenter.LoginPresenter
import com.kangaroo.drawingboard.ui.view.LoginView
import com.kangraoo.basektlib.tools.HString
import com.kangraoo.basektlib.tools.UFont
import com.kangraoo.basektlib.tools.encryption.MessageDigestUtils
import com.kangraoo.basektlib.tools.launcher.LibActivityLauncher
import com.kangraoo.basektlib.tools.task.TaskManager
import com.kangraoo.basektlib.tools.tip.Tip
import com.kangraoo.basektlib.ui.mvp.BMvpActivity
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarListener
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarOptions
import kotlinx.android.synthetic.main.activity_login.*
import net.margaritov.preference.colorpicker.ColorPickerDialog


/**
 * 自动生成：by WaTaNaBe on 2021-06-21 11:03
 * #登录#
 */
class LoginActivity : BMvpActivity<LoginView, LoginPresenter>(), LoginView,
    ColorPickerDialog.OnColorChangedListener {

    companion object{

        fun startFrom(activity: Activity) {
            LibActivityLauncher.instance
                .launch(activity, LoginActivity::class.java)
        }

    }

    override fun getLayoutId() = R.layout.activity_login


    override fun onViewCreated(savedInstanceState: Bundle?) {
        immersionBar {
            hideStatusBar()
        }


        login.setOnClickListener {
            showProgressingDialog()

            if((!TextUtils.isEmpty(user.text?.toString()?.trim()))){
                TaskManager.taskExecutor.execute(Runnable {
                    try {
                        EMClient.getInstance().createAccount(
                            user.text.toString().trim(), MessageDigestUtils.sha1(
                                PASSWORD
                            )
                        )
                        dismissProgressDialog()

                        EMClient.getInstance().login(user.text.toString().trim(), MessageDigestUtils.sha1(PASSWORD), object : EMCallBack {
                            //回调
                            override fun onSuccess() {
                                dismissProgressDialog()
                                UStore.putUser(UserModel(user.text.toString().trim(),color = color))
                                MainActivity.startFrom(visitActivity())
                                finish()
//                        EMClient.getInstance().groupManager().loadAllGroups()
//                        EMClient.getInstance().chatManager().loadAllConversations()
                            }

                            override fun onProgress(progress: Int, status: String) {
                            }
                            override fun onError(code: Int, message: String) {
                                dismissProgressDialog()
                                showToastMsg(Tip.Error,"用户名密码错误")
                            }
                        })
                    } catch (e: HyphenateException){
                        dismissProgressDialog()
                        runOnUiThread {
                            toastMessage.text = "该用户名已被注册"
                        }
                        showToastMsg(Tip.Error, "该用户名已被注册")
                    }
                })


            }else{
                dismissProgressDialog()
                showToastMsg(Tip.Error,"用户名不能为空")
            }
        }
        color = ContextCompat.getColor(visitActivity(),R.color.black)
        val colorPickerDialog = ColorPickerDialog(this,color);

        colorPickerDialog.setOnColorChangedListener(this);//调用颜色更改接口
        //是否显示透明度状态栏
        colorPickerDialog.setAlphaSliderVisible(true);
        //是否显示透明度的值
        colorPickerDialog.setHexValueEnabled(true);
        colorSelect.setOnClickListener {

            colorPickerDialog.show();

        }

    }

    override fun createPresenterInstance(): LoginPresenter {
        return LoginPresenter()
    }

    var color:Int = 0

    override fun onColorChanged(color: Int) {
        this.color = color
        colorSelect.setBackgroundColor(color)
    }

}
