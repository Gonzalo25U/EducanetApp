package com.example.educanetapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.educanetapp.ui.login.LoginScreen
import com.example.educanetapp.ui.profile.ProfileScreen
import com.example.educanetapp.ui.resources.ResourcesScreen
import com.example.educanetapp.ui.classes.ClassesScreen
import com.example.educanetapp.ui.classes.ClasesVirtualesScreen
import com.example.educanetapp.ui.agenda.AgendaScreen
import com.example.educanetapp.ui.settings.SettingsScreen
import com.example.educanetapp.ui.test.TestConnectionScreen
import com.example.educanetapp.viewmodel.AuthViewModel
import com.example.educanetapp.viewmodel.ClassesViewModel
import com.example.educanetapp.viewmodel.AgendaViewModel
import com.example.educanetapp.viewmodel.AgendaViewModelFactory

@Composable
fun NavGraph(navController: NavHostController) {
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = "login") {

        composable("test_connection") { TestConnectionScreen() }

        // 🔹 Login
        composable("login") {
            val authViewModel: AuthViewModel = viewModel()
            LoginScreen(navController = navController, viewModel = authViewModel)
        }


        // 🔹 Perfil (con argumento userEmail)
        composable(
            route = "profile/{userEmail}",
            arguments = listOf(navArgument("userEmail") { type = NavType.StringType })
        ) { backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("userEmail") ?: ""
            ProfileScreen(navController = navController, userEmail = userEmail)
        }

        // 🔹 Settings
        composable("settings/{userEmail}") { backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("userEmail") ?: ""
            SettingsScreen(navController = navController, userEmail = userEmail)
        }

        // 🔹 Recursos educativos
        composable("resources") {
            ResourcesScreen(navController)
        }

        // 🔹 Clases (pantalla de permisos)
        composable("classes") {
            val classesViewModel: ClassesViewModel = viewModel()
            ClassesScreen(navController = navController, viewModel = classesViewModel)
        }

        // 🔹 Clases en curso / Virtuales
        composable("clasesVirtuales") {
            ClasesVirtualesScreen(navController)
        }

        // 🔹 Agenda
        composable("agenda") {
            AgendaScreen(navController = navController)
        }
    }
}