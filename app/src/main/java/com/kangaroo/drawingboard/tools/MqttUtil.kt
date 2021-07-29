package com.kangaroo.drawingboard.tools

import android.util.Log
import com.jeremyliao.liveeventbus.LiveEventBus
import com.kangaroo.drawingboard.data.model.*
import com.kangraoo.basektlib.tools.json.HJson
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.task.TaskManager
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * @author shidawei
 * 创建日期：2021/6/4
 * 描述：
 */
object MqttUtil {
    var move = "MOVE"



    var mqttClient: MqttClient? = null
    val  executorService  = ThreadPoolExecutor(
            1,
            1,
            0,
            TimeUnit.MILLISECONDS,
            LinkedBlockingQueue()
    )
    val qosLevel = 0
    val qosLevelNomar = 1
    val qosLevelHight = 2

    fun mqttService() {
        try {
            initmqtt()
            extended()
            lineService()
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    fun initmqtt() {
        val user = UStore.getUser()

        var  deviceId = user!!.name
        var appId = "d4woe0"

        var endpoint =  "d4woe0.cn1.mqtt.chat"

//        clientId由两部分组成，格式为DeviceID@appId，其中DeviceID由业务方自己设置，appId在console控制台创建，clientId总长度不得超过64个字符。
        var  clientId = "$deviceId@$appId";

//  QoS参数代表传输质量，可选0，1，2。详细信息，请参见名词解释。
        val memoryPersistence = MemoryPersistence()
        try {
            mqttClient = MqttClient("tcp://$endpoint:1883", clientId, memoryPersistence)
        }catch (e: MqttException){
            e.printStackTrace()
        }

//        设置客户端发送超时时间，防止无限阻塞。
        mqttClient?.setTimeToWait(5000)
    }

    fun lineService() {
        val user = UStore.getUser()

        val mqttConnectOptions = MqttConnectOptions()

        /**
         * 用户名，在console中注册
         */
        mqttConnectOptions.userName = user!!.name

        /**
         * 用户密码为第一步中申请的token
         */
        mqttConnectOptions.password = user!!.token!!.toCharArray()
        mqttConnectOptions.isCleanSession = true
        mqttConnectOptions.keepAliveInterval = 90
        mqttConnectOptions.isAutomaticReconnect = true
        mqttConnectOptions.mqttVersion = MqttConnectOptions.MQTT_VERSION_3_1_1
        mqttConnectOptions.connectionTimeout = 5000
        try {
            mqttClient?.connect(mqttConnectOptions)
        } catch (e: MqttException) {
            e.printStackTrace();
        }
        // 暂停1秒钟，等待连接订阅完成
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun unsubscribe() {
        try {
            mqttClient?.unsubscribe(arrayOf(move));
        } catch (e: MqttException) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     */
    fun message(type:String,message: String) {

        try {
            /**
             * 构建一个Mqtt消息
             */
            val message = MqttMessage(message.toByteArray())
            //设置传输质量
            message.qos = qosLevel
            /**
             * 发送普通消息时，Topic必须和接收方订阅的Topic一致，或者符合通配符匹配规则。
             */
            TaskManager.taskExecutor.execute(Runnable {
                try {
                    mqttClient?.publish(type, message)
                }catch (e1: MqttException){
                    e1.printStackTrace();
                }
            })


        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    var threadExecutor = Executors.newSingleThreadExecutor()

    var inithuati = false

    fun extended() {
        val user = UStore.getUser()

        mqttClient?.setCallback(object : MqttCallbackExtended {
            override fun connectionLost(cause: Throwable?) {
            }

            /**
             * 接收消息回调方法
             * @param s
             * @param mqttMessage
             */
            override fun messageArrived(s: String, mqttMessage: MqttMessage) {
                var st = String(mqttMessage.payload)
                when(s){
                    move -> {
                        var data = HJson.fromJson<DrawanPath>(st)!!
                        if(user!=null&&data.name != user.name){
                            UStore.path(data)
                        }
                    }
                }
//                when (s) {
//                    lacn -> {
//                        var st = String(mqttMessage.payload)
//                        if(user!=null&&st != user.name){
//                            if(UStore.getUserList()!=null){
//                                /**
//                                 * 发送数据给其他用户，但要排除自己
//                                 */
//                                message(cn,HJson.toJson(UStore.getUserList()!!.apply {
//                                    username = st
//                                }))
//
//                                ULog.d(st,"发送拉取数据请求",user.name,"接受到数据请求，并返回数据",HJson.toJson(UStore.getUserList()))
//
//                            }
//                        }
//                    }
//                    laun -> {
//                        var st = String(mqttMessage.payload)
//                        if(user!=null&&st != user.name){
//                            if(UStore.getLian()!=null){
//                                /**
//                                 * 发送数据给其他用户，但要排除自己
//                                 */
//                                message(un,HJson.toJson(CoinNodeModel(username = st,coinNode = UStore.getLian()!!)))
//
//                                ULog.d(st,"发送拉取链请求",user.name,"接受到数据请求，并返回数据",HJson.toJson(CoinNodeModel(username = st,coinNode = UStore.getLian()!!)))
//                            }
//                        }
//                    }
//                    cn -> {
//                        var st = String(mqttMessage.payload)
//                        var users =  HJson.fromJson<UserList>(st)
//                        ULog.d(user?.name,"接收到数据请求",HJson.toJson(users))
//
//                        if(users?.username!=null){
//                            if(user!=null&&users.username == user.name){
//                                UStore.putUserListAll(users)
//                                ULog.d(user.name,"接收到数据请求并存入数据")
//                            }
//                        }else if(users?.extusername!=null){
//                            if(user!=null&&users.extusername != user.name){
//                                UStore.putUserListAll(users)
//                                ULog.d(user.name,"接收到数据请求并存入数据")
//                            }
//                        }
//                    }
//                    un -> {
//                        threadExecutor.execute(Runnable {
//                            var st = String(mqttMessage.payload)
//                            var lian =  HJson.fromJson<CoinNodeModel>(st)
//                            ULog.d(user?.name,"接收到链请求",HJson.toJson(lian))
//
//                            if(lian?.username!=null){
//                                if(user!=null&&lian.username == user.name){
//                                    UStore.putLian(lian)
//                                    ULog.d(user.name,"接收到数据请求并存入数据")
//                                }
//                            }else if(lian?.extusername!=null){
//                                if(user!=null&&lian.extusername != user.name){
//                                    UStore.putLian(lian)
//                                    ULog.d(user.name,"接收到数据请求并存入数据")
//                                }
//                            }
//                        })
//
//
//                    }
//
//                }

            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
            }

            /**
             * 连接完成回调方法
             * @param b
             * @param s
             */
            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                /**
                 * 客户端连接成功后就需要尽快订阅需要的Topic。
                 */
                println("connect success")
                Log.d("connect", "connect success")
                executorService.submit {
                    try {
                        val topicFilter = arrayOf<String>(move)
                        val qos = intArrayOf(qosLevel)
                        mqttClient?.subscribe(topicFilter, qos)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }
}