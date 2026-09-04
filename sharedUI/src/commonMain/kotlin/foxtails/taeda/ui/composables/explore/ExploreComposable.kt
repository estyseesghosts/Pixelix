package foxtails.taeda.ui.composables.explore

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import foxtails.taeda.di.LocalAppComponent
import foxtails.taeda.di.injectViewModel
import foxtails.taeda.domain.model.Account
import foxtails.taeda.domain.model.SavedSearchItem
import foxtails.taeda.domain.model.SavedSearchType
import foxtails.taeda.domain.model.toDomain
import foxtails.taeda.ui.composables.custom_account.AccountListItem
import foxtails.taeda.ui.composables.widgets.CustomHashtag
import foxtails.taeda.ui.composables.widgets.CustomPost
import foxtails.taeda.ui.composables.custom_account.CustomAccount
import foxtails.taeda.ui.composables.explore.trending.TrendingComposable
import foxtails.taeda.ui.composables.states.ErrorComposable
import foxtails.taeda.ui.composables.states.LoadingComposable
import foxtails.taeda.ui.navigation.Destination
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import foxtails.taeda.app.generated.resources.Res
import foxtails.taeda.app.generated.resources.accounts
import foxtails.taeda.app.generated.resources.chevron_left
import foxtails.taeda.app.generated.resources.close
import foxtails.taeda.app.generated.resources.default_avatar
import foxtails.taeda.app.generated.resources.explore
import foxtails.taeda.app.generated.resources.hash
import foxtails.taeda.app.generated.resources.hashtags
import foxtails.taeda.app.generated.resources.posts
import foxtails.taeda.app.generated.resources.search
import foxtails.taeda.app.generated.resources.trash

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreComposable(
    navController: NavController,
    initialPage: Int = 0,
    viewModel: ExploreViewModel = injectViewModel(key = "search-viewmodel-key") { exploreViewModel }
) {
    val focusRequester = remember { FocusRequester() }
    val textFieldState = rememberTextFieldState()
    var expanded by rememberSaveable { mutableStateOf(false) }

    val appComponent = LocalAppComponent.current
    LaunchedEffect(Unit) {
        appComponent.searchFieldFocus.events.collect {
            focusRequester.requestFocus()
        }
    }

    Box(
        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainer)
            .semantics { isTraversalGroup = true }) {
        SearchBar(
            modifier = Modifier.align(Alignment.TopCenter).semantics { traversalIndex = 0f },
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            inputField = {
                SearchBarDefaults.InputField(
                    query = textFieldState.text.toString(),
                    onQueryChange = { textFieldState.setTextAndPlaceCursorAtEnd(it) },
                    onSearch = {
                        expanded = false
                        viewModel.onSearch(it)
                        viewModel.saveSearch(it)
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text(stringResource(Res.string.explore)) },
                    modifier = Modifier.focusRequester(focusRequester),
                    leadingIcon = {
                        if (!expanded) {
                            Icon(vectorResource(Res.drawable.search), contentDescription = null)
                        } else {
                            Icon(
                                vectorResource(Res.drawable.chevron_left),
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    expanded = false
                                    textFieldState.clearText()
                                    viewModel.searchState = SearchState()
                                })
                        }
                    },
                    trailingIcon = {
                        AnimatedVisibility(
                            visible = textFieldState.text.isNotBlank(),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Icon(
                                vectorResource(Res.drawable.close),
                                contentDescription = "clear search query",
                                modifier = Modifier.clickable {
                                    textFieldState.clearText()
                                    viewModel.searchState = SearchState()
                                })
                        }
                    })
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {

            LaunchedEffect(textFieldState.text) {
                viewModel.textInputChange(textFieldState.text.toString())
            }

            if (textFieldState.text.isBlank() && viewModel.savedSearches.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.imePadding(),
                    contentPadding = PaddingValues(bottom = 60.dp),
                ) {
                    items(viewModel.savedSearches.reversed()) {
                        if (it.savedSearchType == SavedSearchType.Account) {
                            Row {
                                CustomAccount(
                                    account = it.account!!.toDomain(),
                                    relationship = null,
                                    navController = navController,
                                    removeSavedSearch = { viewModel.deleteSavedSearch(it) })
                            }
                        } else {
                            PastSearchItem(item = it, navController, { text ->
                                expanded = false
                                textFieldState.setTextAndPlaceCursorAtEnd(text)
                                viewModel.onSearch(text)
                            }, { viewModel.deleteSavedSearch(it) })
                        }
                    }
                }
            }
        }
        Box(
            Modifier.windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
                .semantics { traversalIndex = 1f }.padding(top = 80.dp),
        ) {
            if (textFieldState.text.isBlank()) {
                TrendingComposable(
                    navController,
                    viewModel = viewModel,
                    initialPage = initialPage,
                    isSwipeEnabled = viewModel.isSwipeEnabled
                )
            } else {
                when {
                    viewModel.searchState.isLoading -> LoadingComposable()
                    viewModel.searchState.error.isNotBlank() -> ErrorComposable(viewModel.searchState.error)
                    viewModel.searchState.searchResult != null -> SearchResultComposable(
                        searchState = viewModel.searchState,
                        saveAccount = { username, account -> viewModel.saveAccount(username, account) },
                        saveHashtag = { hashtag -> viewModel.saveHashtag(hashtag) },
                        navController = navController
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchResultComposable(
    searchState: SearchState,
    saveAccount: (String, Account) -> Unit,
    saveHashtag: (String) -> Unit,
    navController: NavController
) {
    val result = searchState.searchResult ?: return
    val categories = buildList {
        if (result.accounts.isNotEmpty()) add(SearchCategory.Accounts)
        if (result.posts.isNotEmpty()) add(SearchCategory.Posts)
        if (result.tags.isNotEmpty()) add(SearchCategory.Hashtags)
    }
    if (categories.isEmpty()) return

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { categories.size })
    val scope = rememberCoroutineScope()
    LaunchedEffect(categories) {
        if (pagerState.currentPage >= categories.size) {
            pagerState.scrollToPage(categories.lastIndex)
        }
    }
    val selectedPage = pagerState.currentPage.coerceIn(0, categories.lastIndex)
    Column {
        PrimaryTabRow(selectedTabIndex = selectedPage) {
            categories.forEachIndexed { index, category ->
                Tab(
                    text = {
                        Text(
                            stringResource(
                                when (category) {
                                    SearchCategory.Accounts -> Res.string.accounts
                                    SearchCategory.Posts -> Res.string.posts
                                    SearchCategory.Hashtags -> Res.string.hashtags
                                }
                            )
                        )
                    },
                    selected = selectedPage == index,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 2,
            modifier = Modifier.weight(1f).background(MaterialTheme.colorScheme.background)
        ) { tabIndex ->
            when (categories[tabIndex]) {
                SearchCategory.Accounts -> Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(contentPadding = PaddingValues(8.dp), content = {
                        itemsIndexed(result.accounts) { index, account ->
                            AccountListItem(
                                account = account,
                                relationship = null,
                                navController = navController,
                                index = index,
                                count = result.accounts.size,
                                onClick = { saveAccount(account.username, account) })
                        }
                    })
                }

                SearchCategory.Posts -> Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(contentPadding = PaddingValues(8.dp)) {
                        items(result.posts) { post ->
                            CustomPost(
                                post = post,
                                navController = navController,
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                roundedCornerShape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }

                SearchCategory.Hashtags -> Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(content = {
                        items(result.tags) {
                            CustomHashtag(
                                hashtag = it,
                                onClick = { saveHashtag(it.name) },
                                navController = navController
                            )
                        }
                    })
                }
            }
        }
    }
}

private enum class SearchCategory {
    Accounts,
    Posts,
    Hashtags
}

@Composable
private fun PastSearchItem(
    item: SavedSearchItem,
    navController: NavController,
    setSearchText: (text: String) -> Unit,
    deleteSavedSearch: () -> Unit
) {
    Row(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp).fillMaxWidth().clickable {
            when (item.savedSearchType) {
                SavedSearchType.Account -> navController.navigate(
                    Destination.Profile(
                        item.account?.id, item.account?.username
                    )
                )

                SavedSearchType.Hashtag -> navController.navigate(
                    Destination.HashtagTimeline(
                        item.value
                    )
                )

                SavedSearchType.Search -> setSearchText(item.value)
            }
        }, verticalAlignment = Alignment.CenterVertically
    ) {
        if (item.savedSearchType == SavedSearchType.Account) {
            AsyncImage(
                model = item.account!!.avatar,
                error = painterResource(Res.drawable.default_avatar),
                contentDescription = "",
                modifier = Modifier.height(46.dp).width(46.dp).clip(CircleShape)
            )
        } else {
            Box(
                modifier = Modifier.height(46.dp).width(46.dp)
                    .background(MaterialTheme.colorScheme.surfaceBright, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (item.savedSearchType == SavedSearchType.Hashtag) {
                        vectorResource(Res.drawable.hash)
                    } else {
                        vectorResource(Res.drawable.search)
                    }, contentDescription = null, tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))

        val text = when (item.savedSearchType) {
            SavedSearchType.Account -> {
                "@${item.value}"
            }

            SavedSearchType.Hashtag -> {
                "#${item.value}"
            }

            else -> {
                item.value
            }
        }
        Text(text = text, modifier = Modifier.weight(1f), softWrap = true)
        Box(
            modifier = Modifier.height(22.dp).width(22.dp).clickable { deleteSavedSearch() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.close),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}