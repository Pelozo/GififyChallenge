package net.pelozo.gifify.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import net.pelozo.gifify.model.Gif

@Dao
interface GifDao {
    @Query("SELECT * FROM Gif")
    fun getAll(): Flow<List<Gif>>

    @Query("SELECT * FROM Gif WHERE id = :id")
    suspend fun getOne(id: String): Gif

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGif(gif: Gif)

    @Delete
    suspend fun delete(user: Gif)
}