package foxtails.taeda.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import foxtails.taeda.ui.composables.HomeViewModel
import foxtails.taeda.ui.composables.collection.CollectionViewModel
import foxtails.taeda.ui.composables.custom_account.CustomAccountViewModel
import foxtails.taeda.ui.composables.direct_messages.chat.ChatViewModel
import foxtails.taeda.ui.composables.direct_messages.conversations.ConversationsViewModel
import foxtails.taeda.ui.composables.edit_profile.EditProfileViewModel
import foxtails.taeda.ui.composables.explore.ExploreViewModel
import foxtails.taeda.ui.composables.explore.trending.cameras.CamerasViewModel
import foxtails.taeda.ui.composables.explore.trending.categories.CategoriesViewModel
import foxtails.taeda.ui.composables.explore.trending.editors_choice_accounts.EditorsChoiceAccountsViewModel
import foxtails.taeda.ui.composables.explore.trending.films.FilmsViewModel
import foxtails.taeda.ui.composables.explore.trending.lenses.LensesViewModel
import foxtails.taeda.ui.composables.explore.trending.trending_accounts.TrendingAccountElementViewModel
import foxtails.taeda.ui.composables.explore.trending.trending_accounts.TrendingAccountsViewModel
import foxtails.taeda.ui.composables.explore.trending.trending_hashtags.TrendingHashtagElementViewModel
import foxtails.taeda.ui.composables.explore.trending.trending_hashtags.TrendingHashtagsViewModel
import foxtails.taeda.ui.composables.explore.trending.trending_posts.TrendingPostsViewModel
import foxtails.taeda.ui.composables.followers.FollowersViewModel
import foxtails.taeda.ui.composables.hashtagMentionText.TextWithClickableHashtagsAndMentionsViewModel
import foxtails.taeda.ui.composables.mention.MentionViewModel
import foxtails.taeda.ui.composables.notifications.CustomNotificationViewModel
import foxtails.taeda.ui.composables.notifications.NotificationsViewModel
import foxtails.taeda.ui.composables.post.PostViewModel
import foxtails.taeda.ui.composables.post_editor.PostEditorViewModel
import foxtails.taeda.ui.composables.profile.other_profile.OtherProfileViewModel
import foxtails.taeda.ui.composables.profile.own_profile.AccountSwitchViewModel
import foxtails.taeda.ui.composables.profile.own_profile.OwnProfileViewModel
import foxtails.taeda.ui.composables.profile.server_stats.ServerStatsViewModel
import foxtails.taeda.ui.composables.session.LoginViewModel
import foxtails.taeda.ui.composables.settings.about_instance.AboutInstanceViewModel
import foxtails.taeda.ui.composables.settings.about_pixelix.AboutPlaceholderViewModel
import foxtails.taeda.ui.composables.settings.blocked_accounts.BlockedAccountsViewModel
import foxtails.taeda.ui.composables.settings.followed_hashtags.FollowedHashtagsViewModel
import foxtails.taeda.ui.composables.settings.icon_selection.IconSelectionViewModel
import foxtails.taeda.ui.composables.settings.muted_accounts.MutedAccountsViewModel
import foxtails.taeda.ui.composables.settings.preferences.prefs.PreferencesViewModel
import foxtails.taeda.ui.composables.settings.preferences.prefs.prefs.ClearCacheViewModel
import foxtails.taeda.ui.composables.settings.preferences.prefs.prefs.DefaultLicenseViewModel
import foxtails.taeda.ui.composables.single_post.SinglePostViewModel
import foxtails.taeda.ui.composables.textfield_location.TextFieldLocationsViewModel
import foxtails.taeda.ui.composables.timelines.parametric_timeline_screens.ParametricTimelineViewModel
import foxtails.taeda.ui.composables.timelines.global_timeline.GlobalTimelineViewModel
import foxtails.taeda.ui.composables.timelines.hashtag_timeline.HashtagTimelineViewModel
import foxtails.taeda.ui.composables.timelines.home_timeline.HomeTimelineViewModel
import foxtails.taeda.ui.composables.timelines.local_timeline.LocalTimelineViewModel
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.KmpComponentCreate

@Component
abstract class ViewModelComponent(
    @Component val appComponent: AppComponent
) {
    abstract val loginViewModel: LoginViewModel
    abstract val collectionViewModel: CollectionViewModel
    abstract val customAccountViewModel: CustomAccountViewModel
    abstract val chatViewModel: ChatViewModel
    abstract val aboutInstanceViewModel: AboutInstanceViewModel
    abstract val aboutPixelixViewModel: AboutPlaceholderViewModel
    abstract val accountSwitchViewModel: AccountSwitchViewModel
    abstract val blockedAccountsViewModel: BlockedAccountsViewModel
    abstract val customNotificationViewModel: CustomNotificationViewModel
    abstract val editProfileViewModel: EditProfileViewModel
    abstract val exploreViewModel: ExploreViewModel
    abstract val followedHashtagsViewModel: FollowedHashtagsViewModel
    abstract val followersViewModel: FollowersViewModel
    abstract val globalTimelineViewModel: GlobalTimelineViewModel
    abstract val hashtagTimelineViewModel: HashtagTimelineViewModel
    abstract val parametricTimelineViewModel: ParametricTimelineViewModel
    abstract val homeTimelineViewModel: HomeTimelineViewModel
    abstract val iconSelectionViewModel: IconSelectionViewModel
    abstract val localTimelineViewModel: LocalTimelineViewModel
    abstract val mentionViewModel: MentionViewModel
    abstract val mutedAccountsViewModel: MutedAccountsViewModel
    abstract val newPostViewModel: PostEditorViewModel
    abstract val notificationsViewModel: NotificationsViewModel
    abstract val otherProfileViewModel: OtherProfileViewModel
    abstract val ownProfileViewModel: OwnProfileViewModel
    abstract val postViewModel: PostViewModel
    abstract val preferencesViewModel: PreferencesViewModel
    abstract val serverStatsViewModel: ServerStatsViewModel
    abstract val singlePostViewModel: SinglePostViewModel
    abstract val textWithClickableHashtagsAndMentionsViewModel: TextWithClickableHashtagsAndMentionsViewModel
    abstract val trendingAccountElementViewModel: TrendingAccountElementViewModel
    abstract val trendingAccountsViewModel: TrendingAccountsViewModel
    abstract val trendingHashtagElementViewModel: TrendingHashtagElementViewModel
    abstract val trendingHashtagsViewModel: TrendingHashtagsViewModel
    abstract val editorsChoiceAccountsViewModel: EditorsChoiceAccountsViewModel
    abstract val camerasViewModel: CamerasViewModel
    abstract val trendingPostsViewModel: TrendingPostsViewModel
    abstract val categoriesViewModel: CategoriesViewModel
    abstract val lensesViewModel: LensesViewModel
    abstract val filmsViewModel: FilmsViewModel
    abstract val conversationsViewModel: ConversationsViewModel
    abstract val textFieldLocationsViewModel: TextFieldLocationsViewModel
    abstract val clearCacheViewModel: ClearCacheViewModel
    abstract val defaultLicenseViewModel: DefaultLicenseViewModel
    abstract val homeViewModel: HomeViewModel

    companion object
}

@KmpComponentCreate
expect fun ViewModelComponent.Companion.create(app: AppComponent): ViewModelComponent

val LocalAppComponent = staticCompositionLocalOf<AppComponent> { error("no AppComponent") }

@Composable
inline fun <reified VM: ViewModel> injectViewModel(key: String, crossinline factory: ViewModelComponent.() -> VM): VM {
    val app = LocalAppComponent.current
    return viewModel(key = key) { ViewModelComponent.create(app).factory() }
}