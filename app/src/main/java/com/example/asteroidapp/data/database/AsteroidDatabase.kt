package com.example.asteroidapp.data.database

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.asteroidapp.api.getEndDate
import com.example.asteroidapp.api.getTodayDate
import com.example.asteroidapp.api.models.AsteroidModel
import com.example.asteroidapp.api.models.ImageOfTodayModel
import kotlinx.coroutines.flow.Flow


@Dao
interface AsteroidDao {

    @Query("select * from asteroid_data where closeApproachDate >= :startDate and closeApproachDate <= :endData order by closeApproachDate asc")
    fun getAsteroidsList(
        startDate: String = getTodayDate(), endData: String = getEndDate()
    ): PagingSource<Int, AsteroidModel>

    @Query("select * from asteroid_data")
    fun getAllAsteroid(
    ): Flow<List<AsteroidModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroidList: AsteroidModel)

}

@Dao
interface ImageOfTodayDao {
    @Query("select * from image_of_day_data where :currentDate = date or :currentDate = creationDate")
    fun getImageOfToday(currentDate: String): Flow<ImageOfTodayModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImageOfToday(imageOfTodayModel: ImageOfTodayModel)
}

@Database(entities = [AsteroidModel::class, ImageOfTodayModel::class], version = 3)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
    abstract val imageOfTodayDao: ImageOfTodayDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext, AsteroidDatabase::class.java, "asteroids"
            ).addMigrations(MIGRATION_2_3).build()
        }
    }
    return INSTANCE

}

val MIGRATION_2_3: Migration = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE image_of_day_data " + " ADD COLUMN creationDate TEXT default '' NOT NULL"
        )
    }
}
