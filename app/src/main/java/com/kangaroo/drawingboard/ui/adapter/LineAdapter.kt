package com.kangaroo.drawingboard.ui.adapter

import android.graphics.drawable.ColorDrawable
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kangaroo.drawingboard.R
import com.kangaroo.drawingboard.data.model.User
import kotlinx.android.synthetic.main.item_man.view.*

/**
 * @author shidawei
 * 创建日期：2021/7/19
 * 描述：
 */
class LineAdapter(data: MutableList<User>): BaseQuickAdapter<User, BaseViewHolder>(
    R.layout.item_man,data) {
    override fun convert(holder: BaseViewHolder, item: User) {
        holder.itemView.liveNum.text = item.name
        holder.itemView.liveNum.setTextColor(item.color)
    }
}


