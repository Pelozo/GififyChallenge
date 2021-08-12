package net.pelozo.gifify

import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar


fun ImageView.glideLoad(url: String) {
    Glide.with(this)
        .load(url)
        .apply(glideOptions)
        .into(this)
}

val glideOptions: RequestOptions = RequestOptions()
    .placeholder(R.drawable.progress_anim)
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .priority(Priority.HIGH)


fun Fragment.showSnackbar(message: String, length: Int = Snackbar.LENGTH_SHORT) = view?.run { Snackbar.make(this, message, length).show()}