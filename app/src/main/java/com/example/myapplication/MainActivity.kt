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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val url = "http://starlord.hackerearth.com/studio"
        AsyncTaskHandleJson().execute(url)

        songs_list.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this,"Clicked Item : $position", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MusicPlayer::class.java)
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
            handleJson(result)
        }



    }
    private fun handleJson(jsonString: String?) {
        val jsonArray = JSONArray(jsonString)

        val list = ArrayList<Song>()
        var x = 0

        while (x < jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(x)

            list.add(
                Song(
                    jsonObject.getString("song"),
                    jsonObject.getString("artists")

                )
            )


            x++

        }

        val adapter = ListAdapter(this, list)
        songs_list.adapter = adapter




    }

}
