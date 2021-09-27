package dev.msfjarvis.claw.android.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.primarySurface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.msfjarvis.claw.android.viewmodel.ClawViewModel
import dev.msfjarvis.claw.common.theme.LobstersTheme
import dev.msfjarvis.claw.common.urllauncher.UrlLauncher
import kotlinx.coroutines.launch

private const val ScrollDelta = 50

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LobstersApp(
  viewModel: ClawViewModel = viewModel(),
  urlLauncher: UrlLauncher,
) {
  val systemUiController = rememberSystemUiController()
  val scaffoldState = rememberScaffoldState()
  val listState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()
  var isFabVisible by remember { mutableStateOf(true) }
  val nestedScrollConnection = remember {
    object : NestedScrollConnection {
      override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.y

        if (delta > ScrollDelta) {
          isFabVisible = true
        } else if (delta < -ScrollDelta) {
          isFabVisible = false
        }

        // We didn't consume any offset here so return Offset.Zero
        return Offset.Zero
      }
    }
  }
  LobstersTheme(darkTheme = isSystemInDarkTheme()) {
    ProvideWindowInsets {
      val useDarkIcons = MaterialTheme.colors.isLight
      val statusBarColor = MaterialTheme.colors.primarySurface

      SideEffect {
        systemUiController.setStatusBarColor(color = statusBarColor, darkIcons = useDarkIcons)
        systemUiController.setNavigationBarColor(color = Color.Transparent)
      }
      val items = viewModel.pagerFlow.collectAsLazyPagingItems()
      Scaffold(
        scaffoldState = scaffoldState,
        topBar = { ClawAppBar(modifier = Modifier.statusBarsPadding()) },
        floatingActionButton = {
          ClawFab(
            isFabVisible = isFabVisible,
            listState = listState,
            modifier = Modifier.navigationBarsPadding(),
          )
        },
      ) {
        val isRefreshing = items.loadState.refresh == LoadState.Loading
        SwipeRefresh(
          state = rememberSwipeRefreshState(isRefreshing),
          onRefresh = viewModel::reloadPosts,
        ) {
          if (items.itemCount == 0) {
            Box(modifier = Modifier.fillMaxSize())
          } else {
            NetworkPosts(
              items = items,
              launchUrl = urlLauncher::launch,
              listState = listState,
              isSaved = viewModel::isPostSaved,
              toggleSave = { coroutineScope.launch { viewModel.toggleSave(it) } },
              modifier = Modifier.padding(top = 16.dp).nestedScroll(nestedScrollConnection),
            )
          }
        }
      }
    }
  }
}
