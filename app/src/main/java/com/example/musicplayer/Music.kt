package com.example.musicplayer

import java.util.*
import kotlin.collections.ArrayList

data class Music(
    val id: Int?,
    val name: String?,
    val artist: String?,
    val coverResId: Int?,
    val artistResId: Int?,
    val musicResId: Int?,
    var isFavorite: Boolean = false,
    var isPlaying: Boolean = false

) {
    constructor() : this(null, null, null, null, null, null, false, false)

    companion object {
        fun timeFormat(time: Int): String {
            val minute = time / 1000 / 60
            val second = time / 1000
            return String.format(Locale.US, "%02d:%02d", minute, second)
        }

        fun getList(): ArrayList<Music> {
            val listMusic = arrayListOf<Music>()
            val music1 =
                Music(
                    1,
                    "Chehel Gis",
                    "Evan Band",
                    R.drawable.chehel_gis,
                    R.drawable.cover_evan,
                    R.raw.chehlgis,
                    false, isPlaying = false
                )
            val music2 = Music(
                2,
                "Adame Sabegh",
                "Reza Bahram",
                R.drawable.adame,
                R.drawable.bahram,
                R.raw.adamesabesh,
                false, isPlaying = false
            )
            val music3 = Music(
                3,
                "Ghazi",
                "Shadmehr",
                R.drawable.ghazi_image,
                R.drawable.shadmehr_aghili,
                R.raw.ghazi,
                false, isPlaying = false
            )
            val music4 = Music(
                4,
                "Ashegh Ke Beshi",
                "Ehsan KhajeAmiri",
                R.drawable.ashegh_ehsan_img,
                R.drawable.ehsan,
                R.raw.ahseghkebeshi,
                false, isPlaying = false
            )
            val music5 = Music(
                5,
                "Shah Neshin Ghalbam",
                "Evan Band",
                R.drawable.shah,
                R.drawable.cover_evan,
                R.raw.shah,
                false, isPlaying = false
            )
            val music6 = Music(
                6,
                "Vaghti Ke Bad Misham",
                "Shadmehr",
                R.drawable.vaghti,
                R.drawable.shadmehr_aghili,
                R.raw.vaghti,
                false
            )
            listMusic.add(music1)
            listMusic.add(music2)
            listMusic.add(music3)
            listMusic.add(music4)
            listMusic.add(music5)
            listMusic.add(music6)
            return listMusic

        }

    }
}