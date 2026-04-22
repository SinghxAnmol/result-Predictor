package com.example.smallapp

import android.os.Bundle
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "input") {

        composable("input") {
            InputScreen(navController)
        }

        composable("result/{name}/{marks}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val marks = backStackEntry.arguments?.getString("marks") ?: "0"
            ResultScreen(name, marks, navController)
        }
    }
}

@Composable
fun InputScreen(navController: NavHostController) {

    var name by remember { mutableStateOf("") }
    var study by remember { mutableStateOf("") }
    var sleep by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text("🎓 Student Predictor", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(25.dp))

        Card(shape = RoundedCornerShape(20.dp)) {
            Column(modifier = Modifier.padding(20.dp)) {

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("👤 Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = study,
                    onValueChange = { study = it },
                    label = { Text("📚 Study Hours") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = sleep,
                    onValueChange = { sleep = it },
                    label = { Text("😴 Sleep Hours") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        Button(
            onClick = {
                val s = study.toDoubleOrNull()
                val sl = sleep.toDoubleOrNull()

                if (name.isNotEmpty() && s != null && sl != null) {
                    val marks = (s * 10 + sl * 5).toString()
                    navController.navigate("result/$name/$marks")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(15.dp)
        ) {
            Text("🚀 Predict Now")
        }
    }
}

@Composable
fun ResultScreen(name: String, marks: String, navController: NavHostController) {

    val marksDouble = marks.toDoubleOrNull() ?: 0.0

    val performance = when {
        marksDouble > 80 -> "🔥 Excellent"
        marksDouble > 50 -> "👍 Good"
        else -> "⚠️ Need Improvement"
    }

    val suggestion = when {
        marksDouble > 80 -> "Maintain your routine 💪"
        marksDouble > 50 -> "Increase study hours 📚"
        else -> "Focus more + avoid distractions 🚫"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("🎉 Prediction Result", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(25.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Text("👤 Name: $name", style = MaterialTheme.typography.titleLarge)

                Spacer(modifier = Modifier.height(10.dp))

                Text("📊 Marks: $marks", style = MaterialTheme.typography.headlineMedium)

                Spacer(modifier = Modifier.height(10.dp))

                Text("Performance: $performance")

                Spacer(modifier = Modifier.height(10.dp))

                Text("Suggestion: $suggestion")
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        Button(
            onClick = {
                navController.navigate("input")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("🔄 Try Again")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                val context = navController.context
                val shareText = "Name: $name\nMarks: $marks\n$performance"

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, shareText)

                context.startActivity(Intent.createChooser(intent, "Share via"))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("📤 Share Result")
        }
    }
}