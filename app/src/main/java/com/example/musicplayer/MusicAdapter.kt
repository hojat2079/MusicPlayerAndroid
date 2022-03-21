package com.example.musicplayer

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.facebook.drawee.view.SimpleDraweeView

class MusicAdapter(
    private val musicList: ArrayList<Music>,
    private val callback: MusicItemCallback
) :
    RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {
    var positionMusic: Int = -1
    private var pause = false

    inner class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.findViewById<SimpleDraweeView>(R.id.img_artist_item)
        private val artist = itemView.findViewById<TextView>(R.id.txt_MusicArtist)
        private val name = itemView.findViewById<TextView>(R.id.txt_MusicName)
        private val animation = itemView.findViewById<LottieAnimationView>(R.id.lottieAnimationView)
        fun onBind(music: Music) {
            music.coverResId?.let { image.setActualImageResource(it) }
            name.text = music.name
            artist.text = music.artist
            if (positionMusic == adapterPosition) {
                if (!pause) {
                    animation.playAnimation()
                    Log.i(TAG, "resumeAnimation:+$adapterPosition ")
                } else {
                    animation.repeatCount = 0
                    Log.i(TAG, "pauseAnimation:+$adapterPosition  ")
                }
                animation.visibility = View.VISIBLE
            } else {
                animation.visibility = View.GONE
                Log.i(TAG, "StopAnimation:+$adapterPosition  ")

            }
            itemView.setOnClickListener {
                callback.onClick(music)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        return MusicViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_music, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.onBind(musicList[position])
    }

    override fun getItemCount(): Int = musicList.size

    interface MusicItemCallback {
        fun onClick(music: Music)

    }

    fun onMusicChangeListener(music: Music) {
        val index = musicList.indexOf(music)
        pause = false
        if (index != positionMusic) {
            notifyItemChanged(positionMusic)
            positionMusic = index
            notifyItemChanged(positionMusic)
        }
    }

    fun onAnimationClicked() {
        notifyItemChanged(positionMusic)
        pause = !pause
        notifyItemChanged(positionMusic)
    }

    companion object {
        private const val TAG = "MusicAdapter"
    }
}

