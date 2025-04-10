package com.example.myapplication

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.views.start.StartScreen
import com.example.myapplication.views.stratagem.StratagemScreen
import com.example.myapplication.views.stratagem.StratagemViewModel
import com.example.myapplication.views.stratagem_list.StratagemListScreen
import com.example.myapplication.views.stratagem_list.StratagemListViewModel
import com.example.myapplication.views.stratagem_list.StratagemListViewModelFactory
import com.example.myapplication.domain.repository.StratagemRepository
import com.example.myapplication.ui.theme.AppContent
import com.example.myapplication.utils.UiEvent
import com.example.myapplication.views.settings.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    stratagemRepository: StratagemRepository,
    settingsStore: DataStore<Preferences>,
) {
    val stratagemListViewModel: StratagemListViewModel = viewModel(
        factory = StratagemListViewModelFactory(stratagemRepository)
    )

    val navController = rememberNavController()

    var topBarTitle by remember { mutableStateOf("") }
    var topBarActions by remember { mutableStateOf<@Composable (() -> Unit)>({}) }

    var currentRoute by remember { mutableStateOf(navController.currentDestination?.route) }
    var previousRoute by remember { mutableStateOf(navController.previousBackStackEntry?.destination?.route) }

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            currentRoute = backStackEntry.destination.route
            previousRoute = navController.previousBackStackEntry?.destination?.route
        }
    }

    AppContent(settingsStore) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = topBarTitle,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        )
                    },
                    navigationIcon = {
                        if (currentRoute != Screen.StartScreen.route && previousRoute != Screen.StartScreen.route) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                    contentDescription = stringResource(R.string.navigation_navigate_back_description),
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    },
                    actions = {
                        Row {
                            topBarActions()
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                )
            },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.StartScreen.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = Screen.StartScreen.route) {
                    StartScreen(
                        onNext = { navController.navigate(Screen.StratagemListScreen.route) },
                        modifier = Modifier,
                    )
                }
                composable(route = Screen.settingsScreen.route) {
                    SettingsScreen(
                        updateTopBar = { title, actions ->
                            topBarTitle = title
                            topBarActions = actions
                        }
                    )
                }

                composable(route = Screen.StratagemListScreen.route) {
                    StratagemListScreen(
                        viewModel = stratagemListViewModel,
                        onStratagemClick = { stratagem ->
                            navController.navigate(
                                Screen.stratagemScreen.route.replace("{id}", stratagem.id.toString())
                            )
                        },
                        onAddStratagemClick = {
                            navController.navigate(Screen.stratagemScreen.route)
                        },
                        onSettingsClick = {
                            navController.navigate(Screen.settingsScreen.route)
                        },
                        updateTopBar = { title, actions ->
                            topBarTitle = title
                            topBarActions = actions
                        }
                    )
                }

                composable(
                    route = Screen.stratagemScreen.route,
                    arguments = listOf(navArgument("id") { type = NavType.StringType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")

                    val stratagemViewModel =
                        remember { StratagemViewModel(stratagemRepository, id?.toIntOrNull()) }
                    val state by stratagemViewModel.state.collectAsStateWithLifecycle()

                    LaunchedEffect(key1 = true) {
                        stratagemViewModel.event.collect { event ->
                            when (event) {
                                is UiEvent.NavigateBack -> {
                                    navController.popBackStack()
                                }

                                else -> Unit
                            }
                        }
                    }

                    StratagemScreen(
                        state = state,
                        onEvent = stratagemViewModel::onEvent,
                        updateTopBar = { title, actions ->
                            topBarTitle = title
                            topBarActions = actions
                        }
                    )
                }
            }
        }
    }
}