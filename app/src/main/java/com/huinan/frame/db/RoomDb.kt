package com.huinan.frame.db

import androidx.room.Database
import androidx.room.RoomDatabase

//@Database(
//    entities = [OfferRecordBean::class],
//    version = 2,
//    exportSchema = false
//)
abstract class RoomDb : RoomDatabase() {
    abstract fun getTaskRecordDao(): OfferRecordDao
}