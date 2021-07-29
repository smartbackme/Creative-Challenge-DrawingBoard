package com.kangaroo.drawingboard.data.source

import com.kangaroo.drawingboard.data.model.params.TokenPostParams
import com.kangaroo.drawingboard.data.model.reponses.TokenPostResponse
import com.kangraoo.basektlib.data.DataResult
import com.qdedu.baselibcommon.data.model.responses.BasicApiResult

/**
 * 自动生成：by WaTaNaBe on 2021-06-21 16:18.
 * AppDataSource
 */
interface AppDataSource {


    /**
     * 自动生成：by WaTaNaBe on 2021-06-21 16:18.
     * #tokenPost#
     * #tokenPost#
     */
    suspend fun tokenPost( param: TokenPostParams): DataResult<TokenPostResponse>

//#06#
}
