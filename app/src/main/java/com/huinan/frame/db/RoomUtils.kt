package com.huinan.frame.db

import android.content.Context
import androidx.room.Room
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Room数据库操作工具类
 */
@Singleton
class RoomUtils @Inject constructor(@ApplicationContext context: Context) {

    private val db: RoomDb = Room.databaseBuilder(
        context,
        RoomDb::class.java,
        "DB_NAME"
    )
        .fallbackToDestructiveMigration().build()

//    /**
//     * 插入任务记录
//     * @param bean 要插入的任务记录实体
//     */
//    fun insertTaskRecordBean(bean: OfferRecordBean) {
//        db.getTaskRecordDao().insert(bean)
//    }
}
