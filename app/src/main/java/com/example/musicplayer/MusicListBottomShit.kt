package com.example.musicplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MusicListBottomShit(
/*    private val listMusic: ArrayList<Music>,
    private val musicItemCallback: MusicAdapter.MusicItemCallback*/
    private val musicAdapter: MusicAdapter
) :
    BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.bottom_shit_layout, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_playList)
        recyclerView.adapter = musicAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        return view
    }


}