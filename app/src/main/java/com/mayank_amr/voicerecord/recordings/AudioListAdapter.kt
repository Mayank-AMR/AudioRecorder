package com.mayank_amr.voicerecord.recordings

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mayank_amr.voicerecord.R
import kotlinx.android.synthetic.main.single_list_item_view.view.*
import java.io.File
import java.text.SimpleDateFormat

/**
 * @Project Voice Record
 * @Created_by Mayank Kumar on 07-02-2021 08:04 PM
 */
class AudioListAdapter(
    private val recordingArrayList: Array<File>,
    private var onListItemClickListener: AudioListAdapter.OnListIemClickListener

) : RecyclerView.Adapter<AudioListAdapter.AudioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_list_item_view, parent, false)
        return AudioViewHolder(view)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onListItemClickListener.onItemClick(recordingArrayList[position], position)
        }

        holder.itemView.imageView_list
        holder.itemView.textView_list_file_name.text = recordingArrayList[position].name

        val lastModified =
            SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(recordingArrayList[position].lastModified())
        holder.itemView.textView_file_date.text = lastModified
    }

    override fun getItemCount() = recordingArrayList.size


    /*----------------------------*** ViewHolder Class  ***---------------------------------------*/
    class AudioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    /*----------------------------*** interface to listen list item click  ***--------------------*/
    interface OnListIemClickListener {
        fun onItemClick(file: File, position: Int)

    }

}