package com.kangaroo.drawingboard.data.model.reponses

import com.squareup.moshi.JsonClass

/**
 * 自动生成：by WaTaNaBe on 2021-06-21 16:18.
 * tokenPost
 */
@JsonClass(generateAdapter = true)
data class TokenPostResponse(
    val access_token: String,
    val expires_in: Int,
    val user: User
)

@JsonClass(generateAdapter = true)
data class User(
    val activated: Boolean,
    val created: Long,
    val modified: Long,
    val type: String,
    val username: String,
    val uuid: String
)