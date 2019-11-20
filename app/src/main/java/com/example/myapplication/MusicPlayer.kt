package com.example.myapplication



import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.squareup.picasso.Picasso
import org.json.JSONArray
//import sun.jvm.hotspot.utilities.IntArray
import java.net.HttpURLConnection
import java.net.URL


class MusicPlayer : AppCompatActivity() {

    private lateinit var mediaPlayer : MediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.music_player)
        val url_JSON = "http://starlord.hackerearth.com/studio"
        AsyncTaskHandleJson().execute(url_JSON)


    }

    inner class AsyncTaskHandleJson:AsyncTask<String,String,String>(){
        override fun doInBackground(vararg url:String ?):String{
            var text:String
            var connection = URL(url[0]).openConnection() as HttpURLConnection
            try {
                connection.connect()

                text = connection.inputStream.use { it.reader().use { reader->reader.readText() } }
            }
            finally {
                connection.disconnect()
            }
            return  text
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            handleJson(result)
        }



    }
    private fun handleJson(jsonString: String?) {


        val jsonArray = JSONArray(jsonString)
        val intent = intent
        val songId = intent.getIntExtra("song_id1", 1)
        val songAlbum = findViewById<AppCompatImageView>(R.id.song_album)
        val jsonObject = jsonArray.getJSONObject(songId)
        val urlCoverImage = jsonObject.getString("cover_image")
        val urlSong = jsonObject.getString("url")
        mediaPlayer = MediaPlayer.create(this, Uri.parse(urlSong))
        val totalTime = mediaPlayer.duration
        mediaPlayer.start()

        Picasso.get().load(urlCoverImage).into(songAlbum)


    }
    fun playBtnClick(v:View){
        val playBtn = findViewById<Button>(R.id.play_btn)
        if(mediaPlayer.isPlaying){
            mediaPlayer.pause()
            playBtn.setBackgroundResource(R.drawable.ic_launcher_foreground)
        }
        else{
            mediaPlayer.start()
            playBtn.setBackgroundResource(R.drawable.ic_launcher_background)
        }
    }

}

