package com.redeyesncode.gozulix.service

import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.Environment
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.redeyesncode.gozulix.R

class CallRecordingService : Service() {
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isRecording) {
            startRecording()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        if (isRecording) {
            stopRecording()
        }
        super.onDestroy()
    }
    private fun startRecording() {
        Log.i("ASHUTOSH1","SERVICE_RECORDING")

        try {
            mediaRecorder = MediaRecorder()
            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL)
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            // Set the output file path (adjust this as per your needs)
            val outputFilePath = getOutputFilePath()
            mediaRecorder?.setOutputFile(outputFilePath)

            mediaRecorder?.prepare()
            mediaRecorder?.start()

            isRecording = true
            showRecordingNotification()
        } catch (e: Exception) {
            // Handle exceptions
            e.printStackTrace()
            Log.i("ASHUTOSH0","SERVICE_RECORDING")
            Log.i("ASHUTOSH0","SERVICE_RECORDING")
            Log.i("ASHUTOSH0","SERVICE_RECORDING")
            Log.i("ASHUTOSH0","SERVICE_RECORDING")
            Log.i("ASHUTOSH0","SERVICE_RECORDING")
            Log.i("ASHUTOSH0","SERVICE_RECORDING")
            Log.i("ASHUTOSH0","SERVICE_RECORDING")
        }
    }

    private fun stopRecording() {
        try {
            mediaRecorder?.stop()
            mediaRecorder?.release()
            mediaRecorder = null

            isRecording = false
            stopForeground(true)
        } catch (e: Exception) {
            // Handle exceptions
            e.printStackTrace()
        }
    }

    private fun showRecordingNotification() {
        val notification = NotificationCompat.Builder(this, "your_channel_id")
            .setContentTitle("Call Recording Service")
            .setContentText("Recording calls in progress")
            .setSmallIcon(R.drawable.baseline_android_24)
            .build()

        startForeground(1, notification)
    }

    private fun getOutputFilePath(): String {
        // Return the desired file path where you want to save the recorded call
        // You can use external storage or your app's private directory
        return Environment.getExternalStorageDirectory().absolutePath + "/recorded_call.3gp"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}
