package net.pelozo.gifify.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_gif.view.*
import net.pelozo.gifify.R
import net.pelozo.gifify.glideLoad
import net.pelozo.gifify.model.Gif


class GifAdapter(private var items: List<Gif>, private val listener: GifListener): RecyclerView.Adapter<GifAdapter.ViewHolder>() {

    interface GifListener{
        fun onGifClicked(gif: Gif)
        fun onGifLongClicked(gif: Gif)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gif, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        items[position].let { gif ->
            holder.ivGif.glideLoad(gif.urlImageDownsized)
            holder.tvTitle.text = gif.title
            holder.root.setOnClickListener { listener.onGifClicked(gif) }
            holder.root.setOnLongClickListener{
                listener.onGifLongClicked(gif)
                return@setOnLongClickListener true
            }

        }

    }

    fun setData(newItems: List<Gif>) {
        items = newItems
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root: View = itemView.rootView
        val ivGif: ImageView = itemView.findViewById(R.id.iv_gif)
        val tvTitle: TextView = itemView.tv_title
    }

    override fun getItemCount() = items.size


}