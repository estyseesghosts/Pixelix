package foxtails.taeda

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.bindToBrowserNavigation
import coil3.SingletonImageLoader
import foxtails.taeda.di.AppComponent
import foxtails.taeda.di.create
import foxtails.taeda.domain.service.icon.WebAppIconManager
import foxtails.taeda.utils.KmpContext
import foxtails.taeda.utils.configureLogger

@OptIn(ExperimentalComposeUiApi::class, ExperimentalBrowserHistoryApi::class)
fun webApp() {
    val appComponent = AppComponent.create(
        object : KmpContext() {},
        WebAppIconManager()
    )

    configureLogger(false)

    SingletonImageLoader.setSafe {
        appComponent.provideImageLoader()
    }

    setOAuthRedirectCallback {
        appComponent.systemUrlHandler.onRedirect(it)
    }

    ComposeViewport {
        App(
            appComponent = appComponent,
            onNavHostReady = { navController ->
                // `App` recreates the NavController via `key(activeUser)` on every login/logout.
                // `bindToBrowserNavigation` restores the current URL fragment onto the controller it
                // binds, so a freshly-created controller would immediately navigate back to the
                // previous session's route (e.g. bounce to FirstLogin right after a successful
                // login), overriding the new NavHost's startDestination. Drop the stale fragment
                // first so the new controller honors its startDestination.
                // Trade-off: this also clears a deep-link fragment on cold start; acceptable until
                // deep linking is wired up on web.
                clearBrowserRoute()
                navController.bindToBrowserNavigation()
            },
            exitApp = { /* no-op: the browser tab has no explicit exit */ }
        )
    }
}

/** Removes the `#route` fragment from the address bar without adding a history entry. */
private fun clearBrowserRoute(): Unit =
    js("{ window.history.replaceState(null, '', window.location.pathname + window.location.search); }")

private fun setOAuthRedirectCallback(cb: (String) -> Unit): Unit =
    js("{ window.foxtailsTaedaOAuthCallback = cb; }")
