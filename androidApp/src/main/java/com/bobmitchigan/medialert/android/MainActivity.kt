package com.bobmitchigan.medialert.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bobmitchigan.medialert.MR
import com.bobmitchigan.medialert.android.ui.CalendarScreen
import com.bobmitchigan.medialert.android.ui.CreateMedicineScreen
import com.bobmitchigan.medialert.android.ui.MedicineDetailScreen
import com.bobmitchigan.medialert.android.ui.MedicineListScreen
import com.bobmitchigan.medialert.android.ui.component.navigateSingleTop
import com.bobmitchigan.medialert.domain.Destination
import com.bobmitchigan.medialert.viewModel.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: SplashViewModel by viewModel()
    private val showSplash: Boolean
        get() = viewModel.nextDestination.value == null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition(::showSplash)
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val nextDestination by viewModel.nextDestination.collectAsStateWithLifecycle()

            MyApplicationTheme {
                Scaffold(
                    isFloatingActionButtonDocked = true,
                    floatingActionButtonPosition = FabPosition.Center,
                    floatingActionButton = {
                        if (isBottomNavigationVisible(navController = navController)) {
                            FloatingActionButton(
                                shape = CircleShape,
                                contentColor = MaterialTheme.colors.primary,
                                onClick = { navController.navigateSingleTop(Destination.CreateMedicine) }) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = stringResource(
                                        id = MR.strings.create_medicine_title.resourceId
                                    )
                                )
                            }
                        }
                    },
                    bottomBar = {
                        BottomNavigation(navController)
                    }
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        color = MaterialTheme.colors.background
                    ) {
                        MainContent(nextDestination, navController)
                    }
                }
            }
        }
    }

    @Composable
    private fun BottomNavigation(navController: NavHostController) {
        if (isBottomNavigationVisible(navController = navController)) {
            BottomNavigation(
                modifier = Modifier.fillMaxWidth(),
                elevation = 8.dp,
            ) {
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_medication_24),
                            contentDescription = null
                        )
                    },
                    label = { Text(stringResource(id = MR.strings.medicine_list.resourceId)) },
                    selected = isMedicineList(navController),
                    onClick = {
                        navController.navigate(Destination.MedicineList.destination()) {
                            launchSingleTop = true
                        }
                    })
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                            contentDescription = null
                        )
                    },
                    label = { Text(stringResource(id = MR.strings.medicine_calendar.resourceId)) },
                    selected = isCalendar(navController = navController),
                    onClick = { navController.navigateSingleTop(Destination.Calendar) })
            }
        }
    }

    /**
     * Determines whether the bottom navigation with FAB should be shown.
     *
     * @param navController The navigation controller.
     * @return True if the navigation should be shown, false otherwise.
     */
    @Composable
    private fun isBottomNavigationVisible(navController: NavHostController): Boolean {
        return currentRouteAsState(navController) in listOf(
            Destination.MedicineList,
            Destination.Calendar
        ).map { it.destination() }
    }

    /**
     * Determines whether the current destination is medicine list.
     *
     * @param navController The navigation controller.
     * @return True if the current destination is list.
     */
    @Composable
    private fun isMedicineList(navController: NavHostController) =
        currentRouteAsState(navController = navController) == Destination.MedicineList.destination()

    /**
     * Determines whether the current destination is calendar.
     *
     * @param navController The navigation controller.
     * @return True if the current destination is calendar.
     */
    @Composable
    private fun isCalendar(navController: NavHostController) =
        currentRouteAsState(navController = navController) == Destination.Calendar.destination()

    @Composable
    private fun currentRouteAsState(navController: NavHostController) =
        navController.currentBackStackEntryAsState().value?.destination?.route

    @Composable
    private fun MainContent(
        nextDestination: Destination?,
        navController: NavHostController
    ) {
        nextDestination?.let { dest ->
            NavHost(
                navController = navController,
                startDestination = dest.destination()
            ) {
                composable(Destination.CreateMedicine.destination()) {
                    CreateMedicineScreen(navController)
                }
                composable(
                    Destination.SingleMedicine.detailDestination(),
                    arguments = listOf(navArgument("medicineId") {
                        nullable = true
                        type = NavType.StringType
                        defaultValue = (dest as? Destination.SingleMedicine)?.medicineId
                    })
                ) {
                    MedicineDetailScreen(
                        it.arguments?.getString("medicineId")?.toIntOrNull(),
                        navController
                    )
                }
                composable(Destination.MedicineList.destination()) {
                    MedicineListScreen(navController)
                }
                composable(Destination.Calendar.destination()) {
                    CalendarScreen(navController)
                }
            }
        }
    }
}
