private lateinit var dbHelper: SleepDatabaseHelper
package com.example.smartalarm

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.smartalarm.sensor.SleepSensorManager
import com.example.smartalarm.ui.theme.SmartAlarmTheme

class MainActivity : ComponentActivity() {

    private lateinit var sleepSensorManager: SleepSensorManager

    private val magnitudeState = mutableStateOf(0f)
    private val categoryState = mutableStateOf("LOW")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = SleepDatabaseHelper(this)

        sleepSensorManager = SleepSensorManager(this)

        sleepSensorManager.movementCallback = { mag, cat ->
            runOnUiThread {
                magnitudeState.value = mag
                categoryState.value = cat

                // Save into database
                dbHelper.insertData(mag, cat)
            }
        }
            Log.d("SleepData", "Magnitude: $mag | Category: $cat")
        }

        setContent {
            SmartAlarmTheme {
                MainScreen(
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