package com.kangaroo.drawingboard.tools

import android.graphics.Path
import com.jeremyliao.liveeventbus.LiveEventBus
import com.kangaroo.drawingboard.data.model.*
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.HString
import com.kangraoo.basektlib.tools.UTime
import com.kangraoo.basektlib.tools.UUi
import com.kangraoo.basektlib.tools.json.HJson
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.store.MMKVStore
import com.kangraoo.basektlib.tools.store.MemoryStore

/**
 * @author shidawei
 * 创建日期：2021/6/21
 * 描述：
 */
const val USER:String = "user"
const val DATA:String = "data"
const val MESSAGE:String = "message"
const val HOTIM:String = "hotim"

object UStore {

    fun putUser(user : UserModel){
        MMKVStore.instance(HOTIM).put(USER,user)
    }

    fun getUser():UserModel? = MMKVStore.instance(HOTIM).get(USER,null,UserModel::class.java)

    fun clearUser(){
        MMKVStore.instance(HOTIM).remove(USER)
    }
    var map:HashMap<String,UserPath> = HashMap<String,UserPath>()

    fun path(data: DrawanPath) {
        var path = map[data.name]
        if(path==null){
            path = UserPath(data.color,Path())
            map[data.name] = path
        }
        if(data.type == moveTo){
            path.path.moveTo((data.x/data.width)* UUi.getWidth(SApplication.context()),(data.y/data.height)* UUi.getHeight(SApplication.context()))
        }else if(data.type == lineTo){
            path.path.lineTo((data.x/data.width)* UUi.getWidth(SApplication.context()),(data.y/data.height)* UUi.getHeight(SApplication.context()))
        }
    }


}