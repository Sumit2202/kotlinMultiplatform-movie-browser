import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import style.MovieBrowserKmmTheme
import ui.features.MovieList
import decompose.MovieBrowserKmmRoot

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(root: MovieBrowserKmmRoot) {


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
                AppScaffoldContent(root) {
                    scope.launch {
                        drawerState.open()
                    }
                }
            }
        }
    }
}

@Composable
fun AppDrawer() {
    Row {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(10.dp)
                .border(2.dp, Color.Gray)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxSize(),
                textAlign = TextAlign.Center,
                text = "Hi, All good!"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffoldContent(root: MovieBrowserKmmRoot,onHamburgerClicked: () -> Unit) {

    var bottomBarVisibilityState by rememberSaveable { (mutableStateOf(true)) }
    val topBarVisibilityState by rememberSaveable { (mutableStateOf(true)) }
    Scaffold(
        topBar = {
            SetupTopBar(onHamburgerClicked, topBarVisibilityState)
        },
        /*bottomBar = {
            SetupBottomBar(navController, bottomBarVisibilityState)
        }*/
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            MovieList()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun SetupTopBar(
    onHamburgerClicked: () -> Unit,
    topBarVisibilityState: Boolean
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
                IconButton(
                    onClick = {
                        onHamburgerClicked()
                    }) {
                    Icon(
                        painterResource("menu_top_icon.xml"),
                        contentDescription = null
                    )
                }
            },
        )
    }
}
