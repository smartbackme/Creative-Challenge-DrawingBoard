package com.kangaroo.drawingboard.tools

import android.os.CountDownTimer
import com.kangraoo.basektlib.tools.UReflection
import com.kangraoo.basektlib.tools.UTime
import com.kangraoo.basektlib.tools.log.ULog
import java.lang.reflect.Field

/**
 * @author shidawei
 * 创建日期：2021/7/23
 * 描述：
 */
class MyDownTimer(millisInFuture: Long, countDownInterval: Long,var iTimer:ITimer):
        CountDownTimer(millisInFuture, countDownInterval) {
    interface ITimer{
        fun time(time: String,millisUntilFinished: Long)
        fun timeFinish()
        fun timeStart()
    }

    override fun onTick(millisUntilFinished: Long) {
        iTimer.time(UTime.dateTimeString(millisUntilFinished,"mm:ss"),millisUntilFinished)
    }

    override fun onFinish() {
        iTimer.timeFinish()
    }

    fun myStart(){
        iTimer.timeStart()
        start()
    }
}