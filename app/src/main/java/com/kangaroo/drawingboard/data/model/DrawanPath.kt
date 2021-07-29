package com.kangaroo.drawingboard.data.model

import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.UUi
import com.kangraoo.basektlib.tools.json.HJson
import com.squareup.moshi.JsonClass

/**
 * @author shidawei
 * 创建日期：2021/7/29
 * 描述：
 */
const val moveTo = 1
const val lineTo = 2
@JsonClass(generateAdapter = true)
class DrawanPath @JvmOverloads constructor(val x:Float,val y:Float,val type:Int = moveTo,val name:String,val color:Int,val width:Int = UUi.getWidth(SApplication.context()),val height:Int = UUi.getHeight(SApplication.context())){
    fun toJson() = HJson.toJson(this)
}