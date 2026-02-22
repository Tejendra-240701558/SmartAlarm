package com.example.smartalarm

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartalarm.sensor.SleepSensorManager
import com.example.smartalarm.ui.theme.SmartAlarmTheme

class MainActivity : ComponentActivity() {

    private lateinit var sleepSensorManager: SleepSensorManager

    // UI state holders
    private val magnitudeState = mutableStateOf(0f)
    private val categoryState = mutableStateOf("LOW")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sleepSensorManager = SleepSensorManager(this)

        // Attach callback safely here
        sleepSensorManager.movementCallback = { mag, cat ->
            runOnUiThread {
                magnitudeState.value = mag
                categoryState.value = cat
            }
            Log.d("SleepData", "Magnitude: $mag | Category: $cat")
        }

        setContent {
            SmartAlarmTheme {
                SleepScreen(
                    magnitude = magnitudeState.value,
                    category = categoryState.value
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sleepSensorManager.startListening()
    }

    override fun onPause() {
        super.onPause()
        sleepSensorManager.stopListening()
    }
}

@Composable
fun SleepScreen(magnitude: Float, category: String) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Sleep Movement Monitor", fontSize = 22.sp)
            Spacer(modifier = Modifier.height(20.dp))
            Text("Magnitude: %.2f".format(magnitude), fontSize = 18.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text("Category: $category", fontSize = 20.sp)
        }
    }
}