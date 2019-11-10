package com.example.jetpackcompose

import androidx.annotation.DrawableRes
import androidx.compose.*
import androidx.ui.animation.Crossfade
import androidx.ui.core.WithDensity
import androidx.ui.foundation.Clickable
import androidx.ui.graphics.Color
import androidx.ui.graphics.vector.DrawVector
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.material.ripple.Ripple
import androidx.ui.material.surface.Surface
import androidx.ui.res.vectorResource
import com.example.jetpackcompose.resource.appThemeColors
import com.example.jetpackcompose.resource.themeTypography
import com.example.jetpackcompose.ui.GithubRepoViewModel
import com.example.jetpackcompose.ui.HomeScreen

@Composable
fun Main(viewModel: GithubRepoViewModel) {
    val (drawerState, onDrawerStateChange)= +state { DrawerState.Closed }
    MaterialTheme(
        colors = appThemeColors,
        typography = themeTypography
    ) {
        ModalDrawerLayout(
            drawerState = drawerState,
            onStateChange = onDrawerStateChange,
            gesturesEnabled = drawerState == DrawerState.Opened,
            drawerContent = {
                AppDrawer(
                    currentScreen = MyAppState.currentScreen,
                    closeDrawer = { onDrawerStateChange(DrawerState.Closed) }
                )
            },
            bodyContent = {
                AppContent(viewModel) { onDrawerStateChange(DrawerState.Opened) }
            }
        )
    }
}

enum class Screen {
    HOME,
    FAVORITE
}

@Composable
private fun AppContent(viewModel: GithubRepoViewModel, openDrawer: () -> Unit) {
    Crossfade(MyAppState.currentScreen) { screen ->
        Surface(color = +themeColor { primary }) {
            when(screen) {
                Screen.HOME -> HomeScreen(
                    viewModel = viewModel,
                    openDrawer = openDrawer
                )
                Screen.FAVORITE -> {}
            }
        }
    }
}


@Model
object MyAppState {
    var currentScreen: Screen = Screen.HOME
}

@Composable
private fun AppDrawer(currentScreen: Screen, closeDrawer: () -> Unit) {
    Column(
        crossAxisSize = LayoutSize.Expand,
        mainAxisSize = LayoutSize.Expand
    ) {
        DrawerButton(icon = R.drawable.ic_home, label = "Home", isSelected = currentScreen == Screen.HOME) {
            // TODO: Implementation
        }
    }
}

@Composable
private fun DrawerButton(
    @DrawableRes icon: Int,
    label: String,
    isSelected: Boolean,
    action: () -> Unit
) {
    Button(onClick = action, style = TextButtonStyle()) {
        Row(mainAxisSize = LayoutSize.Expand,
            crossAxisAlignment = CrossAxisAlignment.Center) {
            VectorImage(id = icon)
        }
    }
}

@Composable
fun VectorImage(@DrawableRes id: Int, tint: Color = Color.Transparent) {
    val vector = +vectorResource(id)
    WithDensity {
        Container(width = vector.defaultWidth.toDp(), height = vector.defaultHeight.toDp()) {
            DrawVector(vector, tint)
        }
    }
}

@Composable
fun VectorImageButton(@DrawableRes id: Int, onClick: () -> Unit) {
    Ripple(bounded = false) {
        Clickable(onClick = onClick) {
            VectorImage(id = id)
        }
    }
}