package com.kangaroo.drawingboard.data.source

import com.kangaroo.drawingboard.data.model.params.TokenPostParams
import com.kangaroo.drawingboard.data.source.local.AppLocalDataSource
import com.kangaroo.drawingboard.data.source.remote.AppRemoteDataSource
import com.kangraoo.basektlib.data.source.BaseRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * 自动生成：by WaTaNaBe on 2021-06-21 16:18.
 * AppRepository
 */
class AppRepository : BaseRepository<AppLocalDataSource, AppRemoteDataSource>(AppLocalDataSource(),AppRemoteDataSource()),AppDataSource {

    companion object{
        val instance: AppRepository by lazy {
            AppRepository()
        }
    }
    val ioDispatcher: CoroutineDispatcher = Dispatchers.IO


    /**
     * 自动生成：by WaTaNaBe on 2021-06-21 16:18.
     * #tokenPost#
     * #tokenPost#
     */
    override suspend fun tokenPost( param: TokenPostParams) = remoteDataSource.tokenPost(param)


//#06#
}
