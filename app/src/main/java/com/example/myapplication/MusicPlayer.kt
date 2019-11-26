package com.example.myapplication



import android.media.MediaPlayer
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.squareup.picasso.Picasso
import org.json.JSONArray
//import sun.jvm.hotspot.utilities.IntArray
import java.net.HttpURLConnection
import java.net.URL
import android.widget.SeekBar
import android.widget.TextView


class MusicPlayer : AppCompatActivity() {

    private lateinit var mediaPlayer : MediaPlayer
    private lateinit var seekProgress : SeekBar
    private lateinit var runnable:Runnable
    private var handler: Handler = Handler()
    var songId:Int = 0
    var resultString:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.music_player)
        val url_JSON = "http://starlord.hackerearth.com/studio"
        AsyncTaskHandleJson().execute(url_JSON)



    }

    override fun onBackPressed() {
        super.onBackPressed()
//        mediaPlayer.stop()


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
            resultString = result
            val intent = intent
            songId = intent.getIntExtra("song_id1", 1)
            handleJson(resultString,songId)
        }



    }
    private fun handleJson(jsonString: String?, songId: Int) {


        val jsonArray = JSONArray(jsonString)


        val songAlbum = findViewById<AppCompatImageView>(R.id.song_album)
        val jsonObject = jsonArray.getJSONObject(songId)
        val urlCoverImage = jsonObject.getString("cover_image")
        val urlSong = jsonObject.getString("url")
        mediaPlayer = MediaPlayer.create(this, Uri.parse(urlSong))
        mediaPlayer.start()
        initializeSeekBar()
        Picasso.get().load(urlCoverImage).into(songAlbum)
        seekProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b) {
                    mediaPlayer.seekTo(i)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })



    }
    fun playBtnClick(v:View){
        val playBtn = findViewById<Button>(R.id.play_btn)
        if(mediaPlayer.isPlaying){
            mediaPlayer.pause()

            playBtn.setBackgroundResource(R.drawable.play_btn_image)
        }
        else{
            mediaPlayer.start()

            playBtn.setBackgroundResource(R.drawable.pause_btn_image)
        }
    }
    fun prevBtnClick(v:View){
        mediaPlayer.reset()
        handleJson(resultString, songId-1)
    }
    fun nextBtnClick(v:View){
        mediaPlayer.reset()
        handleJson(resultString, songId+1)
    }



    private fun initializeSeekBar() {
        seekProgress = findViewById(R.id.song_progress)
        seekProgress.max = mediaPlayer.duration
        val startText = findViewById<TextView>(R.id.start)
        val stopText = findViewById<TextView>(R.id.end)
        runnable = Runnable {
            seekProgress.progress = mediaPlayer.currentPosition

            startText.text = "${mediaPlayer.currentSeconds/60}:${(mediaPlayer.currentSeconds)%60}"

            stopText.text = "${mediaPlayer.seconds/60}:${(mediaPlayer.seconds)%60}"


            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }
}




// Extension property to get media player duration in seconds
val MediaPlayer.seconds: Int
    get() {
        return (this.duration / 1000)
    }


// Extension property to get media player current position in seconds
val MediaPlayer.currentSeconds: Int
    get() {
        return (this.currentPosition / 1000)
    }

