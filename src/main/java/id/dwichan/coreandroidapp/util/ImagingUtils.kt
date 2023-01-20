package id.dwichan.coreandroidapp.util

import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import id.dwichan.coreandroidapp.R
import kotlinx.coroutines.*

suspend fun clearImageCache(applicationContext: Context) = coroutineScope {
    Glide.get(applicationContext).clearDiskCache()
}

fun ImageView.loadImage(url: String) {
    Glide.with(this.context)
        .load(url)
        .placeholder(R.drawable.ic_baseline_refresh_24)
        .error(R.drawable.ic_baseline_broken_image_24)
        .into(this)
}

fun ImageView.loadImage(@DrawableRes resourceId: Int) {
    Glide.with(this.context)
        .load(resourceId)
        .placeholder(R.drawable.ic_baseline_refresh_24)
        .error(R.drawable.ic_baseline_broken_image_24)
        .into(this)
}