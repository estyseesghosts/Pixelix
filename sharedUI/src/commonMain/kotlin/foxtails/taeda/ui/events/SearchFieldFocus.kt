package foxtails.taeda.ui.events

import foxtails.taeda.di.AppSingleton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@AppSingleton
@Inject
class SearchFieldFocus {
    private val eventsFlow = MutableSharedFlow<Boolean>()
    val events = eventsFlow.asSharedFlow()

    fun focus() {
        GlobalScope.launch { eventsFlow.emit(true) }
    }
}