package com.example.educanetapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.educanetapp.ui.login.LoginScreen
import com.example.educanetapp.ui.profile.ProfileScreen
import com.example.educanetapp.ui.resources.ResourcesScreen
import com.example.educanetapp.ui.classes.ClassesScreen
import com.example.educanetapp.ui.agenda.AgendaScreen
import com.example.educanetapp.ui.login.RegisterScreen
import com.example.educanetapp.viewmodel.AuthViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            val authViewModel: AuthViewModel = viewModel()
            LoginScreen(navController = navController, viewModel = authViewModel)
        }
        composable("profile") { ProfileScreen(navController) }
        composable("resources") { ResourcesScreen(navController) }
        composable("classes") { ClassesScreen(navController) }
        composable("agenda") { AgendaScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("resources") { ResourcesScreen(navController) }
        composable("agenda") { AgendaScreen(navController) }
    }
}