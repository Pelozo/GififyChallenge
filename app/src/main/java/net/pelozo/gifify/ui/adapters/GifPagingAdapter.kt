package net.pelozo.gifify.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_gif.view.*
import net.pelozo.gifify.R
import net.pelozo.gifify.glideLoad
import net.pelozo.gifify.model.Gif


class GifPagingAdapter(private val listener: GifListener):
    PagingDataAdapter<Gif, GifPagingAdapter.ViewHolder>(COMPARATOR) {

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


    interface GifListener{
        fun onGifClicked(gif: Gif)
        fun onGifLongClicked(gif: Gif)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gif, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       getItem(position)?.let{ gif->

           holder.ivGif.glideLoad(gif.urlImageDownsized)
           holder.tvTitle.text = gif.title
           holder.root.setOnClickListener {listener.onGifClicked(gif)}
           holder.root.setOnLongClickListener{
               listener.onGifLongClicked(gif)
               return@setOnLongClickListener true
           }

           holder.root.animation =
               AnimationUtils.loadAnimation(holder.itemView.context, R.anim.anim_recycler_item_in)
       }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root: View = itemView.rootView
        val ivGif: ImageView = itemView.findViewById(R.id.iv_gif)
        val tvTitle: TextView = itemView.tv_title
    }

}