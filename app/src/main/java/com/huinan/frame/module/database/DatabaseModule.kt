package com.huinan.frame.module.database

import android.content.Context
import androidx.room.Room
import com.huinan.frame.db.OfferRecordDao
import com.huinan.frame.db.RoomDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDb(@ApplicationContext context: Context): RoomDb {
        return Room.databaseBuilder(
            context,
            RoomDb::class.java,
            "DB_NAME"
        )
            .fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideTaskRecordDao(db: RoomDb): OfferRecordDao {
        return db.getTaskRecordDao()
    }
}