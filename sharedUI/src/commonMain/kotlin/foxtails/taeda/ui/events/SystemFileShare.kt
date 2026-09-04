package foxtails.taeda.ui.events

import foxtails.taeda.di.AppSingleton
import foxtails.taeda.utils.KmpUri
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
@AppSingleton
class SystemFileShare {
    private val shareFilesRequestQueue = Channel<List<KmpUri>>(
        capacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val shareFilesRequests get() = shareFilesRequestQueue.receiveAsFlow()

    @OptIn(DelicateCoroutinesApi::class)
    fun share(uris: List<KmpUri>) {
        GlobalScope.launch {
            shareFilesRequestQueue.send(uris)
        }
    }
}