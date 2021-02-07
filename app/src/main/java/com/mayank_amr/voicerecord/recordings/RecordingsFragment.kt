package com.mayank_amr.voicerecord.recordings

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mayank_amr.voicerecord.R
import kotlinx.android.synthetic.main.player_bottom_sheet.*
import kotlinx.android.synthetic.main.recordings_fragment.*
import kotlinx.coroutines.Runnable
import java.io.File
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class RecordingsFragment : Fragment(), AudioListAdapter.OnListIemClickListener {

    private lateinit var allFiles: Array<File>
    private lateinit var fileAdapter: AudioListAdapter


    /*----------------------------*** Media Player  ***-------------------------------------------*/

    private var mediaPlayer: MediaPlayer? = null
    private var fileToPlay: File? = null
    private var isPlaying = false

    // Ui
    lateinit var seekBarHandler: Handler
    lateinit var updateSeekBar: Runnable

    /*--------------------------------------------------------------------------------------------*/


    companion object {
        fun newInstance() = RecordingsFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.recordings_fragment, container, false)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val path = activity?.getExternalFilesDir("/Recordings/")?.absolutePath
        val directory = File(path)
        allFiles = directory.listFiles()

        // Setting RecyclerView.....................................................................
        fileAdapter = AudioListAdapter(allFiles, this)
        recyclerView_files.setHasFixedSize(true)
        recyclerView_files.layoutManager = LinearLayoutManager(activity)
        recyclerView_files.adapter = fileAdapter

        // Setting Play/Pause Button Action.........................................................
        iv_play_pause_button.setOnClickListener {
            if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                pauseAudioPlayer()
            } else {
                resumeAudioPlayer()
            }
        }
    }

    /*----------------------------*** On FileList item clicked  ***-------------------------------*/
    override fun onItemClick(file: File, position: Int) {
        fileToPlay = file
        if (isPlaying) {
            // Stop player.....
            stopPlayingAudio()
            playAudio(fileToPlay!!)
        } else {
            // Start player.....
            playAudio(fileToPlay!!)
        }
    }

    /*----------------------------*** Stop the MediaPlaying  ***----------------------------------*/
    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    private fun stopPlayingAudio() {
        // Stop playing.......
        iv_play_pause_button.setImageDrawable(
            resources.getDrawable(
                R.drawable.ic_baseline_play_circle_filled,
                null
            )
        )
        textView_play_status.text = "Stopped"
        mediaPlayer?.stop()
        mediaPlayer = null
        isPlaying = false
    }


    /*----------------------------*** Start the MediaPlayer  ***----------------------------------*/
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun playAudio(fileToPlay: File) {
        // Start playing......
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(fileToPlay.absolutePath)
        mediaPlayer?.setOnCompletionListener {
            seekBar_player.progress = mediaPlayer!!.duration
            isPlaying = false
        }
        mediaPlayer?.prepare()
        mediaPlayer?.start()

        // Show file name to TextView in bottomSheet.........
        textView_playing_file_name.text = fileToPlay.name

        // Change play/pause button icon......
        iv_play_pause_button.setImageDrawable(
            resources.getDrawable(
                R.drawable.ic_baseline_pause_circle_filled,
                null
            )
        )
        textView_play_status.text = "Playing"

        // update seekBar........
        seekBar_player.max = mediaPlayer!!.duration
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (isPlaying) {
                    seekBar_player.progress = mediaPlayer!!.currentPosition
                } else {
                    timer.cancel()
                }
            }
        }, 0, 25)


        isPlaying = true

        mediaPlayer?.setOnCompletionListener {
            stopPlayingAudio()
            textView_play_status.text = "Finished"
        }
    }


    /*----------------------------*** Pause the MediaPlayer  ***----------------------------------*/
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun pauseAudioPlayer() {
        iv_play_pause_button.setImageDrawable(
            resources.getDrawable(
                R.drawable.ic_baseline_play_circle_filled,
                null
            )
        )
        mediaPlayer?.pause()
        isPlaying = false

    }

    /*----------------------------*** Resume the MediaPlayer  ***---------------------------------*/
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun resumeAudioPlayer() {
        if (fileToPlay != null) {
            iv_play_pause_button.setImageDrawable(
                resources.getDrawable(
                    R.drawable.ic_baseline_pause_circle_filled,
                    null
                )
            )
            mediaPlayer?.start()
            isPlaying = true
        }
    }

}