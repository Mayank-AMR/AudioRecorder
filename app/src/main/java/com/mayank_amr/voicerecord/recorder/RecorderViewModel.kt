package com.mayank_amr.voicerecord.recorder

import android.media.MediaRecorder
import androidx.lifecycle.ViewModel

class RecorderViewModel : ViewModel() {
    private lateinit var mediaRecorder: MediaRecorder


    fun startRecording() {
        //var recordingPath =
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        // mediaRecorder.setOutputFile()
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
    }
}