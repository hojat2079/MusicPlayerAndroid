package com.example.musicplayer

import android.icu.text.Transliterator
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.BaseTransientBottomBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.icon_playList_main
import kotlinx.android.synthetic.main.activity_main.txt_PlayList_main
import kotlinx.android.synthetic.main.bottom_shit_layout.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), MusicAdapter.MusicItemCallback {
    private var mediaPlayer: MediaPlayer? = null
    private var isDragging: Boolean = false
    private var typePlay: Int = 0
    private var cursor: Int = 0
    private lateinit var timer: Timer
    private lateinit var musicAdapter: MusicAdapter
    private var musicPLayList: MusicPlayList = MusicPlayList.SHUFFLE
    private lateinit var musicList: ArrayList<Music>
    private lateinit var musicListBottomShit: MusicListBottomShit
    private var musicState = MusicState.STOPPING
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        Fresco.initialize(this)
        setContentView(R.layout.activity_main)
        musicList = Music.getList()
        musicAdapter = MusicAdapter(musicList, this)
        txt_PlayList_main.setOnClickListener {
            onBindBottomShit()

        }
        btn_shuffle_player.setOnClickListener {
            when (typePlay) {
                0 -> {
                    typePlay++
                    btn_shuffle_player.setImageResource(R.drawable.ic_baseline_repeat_24)
                    musicPLayList = MusicPlayList.REPEAT
                }
                1 -> {
                    typePlay++
                    btn_shuffle_player.setImageResource(R.drawable.ic_baseline_repeat_one_24)
                    musicPLayList = MusicPlayList.REPEAT_ONE
                }
                2 -> {
                    typePlay++
                    btn_shuffle_player.setImageResource(R.drawable.ic_baseline_sort_by_alpha_24)
                    musicPLayList = MusicPlayList.SORTED
                }
                3 -> {
                    typePlay = 0
                    btn_shuffle_player.setImageResource(R.drawable.ic_baseline_shuffle_24)
                    musicPLayList = MusicPlayList.SHUFFLE
                }
            }
        }
        icon_playList_main.setOnClickListener {
            onBindBottomShit()
        }
        onMusicChanged(musicList[cursor])
        btn_like_player.setOnClickListener {
            if (!musicList[cursor].isFavorite) {
                btn_like_player.setImageResource(R.drawable.ic_baseline_favorite_border_red_24)
                musicList[cursor].isFavorite = true
            } else {
                btn_like_player.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                musicList[cursor].isFavorite = false
            }
        }
        btn_next_player.setOnClickListener {
            onNext(it)
        }
        btn_previous_player.setOnClickListener {
            onPrevious()
        }
//        btn_previous_player.setOnLongClickListener {
//            if (mediaPlayer?.currentPosition!! > 2000) {
//                mediaPlayer?.seekTo(mediaPlayer!!.currentPosition - 2000)
//            } else mediaPlayer?.seekTo(0)
//            slider_music_play.value = mediaPlayer?.currentPosition!!.toFloat()
//            return@setOnLongClickListener false
//        }
//        btn_next_player.setOnLongClickListener {
//            if (mediaPlayer?.currentPosition!! < mediaPlayer?.currentPosition!! - 2000) {
//                mediaPlayer?.seekTo(mediaPlayer!!.currentPosition + 2000)
//            } else mediaPlayer?.seekTo(mediaPlayer?.duration!!)
//            slider_music_play.value = mediaPlayer?.currentPosition!!.toFloat()
//            return@setOnLongClickListener false
//        }
        btn_play_player.setOnClickListener {
            musicState = if (musicState == MusicState.PLAYING) {
                mediaPlayer?.pause()
                btn_play_player.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                MusicState.PAUSED
            } else {
                mediaPlayer?.start()
                btn_play_player.setImageResource(R.drawable.ic_pause)
                MusicState.PLAYING
            }
            musicAdapter.onAnimationClicked()
        }
        slider_music_play.addOnChangeListener { _, value, _ ->
            txt_current_play.text = Music.timeFormat(value.toInt())
        }
        slider_music_play.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                isDragging = true
            }

            override fun onStopTrackingTouch(slider: Slider) {
                isDragging = false
                mediaPlayer?.seekTo(slider.value.toInt())
            }

        })


    }

    private fun onPrevious() {
        timer.cancel()
        timer.purge()
        mediaPlayer!!.release()
        when (musicPLayList) {
            MusicPlayList.SHUFFLE -> {
                onRandomPlayList()
                onMusicChanged(musicList[cursor])
            }
            else -> {
                if (cursor > 0)
                    cursor--
                else cursor = musicList.size - 1
                onMusicChanged(musicList[cursor])
            }
        }

    }

    private fun onRandomPlayList() {
        var random: Int
        do {
            random = (0 until musicList.size).random()
        } while (random == cursor)
        cursor = random
    }

    private fun onNext(view: View?) {
        Log.i(TAG, "onNext: ")
        timer.cancel()
        timer.purge()
        mediaPlayer!!.release()
        when (musicPLayList) {
            MusicPlayList.SHUFFLE -> {
                onRandomPlayList()
                onMusicChanged(musicList[cursor])

            }
            (MusicPlayList.SORTED) -> {
                onSortedPlayList()
                onMusicChanged(musicList[cursor])
            }
            MusicPlayList.REPEAT -> {
                if (view != null)
                    onSortedPlayList()
                onMusicChanged(musicList[cursor])
            }
            else -> {
                if (view != null) {
                    onSortedPlayList()
                    onMusicChanged(musicList[cursor])
                } else {
                    onMusicChanged(musicList[cursor])
                    musicPLayList = MusicPlayList.SORTED

                }
            }
        }


    }

    private fun onSortedPlayList() {
        if (cursor < musicList.size - 1)
            cursor++
        else cursor = 0

    }

    private fun onBindBottomShit() {
        musicListBottomShit = MusicListBottomShit(musicAdapter)
        musicListBottomShit.setStyle(BottomSheetDialogFragment.STYLE_NORMAL,R.style.CustomBottomSheetDialogTheme)
        musicListBottomShit.show(supportFragmentManager, null)
    }

    private fun onMusicChanged(music: Music) {
        musicAdapter.onMusicChangeListener(music)
        Log.i(TAG, "onMusicChanged: ")
        mediaPlayer = music.musicResId?.let { MediaPlayer.create(this, it) }!!
        mediaPlayer!!.setOnPreparedListener {
            mediaPlayer!!.start()
            timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    runOnUiThread {
                        if (!isDragging) {
                            slider_music_play.value = mediaPlayer?.currentPosition!!.toFloat()
                        }
                    }
                }

            }, 1000, 1000)
            txt_total_play.text = Music.timeFormat(it.duration)
            musicState = MusicState.PLAYING
            btn_play_player.setImageResource(R.drawable.ic_pause)
            slider_music_play.valueTo = mediaPlayer!!.duration.toFloat()
            mediaPlayer?.setOnCompletionListener {
                onNext(null)
            }
        }

        music.artistResId?.let { label_artist_player.setActualImageResource(it) }
        artist_music_player.text = music.artist
        music.coverResId?.let { cover_music_player.setActualImageResource(it) }
        title_music_player.text = music.name
        music.isPlaying = true
        if (music.isFavorite)
            btn_like_player.setImageResource(R.drawable.ic_baseline_favorite_border_red_24)
        else btn_like_player.setImageResource(R.drawable.ic_baseline_favorite_border_24)

    }

    override fun onClick(music: Music) {
        musicListBottomShit.dismiss()
        if (musicList[cursor] != music) {
            timer.cancel()
            timer.purge()
            mediaPlayer?.release()
            onMusicChanged(music)
            cursor = musicList.indexOf(music)
        }
    }

    enum class MusicState {
        PLAYING, STOPPING, PAUSED
    }

    enum class MusicPlayList {
        SHUFFLE, REPEAT, REPEAT_ONE, SORTED
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy: ")
        timer.cancel()
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}