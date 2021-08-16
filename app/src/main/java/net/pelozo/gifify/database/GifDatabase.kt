package net.pelozo.gifify.database

import androidx.room.Database
import androidx.room.RoomDatabase
import net.pelozo.gifify.model.Gif

@Database(
    entities = [Gif::class],
    version = 1,
)
abstract class GifDatabase : RoomDatabase() {
    abstract val gifDao: GifDao
}