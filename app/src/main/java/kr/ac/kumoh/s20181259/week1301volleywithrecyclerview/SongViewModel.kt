package kr.ac.kumoh.s20181259.week1301volleywithrecyclerview

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject


//data class 쓰는법
class SongViewModel(application: Application) : AndroidViewModel(application) {
    //struct 느낌, 필드를 private로 풀어둠.
    data class Song(var id:Int, var title:String, var singer:String)

    companion object{
        const val QUEUE_TAG = "SongVolleyRequest"
    }

    private val songs = ArrayList<Song>()
    private val _list = MutableLiveData<ArrayList<Song>>()
    val list: LiveData<ArrayList<Song>>
        get() = _list

    private val queue: RequestQueue
    fun requestSong(){
        val request = JsonArrayRequest(
            Request.Method.GET,
            "https://sapppro-yhfyc.run.goorm.io/song",
            null,
            {
                songs.clear()
                parseJson(it)
                _list.value = songs
            },
            {Toast.makeText(getApplication(), "$it", Toast.LENGTH_LONG).show()}
        )
        request.tag = QUEUE_TAG
        queue.add(request)
    }
    override fun onCleared(){
        super.onCleared()
        queue.cancelAll(QUEUE_TAG)
    }
    private fun parseJson(items: JSONArray){
        for(i in 0 until items.length()){
            val item = items[i] as JSONObject
            val id = item.getInt("id")
            val title = item.getString("title")
            val singer = item.getString("singer")

            songs.add(Song(id, title, singer))
        }
    }

    init {
        _list.value = songs
        queue = Volley.newRequestQueue(getApplication())
    }
}