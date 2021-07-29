package com.kangaroo.drawingboard.data.service

import com.kangaroo.drawingboard.data.model.params.TokenPostParams
import com.kangaroo.drawingboard.data.model.reponses.TokenPostResponse
import com.qdedu.baselibcommon.data.model.responses.BasicApiResult
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {



    /**
     * 自动生成：by WaTaNaBe on 2021-06-21 16:18.
     * #tokenPost#
     * #tokenPost#
     */
    @POST(ApiMethods.tokenPost)
    suspend fun tokenPostAsync(@Body params: TokenPostParams): Response<TokenPostResponse>



//#06#
}
