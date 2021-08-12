package net.pelozo.gifify.model.giphyApi.model

import com.squareup.moshi.Json

data class Gif(
    val id: String,
    val title: String,
    val images: GifImages
)

data class GifImages(
    val original: GifImage,
    @field:Json(name="fixed_height")val fixedHeight: GifImage
)

data class GifImage(
    val height: Int,
    val width: Int,
    val url: String
)
