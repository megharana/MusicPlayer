package com.example.myapplication


import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // NEEDS-ATTENTION
        setContentView(R.layout.activity_main) // set the layout as in activity_main.xml
        val url = "http://starlord.hackerearth.com/studio"
        AsyncTaskHandleJson().execute(url) // make a get request to the specified url

        songs_list.setOnItemClickListener { _, _, position, _ ->
            Toast.makeText(this,"Clicked Item : $position", Toast.LENGTH_SHORT).show()


            val intent = Intent(this, MusicPlayer::class.java) // send info across pages
            intent.putExtra("song_id1", position.toInt())

            startActivity(intent)



        }

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
            onReceivingSongList(result)
        }



    }
    private fun onReceivingSongList(customSongsJSONList: String?) {
        val customSongJSONArray = JSONArray(customSongsJSONList)

        val customSongList = ArrayList<Song>()
        var customSongListIterator = 0

        while (customSongListIterator < customSongJSONArray.length()) {
            val customSongJsonObject = customSongJSONArray.getJSONObject(customSongListIterator)

            customSongList.add(
                Song(
                        customSongJsonObject.getString("song"),
                        customSongJsonObject.getString("artists"),
                        customSongJsonObject.getString("cover_image")


                )
            )


            customSongListIterator++

        }

        val adapter = ListAdapter(this, customSongList)
        songs_list.adapter = adapter




    }

}
