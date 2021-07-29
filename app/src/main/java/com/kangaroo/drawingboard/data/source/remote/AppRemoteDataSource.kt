package com.kangaroo.drawingboard.data.source.remote

import com.kangaroo.drawingboard.data.model.params.TokenPostParams
import com.kangaroo.drawingboard.data.source.AppDataSource
import com.kangaroo.drawingboard.data.source.AppService
import com.kangraoo.basektlib.data.DataResult
import com.kangraoo.basektlib.data.source.remote.BaseRemoteDataSource
import com.kangraoo.basektlib.exception.LibNetWorkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.kangraoo.basektlib.data.netError
import com.qdedu.baselibcommon.data.netSuccess

/**
 * 自动生成：by WaTaNaBe on 2021-06-21 16:18.
 * AppRemoteDataSource
 */
 class AppRemoteDataSource internal constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
 ) : BaseRemoteDataSource(), AppDataSource {



    /**
     * 自动生成：by WaTaNaBe on 2021-06-21 16:18.
     * #tokenPost#
     * #tokenPost#
     */
    override suspend fun tokenPost(param: TokenPostParams)= withContext(ioDispatcher) {
        try {
            val data = AppService.getApiService("http://a1.easemob.com/").tokenPostAsync(param)
            if(data.isSuccessful) {
                return@withContext DataResult.Success(data.body()!!).netSuccess()
            }else{
                return@withContext DataResult.Error(LibNetWorkException(data.code(),data.message())).netError()
            }
        } catch (e: Exception) {
            return@withContext DataResult.Error(e).netError()
        }
    }


//#06#
 }
