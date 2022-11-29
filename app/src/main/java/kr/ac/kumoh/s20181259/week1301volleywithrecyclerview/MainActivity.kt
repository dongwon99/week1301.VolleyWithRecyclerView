package kr.ac.kumoh.s20181259.week1301volleywithrecyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.ac.kumoh.s20181259.week1301volleywithrecyclerview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var model: SongViewModel
    private val songAdap = SongAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this)[SongViewModel::class.java]
        model.requestSong()

        binding.list.apply {
            adapter = songAdap
            layoutManager = LinearLayoutManager(applicationContext)
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
        }
        model.list.observe(this){
            songAdap.notifyItemRangeInserted(0, model.list.value?.size ?: 0)
        }
    }

    inner class SongAdapter: RecyclerView.Adapter<SongAdapter.ViewHolder>(){
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            //안드로이드에서 주는 레이아웃
            //이제 우리가 만든 레이아웃을 쓸거기 때문에 그때는 android 뺄 것
            val title = itemView.findViewById<TextView>(android.R.id.text1)
            val singer = itemView.findViewById<TextView>(android.R.id.text2)
        }

        //객체를 세팅
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            //layout을 저거를 읽어오겠다
            val view = layoutInflater.inflate(android.R.layout.simple_list_item_2, parent, false)
            //inflation 한 view를 위의 viewHolder에게 넘겨줌
            //뷰홀더는
            return ViewHolder(view)
        }

        //값을 셋팅
        //가장 처음에 리스트 아이템을 xml 파일에서 정보를 읽어와서 메모리 내의 객체로 만드는 것
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.title.text = model.list.value?.get(position)?.title.toString()
            holder.singer.text = model.list.value?.get(position)?.singer.toString()
        }

        override fun getItemCount() = model.list.value?.size ?: 0
    }
}