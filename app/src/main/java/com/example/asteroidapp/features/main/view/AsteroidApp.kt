package com.example.asteroidapp.features.main.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.asteroidapp.R
import com.example.asteroidapp.api.AsteroidApiFilter
import com.example.asteroidapp.navigation.AsteroidNavHost

@Composable
fun AsteroidApp(navController: NavHostController = rememberNavController()) {
    AsteroidNavHost(navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsteroidAppTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onFilterClick: (AsteroidApiFilter) -> Unit = {},
    canNavigateBack: Boolean,
    showMenu: Boolean = true,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {}
) {
    var menuExpanded by rememberSaveable { mutableStateOf(false) }

    CenterAlignedTopAppBar(title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Filled.ArrowBack, contentDescription = stringResource(
                            R.string.text_back_button
                        )
                    )
                }
            }
        },
        actions = {
            if (showMenu) {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More Options")
                }
                DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                    DropdownMenuItem(text = { Text(stringResource(R.string.text_filter_today)) },
                        onClick = {
                            menuExpanded = false
                            onFilterClick(AsteroidApiFilter.SHOW_TODAY)
                        })
                    DropdownMenuItem(text = { Text(stringResource(R.string.text_filter_week)) },
                        onClick = {
                            menuExpanded = false
                            onFilterClick(AsteroidApiFilter.SHOW_WEEK)
                        })
                    DropdownMenuItem(text = { Text(stringResource(R.string.text_filter_saved)) },
                        onClick = {
                            menuExpanded = false
                            onFilterClick(AsteroidApiFilter.SHOW_SAVED)
                        })
                }
            }
        })
}