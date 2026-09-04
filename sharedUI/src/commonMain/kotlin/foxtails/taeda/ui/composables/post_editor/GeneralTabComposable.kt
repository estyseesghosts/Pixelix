package foxtails.taeda.ui.composables.post_editor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import foxtails.taeda.domain.model.Visibility
import foxtails.taeda.ui.composables.textfield_location.TextFieldLocationsComposable
import foxtails.taeda.ui.composables.widgets.MaxLengthTextField
import foxtails.taeda.ui.composables.widgets.SuggestionsBar
import foxtails.taeda.utils.getPlatformUriObject
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import foxtails.taeda.app.generated.resources.Res
import foxtails.taeda.app.generated.resources.audience
import foxtails.taeda.app.generated.resources.audience_public
import foxtails.taeda.app.generated.resources.caption
import foxtails.taeda.app.generated.resources.category
import foxtails.taeda.app.generated.resources.chatbubble
import foxtails.taeda.app.generated.resources.confirm
import foxtails.taeda.app.generated.resources.content_warning_or_spoiler_text
import foxtails.taeda.app.generated.resources.disable_comments
import foxtails.taeda.app.generated.resources.eye_off
import foxtails.taeda.app.generated.resources.followers_only
import foxtails.taeda.app.generated.resources.globe
import foxtails.taeda.app.generated.resources.location
import foxtails.taeda.app.generated.resources.lock
import foxtails.taeda.app.generated.resources.mentioned_only
import foxtails.taeda.app.generated.resources.send
import foxtails.taeda.app.generated.resources.sensitive_content
import foxtails.taeda.app.generated.resources.sensitive_nsfw_media
import foxtails.taeda.app.generated.resources.tag
import foxtails.taeda.app.generated.resources.unlisted


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralTab(
    viewModel: PostEditorViewModel,
    paddingValues: PaddingValues
) {
    val suggestionsState by viewModel.hashtagMentionsSuggestionsManager.suggestionsState.collectAsStateWithLifecycle()
    val verticalScrollState = rememberScrollState()
    Column(Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.verticalScroll(verticalScrollState).weight(1f)
                .padding(paddingValues).padding(all = 12.dp)
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                itemsIndexed(viewModel.mediaItems) { index, image ->
                    AsyncImage(
                        model = image.imageUri.getPlatformUriObject(),
                        contentDescription = "Thumbnail $index",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp))
                    )
                }
            }

            @Composable
            fun CaptionField(viewModel: PostEditorViewModel) {
                MaxLengthTextField(
                    value = viewModel.caption,
                    onValueChange = { viewModel.updateCaption(it) },
                    textFieldModifier = Modifier.fillMaxWidth().onFocusChanged { focusState ->
                        viewModel.hashtagMentionsSuggestionsManager.onFocusChanged(focusState.isFocused)
                    },
                    label = Res.string.caption,
                    maxLength = viewModel.instance?.configuration?.statusConfig?.maxCharacters,
                    minLines = 4,
                    submit = {}
                )
            }

            @OptIn(ExperimentalMaterial3Api::class)
            @Composable
            fun VisibilityField(viewModel: PostEditorViewModel) {
                var isExpanded by remember { mutableStateOf(false) }
                val visibility = viewModel.visibility
                val (buttonText, buttonIcon) = when (visibility) {
                    Visibility.PUBLIC -> stringResource(Res.string.audience_public) to Res.drawable.globe
                    Visibility.UNLISTED -> stringResource(Res.string.unlisted) to Res.drawable.eye_off
                    Visibility.PRIVATE -> stringResource(Res.string.followers_only) to Res.drawable.lock
                    Visibility.DIRECT -> stringResource(Res.string.mentioned_only) to Res.drawable.send
                }

                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = { isExpanded = it }
                ) {
                    TextField(
                        value = buttonText,
                        onValueChange = { isExpanded = true },
                        modifier = Modifier.fillMaxWidth().menuAnchor(
                            ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            enabled = true
                        ),
                        label = { Text(stringResource(Res.string.audience)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                        leadingIcon = { Icon(vectorResource(buttonIcon), contentDescription = null) },
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        ),
                        shape = MaterialTheme.shapes.medium,
                        readOnly = true
                    )

                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }
                    ) {
                        val options = buildList {
                            if (!(viewModel.accountState.account?.locked ?: false)) {
                                add(Visibility.PUBLIC to (stringResource(Res.string.audience_public) to Res.drawable.globe))
                                add(Visibility.UNLISTED to (stringResource(Res.string.unlisted) to Res.drawable.eye_off))
                            }
                            add(Visibility.PRIVATE to (stringResource(Res.string.followers_only) to Res.drawable.lock))
                            if (viewModel.capabilities.value.newPost.includeDirectVisibility) {
                                add(Visibility.DIRECT to (stringResource(Res.string.mentioned_only) to Res.drawable.send))
                            }
                        }
                        options.forEach { (option, labelAndIcon) ->
                            DropdownMenuItem(
                                text = { Text(labelAndIcon.first) },
                                onClick = {
                                    viewModel.visibility = option
                                    isExpanded = false
                                },
                                leadingIcon = { Icon(vectorResource(labelAndIcon.second), contentDescription = null) },
                                trailingIcon = {
                                    if (visibility == option) {
                                        Icon(
                                            vectorResource(Res.drawable.confirm),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }

            CaptionField(viewModel)

            NewPostPref(
                leadingIcon = Res.drawable.sensitive_content,
                title = stringResource(Res.string.sensitive_nsfw_media),
                trailingContent = {
                    Switch(
                        checked = viewModel.isSensitive,
                        onCheckedChange = { viewModel.isSensitive = it })
                })

            AnimatedVisibility(
                visible = viewModel.isSensitive,
                enter = slideInVertically() + fadeIn(),
                exit = shrinkVertically(animationSpec = spring(stiffness = Spring.StiffnessMedium)) + fadeOut(),
            ) {
                NewPostTextField(
                    value = viewModel.contentWarning,
                    onChange = { viewModel.contentWarning = it },
                    label = stringResource(Res.string.content_warning_or_spoiler_text)
                )
            }

            VisibilityField(viewModel)

            if (viewModel.capabilities.value.newPost.showCategoriesDropdown) {
                var isCategoriesExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = isCategoriesExpanded,
                    onExpandedChange = { isCategoriesExpanded = it }
                ) {
                    TextField(
                        value = viewModel.categoriesState.selectedCategory?.name ?: "",
                        onValueChange = { _ ->
                            isCategoriesExpanded = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true),
                        label = { Text(stringResource(Res.string.category)) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoriesExpanded)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = vectorResource(Res.drawable.tag),
                                contentDescription = null
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        ),
                        shape = MaterialTheme.shapes.medium,
                        readOnly = true
                    )

                    ExposedDropdownMenu(
                        expanded = isCategoriesExpanded, onDismissRequest = {
                            isCategoriesExpanded = false
                        }) {
                        viewModel.categoriesState.categories.forEach {
                            DropdownMenuItem(
                                text = { Text(it.name) },
                                onClick = {
                                    viewModel.categoriesState = viewModel.categoriesState.copy(selectedCategory = it)
                                    isCategoriesExpanded = false
                                },
                                trailingIcon = {
                                    if (viewModel.categoriesState.selectedCategory?.id == it.id) {
                                        Icon(
                                            imageVector = vectorResource(Res.drawable.confirm),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                })
                        }
                    }
                }
            }

            NewPostPref(
                leadingIcon = Res.drawable.chatbubble,
                title = stringResource(Res.string.disable_comments),
                trailingContent = {
                    Switch(
                        checked = viewModel.areCommentsDisabled,
                        onCheckedChange = { viewModel.areCommentsDisabled = it })
                })

            if (viewModel.capabilities.value.newPost.showLocationInputInGeneral) {
                TextFieldLocationsComposable(
                    submit = {
                        viewModel.locationId = it.id
                    },
                    initialValue = null,
                    labelStringId = Res.string.location,
                    modifier = Modifier.fillMaxWidth(),
                    imeAction = ImeAction.Default,
                    suggestionsBoxColor = MaterialTheme.colorScheme.surfaceContainer,
                    submitButton = null
                )
            }
        }
        if (viewModel.hashtagMentionsSuggestionsManager.suggestionsOpen) {
            SuggestionsBar(
                state = suggestionsState, bottomBarPadding = false, onSelected = { selected ->
                    viewModel.caption =
                        viewModel.hashtagMentionsSuggestionsManager.selectSuggestion(
                            selected, viewModel.caption
                        )
                })
        }
    }

}