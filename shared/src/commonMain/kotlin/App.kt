import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.router.stack.active
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import decompose.MovieBrowserKmmRoot
import kotlinx.coroutines.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import style.MovieBrowserKmmTheme
import ui.features.DrawerOptions
import ui.features.MovieDetailsScreen
import ui.features.MovieList
import ui.features.UserImageArea
import util.generateImageLoader


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(root: MovieBrowserKmmRoot) {
    CompositionLocalProvider(
        LocalImageLoader provides generateImageLoader()
    ) {
        MovieBrowserKmmTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            AppDrawer()
                        }
                    }
                ) {
                    AppScaffoldContent(
                        root,
                        onHamburgerClicked = {
                            scope.launch {
                                drawerState.open()
                            }
                        },
                        onBackPressed = {
                            backPressed(root)
                        }
                    )
                }
            }
        }
    }
}

private fun backPressed(root: MovieBrowserKmmRoot) {
    print("onBackPressed :${root.childStack.active.instance}")
    if (root.childStack.active.instance is MovieBrowserKmmRoot.Child.DetailScreen) {
        (root.childStack.active.instance as MovieBrowserKmmRoot.Child.DetailScreen).detailsScreenComponent.onBackPressed()
    }
}

@Composable
fun AppDrawer() {
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        UserImageArea()
        Spacer(modifier = Modifier.height(10.dp).fillMaxWidth())
        DrawerOptions()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffoldContent(
    root: MovieBrowserKmmRoot,
    onHamburgerClicked: () -> Unit,
    onBackPressed: () -> Unit
) {

    var backArrowVisibilityState by remember { mutableStateOf(false) }
    var bottomBarVisibilityState by rememberSaveable { (mutableStateOf(true)) }
    val topBarVisibilityState by rememberSaveable { (mutableStateOf(true)) }
    Scaffold(
        topBar = {
            SetupTopBar(onHamburgerClicked, topBarVisibilityState, backArrowVisibilityState) {
                onBackPressed()
            }
        },
        /*bottomBar = {
            SetupBottomBar(navController, bottomBarVisibilityState)
        }*/
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            Children(root.childStack) {
                when (val child = it.instance) {
                    is MovieBrowserKmmRoot.Child.MainScreen -> {
                        backArrowVisibilityState = false
                        MovieList(child.mainScreenComponent)
                    }

                    is MovieBrowserKmmRoot.Child.DetailScreen -> {
                        backArrowVisibilityState = true
                        MovieDetailsScreen(child.detailsScreenComponent)
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun SetupTopBar(
    onHamburgerClicked: () -> Unit,
    topBarVisibilityState: Boolean,
    backArrowVisibilityState: Boolean,
    onBackPressed: () -> Unit
) {
    val scope = rememberCoroutineScope()
    AnimatedVisibility(
        visible = topBarVisibilityState,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it }),
    ) {
        TopAppBar(
            title = { Text(text = "Movie Buff"/*stringResource(id = R.string.app_name)*/) },
            navigationIcon = {
                if (backArrowVisibilityState) ShowBackArrow(onBackPressed) else ShowHamburgerIcon(
                    onHamburgerClicked
                )
            }
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ShowHamburgerIcon(onHamburgerClicked: () -> Unit) {
    IconButton(
        onClick = {
            onHamburgerClicked()
        }) {
        Icon(
            painterResource("menu_top_icon.xml"),
            contentDescription = null
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ShowBackArrow(onBackPressed: () -> Unit) {
    IconButton(
        onClick = {
            println("back button clicked")
            onBackPressed.invoke()
        }) {
        Icon(
            painterResource("arrow_back.xml"),
            contentDescription = null
        )
    }
}