package com.daniyalak.stepcounterkotlin_androidfitnessapp.ui

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.daniyalak.stepcounterkotlin_androidfitnessapp.R
import com.daniyalak.stepcounterkotlin_androidfitnessapp.callback.stepsCallback
import com.daniyalak.stepcounterkotlin_androidfitnessapp.helper.GeneralHelper
import com.daniyalak.stepcounterkotlin_androidfitnessapp.service.StepDetectorService
import com.daniyalak.stepcounterkotlin_androidfitnessapp.utils.PermissionUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), stepsCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         if(PermissionUtils.checkPhysicalPermission(this)) {

             val intent = Intent(this, StepDetectorService::class.java)
             startService(intent)

             StepDetectorService.subscribe.register(this)
         }else{
             PermissionUtils.checkPhysicalPermission(this)
         }



    }

    override fun subscribeSteps(steps: Int) {
        Log.d("TAGGGGG", "onSensorChanged: stepssteps$steps")
        TV_STEPS.setText(steps.toString())
        TV_CALORIES.setText(GeneralHelper.getCalories(steps))
        TV_DISTANCE.setText(GeneralHelper.getDistanceCovered(steps))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
           PermissionUtils.PHYCAL_ACTIVITY_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    Toast.makeText(this, "Physical Activity Permission Granted", Toast.LENGTH_SHORT).show()
                    // You can proceed with tasks that require this permission
                    PermissionUtils.checkNotificationPermission(this)


                } else {
                    // Permission denied
                    Toast.makeText(this, "Physical Activity Permission Denied", Toast.LENGTH_SHORT).show()
                    PermissionUtils.checkPhysicalPermission(this)
                    // Handle the case when the permission is not granted
                }
            }
        PermissionUtils.NOTIFICATION_PERMISSION_CODE -> {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Toast.makeText(this, "Notification Permission Granted", Toast.LENGTH_SHORT).show()
                // Proceed with posting notifications

                val intent = Intent(this, StepDetectorService::class.java)
                startService(intent)

                StepDetectorService.subscribe.register(this)
            } else {
                // Permission denied
                Toast.makeText(this, "Notification Permission Denied", Toast.LENGTH_SHORT).show()
                // Handle the case when the permission is not granted
            }
        }
        }



    }
}