package com.example.helper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.helper.ui.theme.HelperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelperTheme {
                UserApp()
            }
        }
    }
}

@Composable
fun UserApp(viewModel: UserViewModel = viewModel()) {
    val navController = rememberNavController()
    val users by viewModel.users.collectAsState()

    NavHost(navController = navController, startDestination = "userList") {
        composable("userList") {
            UserListScreen(
                users = users,
                onAddUser = { navController.navigate("addUser") },
                onUserClick = { userId -> navController.navigate("userDetail/$userId") },
                onDeleteUser = { userId -> viewModel.deleteUser(userId) }
            )
        }
        composable("addUser") {
            AddUserScreen(
                onSave = { name, email, age ->
                    viewModel.addUser(name, email, age)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            "userDetail/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            val user = users.find { it.id == userId }
            UserDetailScreen(
                user = user,
                onEdit = { navController.navigate("editUser/$userId") },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            "editUser/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            val user = users.find { it.id == userId }
            EditUserScreen(
                user = user,
                onSave = { name, email, age ->
                    viewModel.updateUser(userId, name, email, age)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
