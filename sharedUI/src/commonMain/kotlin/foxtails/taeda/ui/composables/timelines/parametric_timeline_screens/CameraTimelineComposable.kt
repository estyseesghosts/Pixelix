package foxtails.taeda.ui.composables.timelines.parametric_timeline_screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import foxtails.taeda.di.injectViewModel

@Composable
fun CameraTimelineComposable(
    navController: NavController,
    camera: String,
    viewModel: ParametricTimelineViewModel = injectViewModel(key = "camera-$camera") {
        parametricTimelineViewModel.apply { init(ParametricTimelineViewModel.FetchType.CAMERA, camera) }
    }
) {
    TimelineScreen(
        title = camera,
        subtitle = "Camera",
        navController = navController,
        viewModel = viewModel,
        isFirstItemLarge = true
    )
}