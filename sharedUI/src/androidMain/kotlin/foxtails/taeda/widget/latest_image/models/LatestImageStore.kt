package foxtails.taeda.widget.notifications.models

import coil3.Bitmap
import foxtails.taeda.widget.BitmapSerializer
import kotlinx.serialization.Serializable

@Serializable
data class LatestImageStore(
    @Serializable(with = BitmapSerializer::class) // <--- Add this
    val latestImageBitmap: Bitmap? = null,
    val postId: String = "",
    val refreshing: Boolean = false,
    val error: String = ""
)