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
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background

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
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AppDrawer() {
    Column {
        UserImageArea()
        Spacer(modifier = Modifier.fillMaxWidth().height(2.dp).background(MaterialTheme.colorScheme.primary))
        DrawerOptions()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffoldContent(
    root: MovieBrowserKmmRoot,
    onHamburgerClicked: () -> Unit
) {

    var backArrowVisibilityState by remember { mutableStateOf(false) }
    var bottomBarVisibilityState by rememberSaveable { (mutableStateOf(true)) }
    var topBarVisibilityState by remember { mutableStateOf(true) }
    Scaffold(
        topBar = {
            SetupTopBar(onHamburgerClicked, topBarVisibilityState, backArrowVisibilityState)
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).background(Color.LightGray)
        ) {
            Children(root.childStack) {
                when (val child = it.instance) {
                    is MovieBrowserKmmRoot.Child.MainScreen -> {
                        backArrowVisibilityState = false
                        topBarVisibilityState = true
                        MovieList(child.mainScreenComponent)
                    }

                    is MovieBrowserKmmRoot.Child.DetailScreen -> {
                        backArrowVisibilityState = true
                        topBarVisibilityState = false
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
    backArrowVisibilityState: Boolean
) {
    AnimatedVisibility(
        visible = topBarVisibilityState,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it }),
    ) {
        TopAppBar(
            title = { Text(text = "Movie Browser KMM"/*stringResource(id = R.string.app_name)*/) },
            navigationIcon = {
                ShowHamburgerIcon(
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

