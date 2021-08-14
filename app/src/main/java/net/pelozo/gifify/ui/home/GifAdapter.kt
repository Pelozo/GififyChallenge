package net.pelozo.gifify.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_gif.view.*
import net.pelozo.gifify.R
import net.pelozo.gifify.glideLoad
import net.pelozo.gifify.model.giphyApi.model.Gif


class GifAdapter(private val viewmodel: HomeViewModel):
    PagingDataAdapter<Gif, GifAdapter.ViewHolder>(COMPARATOR) {

    companion object{
        private val COMPARATOR = object: DiffUtil.ItemCallback<Gif>(){
            override fun areItemsTheSame(oldItem: Gif, newItem: Gif): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Gif, newItem: Gif): Boolean {
                return oldItem.id == newItem.id
                        && oldItem.title == newItem.title
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gif, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       getItem(position)?.let{ gif->

           holder.ivGif.glideLoad(gif.images.fixedHeight.url)
           holder.tvTitle.text = gif.title
           holder.root.setOnClickListener {viewmodel.gifClicked(gif)}
           holder.root.setOnLongClickListener{
               viewmodel.gifLongClicked(gif)
               return@setOnLongClickListener true
           }
       }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = itemView.rootView
        val ivGif: ImageView = itemView.findViewById(R.id.iv_gif)
        val tvTitle: TextView = itemView.tv_title
    }

    fun clear() {

    }

}