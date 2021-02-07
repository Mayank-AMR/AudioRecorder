package com.mayank_amr.voicerecord.recorder

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.mayank_amr.voicerecord.R
import kotlinx.android.synthetic.main.recorder_fragment.*
import java.text.SimpleDateFormat
import java.util.*


class RecorderFragment : Fragment() {
    private var isMicAccessAllowed = false
    private var isRecording = false

    private lateinit var mediaRecorder: MediaRecorder

    private lateinit var timer: Chronometer


    companion object {
        private const val MIC_PERMISSION_CODE = 101
        private const val TIME_FORMAT = "yyyyMMdd_HHmmss"

        fun newInstance() = RecorderFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.recorder_fragment, container, false)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        timer = cm_recorder_timer

        imageButton_record.setOnClickListener(View.OnClickListener {

            if (isRecording) {
                // Stop Recording......
                imageButton_record.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.ic_baseline_mic_desabled_120,
                        null
                    )
                )
                isRecording = false
                stopRecording()

            } else {
                /*
                * if Mic permission granted only then start recording.......
                */
                if (checkPermission()) {

                    startRecording() // Start recording....

                    imageButton_record.setImageDrawable(
                        resources.getDrawable(
                            R.drawable.ic_baseline_mic_enabled_120,
                            null
                        )
                    )
                    isRecording = true


                }
            }
        })


    }


    /*----------------------------*** Check Mic Permission ***------------------------------------*/
    private fun checkPermission(): Boolean {
        return if (activity?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.RECORD_AUDIO,
                )
            } == PackageManager.PERMISSION_GRANTED &&
            activity?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                )
            } == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    MIC_PERMISSION_CODE
                )
            }
            false
        }
    }


    /*----------------------------*** Start recording ***-----------------------------------------*/
    private fun startRecording() {
        val recordingPath = activity?.getExternalFilesDir("/Recordings/")?.absolutePath
        val fileName = "Recording_${getCurrentTime()}.3gpp" // Recorded file name.....
        timer.base = SystemClock.elapsedRealtime()
        timer.start()

        mediaRecorder = MediaRecorder()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder.setOutputFile("$recordingPath/$fileName")
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        mediaRecorder.prepare()
        mediaRecorder.start()
        showToast("Recording Started")

    }


    /*----------------------------*** Stop recording ***-----------------------------------------*/
    private fun stopRecording() {
        timer.stop()
        mediaRecorder.stop()
        mediaRecorder.release()
        showToast("Recording Saved")
    }


    /*----------------------------*** Pause recording ***-----------------------------------------*/
//    @RequiresApi(Build.VERSION_CODES.N)
//    fun pauseRecording() {
//        mediaRecorder.pause()
//    }


    /*----------------------------*** Resume recording ***----------------------------------------*/
//    @RequiresApi(Build.VERSION_CODES.N)
//    fun resumeRecording() {
//        mediaRecorder.resume()
//    }


    /*----------------------------*** Return current Time in string ***---------------------------*/
    @SuppressLint("SimpleDateFormat")
    private fun getCurrentTime() =
        SimpleDateFormat(TIME_FORMAT).format(Calendar.getInstance().timeInMillis)


    /*----------------------------*** To Show toast message ***-----------------------------------*/
    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }


}