package com.daniyalak.stepcounterkotlin_androidfitnessapp.service

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.daniyalak.stepcounterkotlin_androidfitnessapp.callback.stepsCallback
import com.daniyalak.stepcounterkotlin_androidfitnessapp.helper.GeneralHelper
import com.daniyalak.stepcounterkotlin_androidfitnessapp.helper.PrefsHelper
import kotlin.math.roundToInt

class StepDetectorService : Service(), SensorEventListener {

    companion object {
        lateinit var callback: stepsCallback
    }

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val sensorManager: SensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val countSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        Log.d("TAGGGGG", "onSensorChanged: $countSensor")

        if(countSensor != null){
            Log.d("TAGGGGG", "onSensorChanged: countSensor")
            Toast.makeText(this, "Step Detecting Start", Toast.LENGTH_SHORT).show()
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_NORMAL)

            GeneralHelper.updateNotification(this, this, PrefsHelper.getInt("FSteps"))
            callback.subscribeSteps(PrefsHelper.getInt("FSteps"))

        }else{
            Toast.makeText(this, "Sensor Not Detected", Toast.LENGTH_SHORT).show()
        }

        return START_STICKY_COMPATIBILITY
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        Log.d("TAGGGGG", "onSensorChanged: 1")

        if (PrefsHelper.getString("TodayDate") != GeneralHelper.getToadyDate()) {
            PrefsHelper.putInt("Steps", p0!!.values[0].roundToInt())
            PrefsHelper.putString("TodayDate", GeneralHelper.getToadyDate())
        } else {
            val storeSteps = PrefsHelper.getInt("Steps")
            Log.d("TAGGGGG", "onSensorChanged: $storeSteps")
            val sensorSteps = p0!!.values[0].roundToInt()
            val finalSteps = sensorSteps - storeSteps
            if (finalSteps > 0) {
                PrefsHelper.putInt("FSteps", finalSteps)
                GeneralHelper.updateNotification(this, this, finalSteps)
                callback?.subscribeSteps(finalSteps)
            }
        }

    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "stp001",
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(serviceChannel)
        }
    }
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Log.d("SERVICE", p0.toString())
    }

    object subscribe {
        fun register(activity: Activity) {
            callback = activity as stepsCallback
        }
    }

}