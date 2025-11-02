package com.example.educanetapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.educanetapp.ui.login.LoginScreen
import com.example.educanetapp.ui.login.RegisterScreen
import com.example.educanetapp.ui.profile.ProfileScreen
import com.example.educanetapp.ui.resources.ResourcesScreen
import com.example.educanetapp.ui.classes.ClassesScreen
import com.example.educanetapp.ui.agenda.AgendaScreen
import com.example.educanetapp.viewmodel.AuthViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {

        // Login
        composable("login") {
            val authViewModel: AuthViewModel = viewModel()
            LoginScreen(navController = navController, viewModel = authViewModel)
        }

        // Register
        composable("register") {
            val authViewModel: AuthViewModel = viewModel()
            RegisterScreen(navController = navController, viewModel = authViewModel)
        }

        // Profile con argumento userEmail
        composable(
            route = "profile/{userEmail}",
            arguments = listOf(navArgument("userEmail") { type = NavType.StringType })
        ) { backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("userEmail") ?: ""
            ProfileScreen(navController = navController, userEmail = userEmail)
        }

        // Recursos educativos
        composable("resources") {
            ResourcesScreen(navController)
        }

        // Clases
        composable("classes") {
            ClassesScreen(navController)
        }

        // Agenda
        composable("agenda") {
            AgendaScreen(navController)
        }
    }
}
