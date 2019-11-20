package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.widget.AppCompatTextView

class ListAdapter(val context: Context, val list:ArrayList<Song>): BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.row_layout, parent, false)


        val songName = view.findViewById(R.id.song_name) as AppCompatTextView
        val songArtists = view.findViewById(R.id.artists) as AppCompatTextView



        songName.text = list[position].name
        songArtists.text = list[position].artist_name

        return view


    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}