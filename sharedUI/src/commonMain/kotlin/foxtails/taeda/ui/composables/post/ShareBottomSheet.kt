package foxtails.taeda.ui.composables.post

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import foxtails.taeda.LocalSnackbarPresenter
import foxtails.taeda.domain.model.MediaAttachment
import foxtails.taeda.domain.model.Post
import foxtails.taeda.domain.model.Visibility
import foxtails.taeda.domain.service.platform.PlatformFeatures
import foxtails.taeda.ui.composables.profile.other_profile.BlockAccountAlert
import foxtails.taeda.ui.composables.settings.muted_accounts.MuteAccountAlert
import foxtails.taeda.ui.composables.widgets.ButtonRowElement
import foxtails.taeda.ui.navigation.Destination
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import foxtails.taeda.app.generated.resources.Res
import foxtails.taeda.app.generated.resources.audience_public
import foxtails.taeda.app.generated.resources.block_this_profile
import foxtails.taeda.app.generated.resources.blocked
import foxtails.taeda.app.generated.resources.delete_this_post
import foxtails.taeda.app.generated.resources.download
import foxtails.taeda.app.generated.resources.download_image
import foxtails.taeda.app.generated.resources.edit
import foxtails.taeda.app.generated.resources.edit_post
import foxtails.taeda.app.generated.resources.eye
import foxtails.taeda.app.generated.resources.followers_only
import foxtails.taeda.app.generated.resources.mute_this_profile
import foxtails.taeda.app.generated.resources.muted
import foxtails.taeda.app.generated.resources.open
import foxtails.taeda.app.generated.resources.open_in_browser
import foxtails.taeda.app.generated.resources.report_this_post
import foxtails.taeda.app.generated.resources.share
import foxtails.taeda.app.generated.resources.share_this_post
import foxtails.taeda.app.generated.resources.trash
import foxtails.taeda.app.generated.resources.unlisted
import foxtails.taeda.app.generated.resources.visibility_x
import foxtails.taeda.app.generated.resources.warning

@Composable
fun ShareBottomSheet(
    url: String,
    minePost: Boolean,
    viewModel: PostViewModel,
    post: Post,
    currentMediaAttachmentNumber: Int,
    navController: NavController,
    closeBottomSheet: () -> Unit
) {

    var humanReadableVisibility by remember {
        mutableStateOf("")
    }

    var isReportDialogOpen by remember { mutableStateOf(false) }
    var showMuteAlert by remember { mutableStateOf(false) }
    var showBlockAlert by remember { mutableStateOf(false) }

    val mediaAttachment: MediaAttachment? = viewModel.post?.mediaAttachments?.let { attachments ->
        if (attachments.isNotEmpty() && currentMediaAttachmentNumber in attachments.indices) {
            attachments[currentMediaAttachmentNumber]
        } else {
            null
        }
    }

    LaunchedEffect(Unit) {
        humanReadableVisibility = when (post.visibility) {
            Visibility.PUBLIC -> getString(Res.string.audience_public)
            Visibility.UNLISTED -> getString(Res.string.unlisted)
            Visibility.PRIVATE -> getString(Res.string.followers_only)
            else -> ""
        }
    }


    Column(
        modifier = Modifier.padding(bottom = 32.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.eye),
                contentDescription = "",
                Modifier.padding(start = 18.dp, top = 12.dp, bottom = 12.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(text = stringResource(Res.string.visibility_x, humanReadableVisibility))
        }

//        if (mediaAttachment?.license != null) {
//            ButtonRowElement(
//                icon = Res.drawable.document_text, text = stringResource(
//                    Res.string.license, mediaAttachment.license.name
//                ), onClick = {
//                    viewModel.openUrl(mediaAttachment.license.url)
//                    closeBottomSheet()
//                })
//        }

        HorizontalDivider(Modifier.padding(12.dp))

        ButtonRowElement(
            icon = Res.drawable.open, text = stringResource(
                Res.string.open_in_browser
            ), onClick = {
                viewModel.openUrl(url)
                closeBottomSheet()
            })

        ButtonRowElement(
            icon = Res.drawable.share,
            text = stringResource(Res.string.share_this_post),
            onClick = {
                viewModel.shareText(url)
                closeBottomSheet()
            })

        if (PlatformFeatures.downloadToGallery && mediaAttachment?.url != null) {
            val snackbarPresenter = LocalSnackbarPresenter.current
            ButtonRowElement(
                icon = Res.drawable.download,
                text = stringResource(Res.string.download_image),
                onClick = {
                    viewModel.saveImage(mediaAttachment.url)
                    snackbarPresenter("Image saved to the gallery")
                    closeBottomSheet()
                })
        }

        if (minePost) {
            HorizontalDivider(Modifier.padding(12.dp))

            ButtonRowElement(
                icon = Res.drawable.edit, text = stringResource(Res.string.edit_post), onClick = {
                    navController.navigate(Destination.EditPost(post.id))
                })

            ButtonRowElement(
                icon = Res.drawable.trash,
                text = stringResource(Res.string.delete_this_post),
                onClick = {
                    viewModel.deleteDialog = post.id
                },
                color = MaterialTheme.colorScheme.error
            )
        } else {
            HorizontalDivider(Modifier.padding(12.dp))

            val relationship = viewModel.relationshipState.accountRelationship

            if (relationship == null || relationship.muted == true || relationship.mutedNotifications == true || relationship.mutedStatuses == true || relationship.mutedReblogs == true) {
                ButtonRowElement(
                    icon = Res.drawable.muted, text = stringResource(
                        Res.string.mute_this_profile
                    ), onClick = {
                        showMuteAlert = true
                    }, color = MaterialTheme.colorScheme.error
                )
            }
            if (relationship == null || !relationship.blocked) {
                ButtonRowElement(
                    icon = Res.drawable.blocked, text = stringResource(
                        Res.string.block_this_profile
                    ), onClick = {
                        showBlockAlert = true
                    }, color = MaterialTheme.colorScheme.error
                )
            }



            ButtonRowElement(
                icon = Res.drawable.warning,
                text = stringResource(Res.string.report_this_post),
                onClick = {
                    isReportDialogOpen = true
                },
                color = MaterialTheme.colorScheme.error
            )
        }
    }

    if (showMuteAlert) {
        LaunchedEffect(Unit) {
            viewModel.getRelationship()
        }
        MuteAccountAlert(
            onDismissRequest = { showMuteAlert = false },
            onConfirmation = { userMuteRequest ->
                showMuteAlert = false
                viewModel.post?.account?.let {
                    viewModel.muteAccount(
                        it.id, it.username, userMuteRequest
                    )
                }
                closeBottomSheet()
            },
            mutedAccount = viewModel.mutedAccount,
            capabilities = viewModel.capabilities.value
        )
    }
    if (showBlockAlert) {
        BlockAccountAlert(
            onDismissRequest = { showBlockAlert = false },
            onConfirmation = { userBlockRequest ->
                showBlockAlert = false
                viewModel.post?.account?.let {
                    viewModel.blockAccount(
                        it.id, it.username, userBlockRequest
                    )
                }
                closeBottomSheet()
            },
            account = viewModel.post?.account,
            capabilities = viewModel.capabilities.value
        )
    }

    if (isReportDialogOpen) {
        ReportDialog(
            dismissDialog = {
                isReportDialogOpen = false
                viewModel.reportState = null
            }, reportState = viewModel.reportState
        ) { category ->
            viewModel.reportPost(category)
            viewModel.reportState = null
        }
    }
}
