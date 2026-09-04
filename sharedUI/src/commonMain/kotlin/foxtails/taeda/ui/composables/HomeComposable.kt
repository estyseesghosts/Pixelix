package foxtails.taeda.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import foxtails.taeda.di.LocalAppComponent
import foxtails.taeda.di.injectViewModel
import foxtails.taeda.ui.composables.contribute.ContributeBottomSheet
import foxtails.taeda.ui.composables.timelines.home_timeline.HomeTimelineComposable
import foxtails.taeda.ui.composables.timelines.local_timeline.LocalTimelineComposable
import foxtails.taeda.ui.navigation.Destination
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import foxtails.taeda.app.generated.resources.Res
import foxtails.taeda.app.generated.resources.add_circle
import foxtails.taeda.app.generated.resources.app_name
import foxtails.taeda.app.generated.resources.coffee
import foxtails.taeda.app.generated.resources.home
import foxtails.taeda.app.generated.resources.home_timeline_explained
import foxtails.taeda.app.generated.resources.local
import foxtails.taeda.app.generated.resources.local_timeline_explained
import foxtails.taeda.app.generated.resources.mail
import foxtails.taeda.app.generated.resources.settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeComposable(
    navController: NavController,
    openPreferencesDrawer: () -> Unit,
    viewModel: HomeViewModel = injectViewModel("homeViewModel") { homeViewModel },
) {
    val pagerState = rememberPagerState(viewModel.defaultTab) { 2 }
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }

    val donationSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showDonationBottomSheet by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val appComponent = LocalAppComponent.current

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Top),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior, title = {
                    Text(
                        stringResource(Res.string.app_name),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }, actions = {
                    Row {
                        IconButton(onClick = {
                            showDonationBottomSheet = true
                        }) {
                            Icon(
                                imageVector = vectorResource(Res.drawable.coffee),
                                contentDescription = "Coffee"
                            )
                        }

                        IconButton(onClick = {
                            navController.navigate(Destination.HomeTabNewPost) {
                                launchSingleTop = true
                                restoreState = false
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = false
                                    saveState = true
                                }
                            }
                        }) {
                            Icon(
                                imageVector = vectorResource(Res.drawable.add_circle),
                                contentDescription = "New Post"
                            )
                        }

                        if (viewModel.capabilities.value.general.supportsDMs) {
                            IconButton(onClick = {
                                navController.navigate(Destination.Conversations)
                            }) {
                                Icon(
                                    imageVector = vectorResource(Res.drawable.mail),
                                    contentDescription = "Conversations"
                                )
                            }
                        }


                        IconButton(onClick = {
                            openPreferencesDrawer()
                        }) {
                            Icon(
                                imageVector = vectorResource(Res.drawable.settings),
                                contentDescription = "Settings"
                            )
                        }
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }) { paddingValues ->
        Box(
            Modifier.fillMaxSize().padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            PrimaryTabRow(
                selectedTabIndex = pagerState.currentPage,
                divider = {},
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier.clip(
                    RoundedCornerShape(
                        bottomStart = 24.dp, bottomEnd = 24.dp
                    )
                ).zIndex(1f)
            ) {
                Tab(
                    text = { Text(stringResource(Res.string.home)) },
                    selected = pagerState.currentPage == 0,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                    onClick = {
                        if (pagerState.currentPage == 0) {
                            appComponent.backToTopTrigger.scrollToTop()
                        } else {
                            scope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                        }
                    })

                Tab(
                    text = { Text(stringResource(Res.string.local)) },
                    selected = pagerState.currentPage == 1,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                    onClick = {
                        scope.launch {
                            if (pagerState.currentPage == 1) {
                                appComponent.backToTopTrigger.scrollToTop()
                            } else {
                                pagerState.animateScrollToPage(1)
                            }
                        }
                    })

            }

            HorizontalPager(
                state = pagerState,
                beyondViewportPageCount = 3,
                userScrollEnabled = viewModel.isSwipeBetweenTabsEnabled,
                modifier = Modifier.padding(top = 24.dp)
                    .background(MaterialTheme.colorScheme.background).zIndex(0f)
            ) { tabIndex ->
                when (tabIndex) {
                    0 -> Box(modifier = Modifier.fillMaxSize()) {
                        HomeTimelineComposable(pagerState, tabIndex, navController)
                    }

                    1 -> Box(modifier = Modifier.fillMaxSize()) {
                        LocalTimelineComposable(pagerState, tabIndex, navController)
                    }

                }
            }
        }
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            }, sheetState = sheetState
        ) {
            Box(
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Column {
                    Spacer(modifier = Modifier.height(18.dp))

                    SheetItem(
                        header = stringResource(Res.string.home),
                        description = stringResource(Res.string.home_timeline_explained)
                    )

                    SheetItem(
                        header = stringResource(Res.string.local),
                        description = stringResource(Res.string.local_timeline_explained)
                    )

                }
            }
        }
    }

    if (showDonationBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showDonationBottomSheet = false
            }, sheetState = donationSheetState
        ) {
            ContributeBottomSheet { url -> viewModel.openUrl(url) }
        }
    }
}


@Composable
fun SheetItem(header: String, description: String) {
    Column {
        Text(text = header, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = description)
        Spacer(modifier = Modifier.height(16.dp))
    }
}