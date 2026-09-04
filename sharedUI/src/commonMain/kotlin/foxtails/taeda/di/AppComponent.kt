package foxtails.taeda.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import co.touchlab.kermit.Logger
import coil3.ImageLoader
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import foxtails.taeda.domain.model.SavedSearches
import foxtails.taeda.domain.model.SessionStorage
import foxtails.taeda.domain.repository.pixelfed.PixelfedApi
import foxtails.taeda.domain.repository.pixelfed.createPixelfedApi
import foxtails.taeda.domain.repository.serializers.SavedSearchesSerializer
import foxtails.taeda.domain.repository.serializers.SessionStorageSerializer
import foxtails.taeda.domain.repository.sharkey.SharkeyApi
import foxtails.taeda.domain.repository.sharkey.createSharkeyApi
import foxtails.taeda.domain.service.file.FileService
import foxtails.taeda.domain.service.general.AccountService
import foxtails.taeda.domain.service.general.AccountServiceDelegate
import foxtails.taeda.domain.service.general.AppIconManager
import foxtails.taeda.domain.service.general.AppIconService
import foxtails.taeda.domain.service.general.AppIconServiceDelegate
import foxtails.taeda.domain.service.general.AuthService
import foxtails.taeda.domain.service.general.AuthServiceDelegate
import foxtails.taeda.domain.service.general.CollectionService
import foxtails.taeda.domain.service.general.CollectionServiceDelegate
import foxtails.taeda.domain.service.general.DirectMessagesService
import foxtails.taeda.domain.service.general.DirectMessagesServiceDelegate
import foxtails.taeda.domain.service.general.ExploreService
import foxtails.taeda.domain.service.general.ExploreServiceDelegate
import foxtails.taeda.domain.service.general.InstanceService
import foxtails.taeda.domain.service.general.InstanceServiceDelegate
import foxtails.taeda.domain.service.general.NotificationService
import foxtails.taeda.domain.service.general.NotificationServiceDelegate
import foxtails.taeda.domain.service.general.PostEditorService
import foxtails.taeda.domain.service.general.PostEditorServiceDelegate
import foxtails.taeda.domain.service.general.PostService
import foxtails.taeda.domain.service.general.PostServiceDelegate
import foxtails.taeda.domain.service.general.Session
import foxtails.taeda.domain.service.general.TimelineService
import foxtails.taeda.domain.service.general.TimelineServiceDelegate
import foxtails.taeda.domain.service.general.WidgetService
import foxtails.taeda.domain.service.general.WidgetServiceDelegate
import foxtails.taeda.domain.service.pixelfed.PixelfedAuthInterceptor
import foxtails.taeda.domain.service.preferences.UserPreferences
import foxtails.taeda.domain.service.sharkey.SharkeyAuthInterceptor
import foxtails.taeda.ui.events.AccountIntentHandler
import foxtails.taeda.ui.events.BackToTopTrigger
import foxtails.taeda.ui.events.GlobalNavigator
import foxtails.taeda.ui.events.GlobalNavigatorImpl
import foxtails.taeda.ui.events.NotificationBadgeRefresher
import foxtails.taeda.ui.events.NotificationBadgeState
import foxtails.taeda.ui.events.SearchFieldFocus
import foxtails.taeda.ui.events.SystemFileShare
import foxtails.taeda.ui.events.SystemUrlHandler
import foxtails.taeda.utils.KmpContext
import foxtails.taeda.utils.coilContext
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.CallConverterFactory
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.http.Url
import io.ktor.http.set
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.KmpComponentCreate
import me.tatarka.inject.annotations.Provides
import me.tatarka.inject.annotations.Qualifier
import me.tatarka.inject.annotations.Scope

@Scope
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER)
annotation class AppSingleton

expect fun ImageLoader.Builder.addPlatformImageDecoders(): ImageLoader.Builder

@AppSingleton
@Component
abstract class AppComponent(
    @get:Provides val context: KmpContext,
    @get:Provides val iconManager: AppIconManager,
) {
    abstract val systemUrlHandler: SystemUrlHandler
    abstract val systemFileShare: SystemFileShare

    abstract val accountIntentHandler: AccountIntentHandler

    abstract val authService: AuthService
    abstract val accountService: AccountService
    abstract val widgetService: WidgetService
    abstract val notificationBadgeRefresher: NotificationBadgeRefresher

    abstract val preferences: UserPreferences
    abstract val searchFieldFocus: SearchFieldFocus
    abstract val backToTopTrigger: BackToTopTrigger
    abstract val globalNavigator: GlobalNavigator
    abstract val notificationBadgeState: NotificationBadgeState

    @Provides
    fun bindGlobalNavigator(impl: GlobalNavigatorImpl): GlobalNavigator = impl

    @Provides
    fun provideTimelineService(delegate: TimelineServiceDelegate): TimelineService = delegate

    @Provides
    fun provideExploreService(delegate: ExploreServiceDelegate): ExploreService = delegate

    @Provides
    fun provideAppIconService(delegate: AppIconServiceDelegate): AppIconService = delegate

    @Provides
    fun providePostEditorService(delegate: PostEditorServiceDelegate): PostEditorService = delegate

    @Provides
    fun providePostService(delegate: PostServiceDelegate): PostService = delegate

    @Provides
    fun provideWidgetService(delegate: WidgetServiceDelegate): WidgetService = delegate
    @Provides
    fun provideNotificationService(delegate: NotificationServiceDelegate): NotificationService = delegate
    @Provides
    fun provideInstanceService(delegate: InstanceServiceDelegate): InstanceService = delegate

    @Provides
    fun provideAuthService(delegate: AuthServiceDelegate): AuthService = delegate

    @Provides
    fun provideAccountService(delegate: AccountServiceDelegate): AccountService = delegate

    @Provides
    fun provideCollectionService(delegate: CollectionServiceDelegate): CollectionService = delegate
    @Provides
    fun provideDirectMessagesService(delegate: DirectMessagesServiceDelegate): DirectMessagesService = delegate

    @get:Provides
    @get:AppSingleton
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
        encodeDefaults = true
        coerceInputValues = true
    }

    @Qualifier
    @Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
    annotation class PixelfedClient

    @Qualifier
    @Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
    annotation class SharkeyClient


    @Qualifier
    @Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
    annotation class SimpleClient

    @Provides
    @AppSingleton
    @PixelfedClient
    fun providePixelfedHttpClient(
        json: Json,
        session: Session,
        sessionStorage: DataStore<SessionStorage>,
        globalNavigator: GlobalNavigator
    ): HttpClient {
        val authInterceptor = PixelfedAuthInterceptor(session)

        return HttpClient {

            expectSuccess = true
            install(ContentNegotiation) { json(json) }
            install(Logging) {
                logger = object : io.ktor.client.plugins.logging.Logger {
                    override fun log(message: String) {
                        val formattedMessage = message.lines().joinToString(separator = "\n") { "\t\t$it" }
                        Logger.v (tag = "PLACEHOLDERHttp") {
                            formattedMessage
                        }
                    }
                }
                level = LogLevel.NONE
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 60000
                socketTimeoutMillis = 60000
                connectTimeoutMillis = 60000
            }
        }.apply {
            plugin(HttpSend).intercept { request ->
                // Apply the correct server URL from session credentials without executing.
                // Previously Session.intercept() called execute() here, which caused every
                // request to be sent twice: once without the Bearer token (triggering a 401
                // from the server) and once with it. For multipart uploads the first execute
                // consumes the request body, so the retried authenticated request fails too.
                session.credentials.value?.let { creds ->
                    if (request.url.host != "api.fedisea.surf" && request.url.host != "pixelfed.org") {
                        request.url.set(host = Url(creds.serverUrl).host)
                    }
                }
                with(authInterceptor) { intercept(request) }
            }
        }
    }

    @Provides
    @AppSingleton
    fun providePixelfedApi(@PixelfedClient client: HttpClient): PixelfedApi =
        Ktorfit.Builder()
            .converterFactories(CallConverterFactory())
            .httpClient(client)
            .baseUrl("https://err.or/")
            .build()
            .createPixelfedApi()

    @Provides
    @AppSingleton
    @SharkeyClient
    fun provideSharkeyHttpClient(json: Json, session: Session): HttpClient {
        val authInterceptor = SharkeyAuthInterceptor(session)
        return HttpClient {
            expectSuccess = true
            install(ContentNegotiation) { json(json) }
            install(HttpTimeout) {
                requestTimeoutMillis = 60000
                socketTimeoutMillis = 60000
                connectTimeoutMillis = 60000
            }
        }.apply {
            plugin(HttpSend).intercept { request ->
                with(authInterceptor) { intercept(request) }
            }
        }
    }

    @Provides
    @AppSingleton
    fun provideSharkeyApi(@SharkeyClient client: HttpClient): SharkeyApi =
        Ktorfit.Builder()
            .converterFactories(CallConverterFactory())
            .httpClient(client)
            .baseUrl("https://err.or/")
            .build()
            .createSharkeyApi()


    @Provides
    @AppSingleton
    @SimpleClient
    fun provideSimpleHttpClient(): HttpClient {
        return HttpClient {
            expectSuccess = true
        }
    }


    @Provides
    @AppSingleton
    fun providePreferences(): DataStore<Preferences> =
        FileService.createPreferences("settings.preferences_pb")

    @Provides
    @AppSingleton
    fun provideSavedSearchesDataStore(): DataStore<SavedSearches> =
        FileService.createDataStore("saved_searches_2.json", SavedSearchesSerializer)

    @Provides
    @AppSingleton
    fun provideSessionStorageDataStore(): DataStore<SessionStorage> =
        FileService.createDataStore("session_storage_datastore.json", SessionStorageSerializer)

    @Provides
    @AppSingleton
    fun provideImageLoader(): ImageLoader =
        ImageLoader.Builder(context.coilContext)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache(
                MemoryCache.Builder()
                    .maxSizePercent(context.coilContext, 0.2)
                    .build()
            )
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache(FileService.createDiskCache())
            .addPlatformImageDecoders()
            .build()

    companion object
}

@KmpComponentCreate
expect fun AppComponent.Companion.create(
    context: KmpContext,
    iconManager: AppIconManager,
): AppComponent