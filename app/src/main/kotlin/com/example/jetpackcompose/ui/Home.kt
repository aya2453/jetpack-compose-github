package com.example.jetpackcompose.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.*
import androidx.ui.core.*
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.*
import androidx.ui.input.EditorStyle
import androidx.ui.input.ImeAction
import androidx.ui.input.KeyboardType
import androidx.ui.layout.*
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.material.Divider
import androidx.ui.material.ripple.Ripple
import androidx.ui.material.surface.Surface
import androidx.ui.material.themeColor
import androidx.ui.material.themeTextStyle
import com.example.jetpackcompose.*
import com.example.jetpackcompose.R

@Composable
fun HomeScreen(viewModel: GithubRepoViewModel, openDrawer: () -> Unit) {
    FlexColumn {
        inflexible {
            Padding(32.dp) {
                Surface(color = +themeColor { primaryVariant }, shape = RoundedCornerShape(30.dp)) {
                    SearchBar(viewModel)
                }
            }
        }

        flexible(flex = 1f) {
            Surface(color = +themeColor { background }, shape = RoundedCornerShape(24.dp, 24.dp)) {
                FlexColumn(crossAxisSize = LayoutSize.Expand) {
                    inflexible {
                        Text(
                            "Search Results️: ${GithubRepoState.githubRepoResult?.totalCount
                                ?: 0} hits️",
                            style = (+themeTextStyle { h5 }),
                            modifier = Spacing(left = 16.dp, top = 16.dp, bottom = 12.dp)
                        )
                    }

                    flexible(1f) {
                        SingleMessage()
                        ProgressBar2()
                        GithubRepos()
                    }
                }
            }
        }
    }
}

@Composable
private fun SingleMessage() {
    if (!GithubRepoState.viewStatus.shouldShowSingleMessage) {
        return
    }
    Text(text = GithubRepoState.viewStatus.message, modifier = Spacing(left = 16.dp))
}

@Composable
private fun ProgressBar2() {
    if (GithubRepoState.viewStatus != ViewStatus.LOADING) {
        return
    }
    Center {
        CircularProgressIndicator()
    }
}

@Composable
private fun GithubRepos() {
    val context = +ambient(ContextAmbient)
    val items = GithubRepoState.githubRepoResult?.items
    if (GithubRepoState.viewStatus != ViewStatus.COMPLETED
        || items == null
    ) {
        return
    }

    VerticalScroller {
        Column {
            items.forEach {
                Ripple(bounded = true) {
                    Clickable(onClick = {
                        launchBrowser(context, it)
                    }) {
                        Padding(top = 16.dp, bottom = 16.dp) {
                            GithubItem(it)
                        }
                    }
                }
                Divider(
                    color = Color(0xFFDDDDDD),
                    modifier = Spacing(left = 32.dp)
                )
            }
        }
    }
}

private fun launchBrowser(context: Context, item: GithubRepo) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.htmlUrl))
    context.startActivity(intent)
}

@Composable
private fun GithubItem(item: GithubRepo) {
    FlexRow {
        inflexible {
            Padding(left = 16.dp) {
                VectorImage(
                    id = R.drawable.ic_account_circle,
                    tint = +themeColor { primary }
                )
            }
        }
        expanded(1f) {
            Column(modifier = Spacing(left = 8.dp, right = 8.dp)) {
                Text(item.name)
                HeightSpacer(height = 4.dp)
                Text(item.description ?: "", style = (+themeTextStyle { subtitle2 }))
            }
        }
    }
}

@Composable
private fun SearchBar(viewModel: GithubRepoViewModel) {
    FlexRow(mainAxisAlignment = MainAxisAlignment.Center, modifier = Spacing(8.dp)) {
        inflexible {
            Padding(left = 6.dp, right = 16.dp) {
                VectorImage(
                    id = R.drawable.ic_search,
                    tint = +themeColor { background })
            }
        }
        expanded(1.0f) {
            val state = +state { EditorModel() }
            TextField(
                focusIdentifier = "ほげ",
                value = state.value,
                onValueChange = { new ->
                    state.value = if (new.text.any { it == '\n' }) {
                        state.value
                    } else {
                        new
                    }
                },
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search,
                editorStyle = EditorStyle(textStyle = (+themeTextStyle { body2 })),
                onImeActionPerformed = {
                    if (it == ImeAction.Search) {
                        if (!state.value.text.isBlank()) {
                            viewModel.fetchRepositories(state.value.text)
                        }
                    }
                })

        }
    }
}