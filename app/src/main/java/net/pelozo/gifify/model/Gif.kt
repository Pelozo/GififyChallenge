package net.pelozo.gifify.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Gif(
    @PrimaryKey val id: String,
    val title: String,
    val urlImageOriginal: String,
    val urlImageDownsized: String
) {

    companion object{
        fun fromDto(gifDto: GifDto): Gif {
            return Gif(
                gifDto.id,
                gifDto.title,
                gifDto.images.original.url,
                gifDto.images.fixedHeight.url
            )
        }
    }
}