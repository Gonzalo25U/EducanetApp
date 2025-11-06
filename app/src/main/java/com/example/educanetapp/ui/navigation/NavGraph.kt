package com.example.educanetapp.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.educanetapp.ui.agenda.AgendaScreen
import com.example.educanetapp.ui.camera.CameraScreen
import com.example.educanetapp.ui.classes.ClassesScreen
import com.example.educanetapp.ui.login.LoginScreen
import com.example.educanetapp.ui.login.RegisterScreen
import com.example.educanetapp.ui.profile.ProfileScreen
import com.example.educanetapp.ui.resources.ResourcesScreen
import com.example.educanetapp.viewmodel.AuthViewModel
import com.example.educanetapp.viewmodel.ProfileViewModel
import com.example.educanetapp.viewmodel.ProfileViewModelFactory

@Composable
fun NavGraph(navController: NavHostController, startDestination: String = "login") {
    NavHost(navController = navController, startDestination = startDestination) {

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

        // Profile con argumento userEmail (MODIFICADO)
        composable(
            route = "profile/{userEmail}",
            arguments = listOf(navArgument("userEmail") { type = NavType.StringType })
        ) { backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("userEmail") ?: ""
            val context = LocalContext.current

            // 1. Creamos el ViewModel aquí usando su Factory
            val profileViewModel: ProfileViewModel = viewModel(
                factory = ProfileViewModelFactory(context, userEmail)
            )

            // 2. Observamos el SavedStateHandle para la URI de la imagen
            val capturedImageUri = backStackEntry.savedStateHandle
                .getLiveData<String>("captured_image_uri")
                .observeAsState(null) // Observamos como estado

            // 3. Cuando la URI cambie, notificamos al ViewModel
            LaunchedEffect(capturedImageUri.value) {
                capturedImageUri.value?.let { uriString ->
                    // Notificamos al ViewModel sobre la nueva imagen
                    profileViewModel.onProfileImageChanged(Uri.parse(uriString))

                    // Limpiamos el estado para que no se procese de nuevo
                    backStackEntry.savedStateHandle.remove<String>("captured_image_uri")
                }
            }

            // 4. Pasamos el ViewModel a la pantalla de perfil
            ProfileScreen(
                navController = navController,
                viewModel = profileViewModel
            )
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

        // Pantalla de Cámara (NUEVA RUTA)
        composable("camera") {
            CameraScreen(
                onImageCaptured = { savedUri ->
                    // 1. Cuando la imagen se captura, la "devolvemos" a la pantalla anterior
                    //    poniéndola en el SavedStateHandle de la ruta de perfil.
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("captured_image_uri", savedUri.toString())

                    // 2. Volvemos a la pantalla anterior (Perfil)
                    navController.popBackStack()
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}