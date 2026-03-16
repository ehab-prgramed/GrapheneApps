package com.prgramed.eprayer.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.prgramed.eprayer.feature.prayertimes.PrayerTimesScreen
import com.prgramed.eprayer.feature.qibla.QiblaScreen
import com.prgramed.eprayer.feature.settings.SettingsScreen

private val Navy = Color(0xFF0F1B2D)
private val NavyBar = Color(0xFF0A1525)
private val Peach = Color(0xFFE8B98A)
private val TextMuted = Color(0xFF5A6A7A)

private data class NavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
)

private val navItems = listOf(
    NavItem(PrayerDestinations.PRAYER_TIMES, "Prayer", Icons.Default.Schedule),
    NavItem(PrayerDestinations.QIBLA, "Qibla", Icons.Default.Explore),
    NavItem(PrayerDestinations.SETTINGS, "Settings", Icons.Default.Settings),
)

@Composable
fun PrayerNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = modifier,
        containerColor = Navy,
        bottomBar = {
            NavigationBar(containerColor = NavyBar) {
                navItems.forEach { item ->
                    val selected = currentDestination?.hierarchy?.any {
                        it.route == item.route
                    } == true
                    NavigationBarItem(
                        icon = {
                            Icon(
                                item.icon,
                                contentDescription = item.label,
                                tint = if (selected) Peach else TextMuted,
                            )
                        },
                        label = {
                            Text(
                                item.label,
                                color = if (selected) Peach else TextMuted,
                            )
                        },
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Peach.copy(alpha = 0.12f),
                        ),
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = PrayerDestinations.PRAYER_TIMES,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(PrayerDestinations.PRAYER_TIMES) { PrayerTimesScreen() }
            composable(PrayerDestinations.QIBLA) { QiblaScreen() }
            composable(PrayerDestinations.SETTINGS) { SettingsScreen() }
        }
    }
}
