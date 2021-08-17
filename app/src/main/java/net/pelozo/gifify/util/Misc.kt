package net.pelozo.gifify.util

import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import net.pelozo.gifify.R

class Misc {

    companion object{
        val glideOptions: RequestOptions = RequestOptions()
            .placeholder(R.drawable.progress_anim)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)

    }
}