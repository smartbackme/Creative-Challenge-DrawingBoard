package com.kangaroo.drawingboard.data.source.local

import com.kangaroo.drawingboard.data.model.params.TokenPostParams
import com.kangaroo.drawingboard.data.model.reponses.TokenPostResponse
import com.kangaroo.drawingboard.data.source.AppDataSource
import com.kangraoo.basektlib.data.DataResult
import com.kangraoo.basektlib.data.source.local.BaseLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * 自动生成：by WaTaNaBe on 2021-06-21 16:18.
 * AppLocalDataSource
 */
public class AppLocalDataSource internal constructor(
     private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseLocalDataSource(), AppDataSource {


    /**
     * 自动生成：by WaTaNaBe on 2021-06-21 16:18.
     * #tokenPost#
     * #tokenPost#
     */
    override suspend fun tokenPost(param: TokenPostParams):  DataResult<TokenPostResponse> {
        TODO("Not yet implemented")
    }


//#06#
}
