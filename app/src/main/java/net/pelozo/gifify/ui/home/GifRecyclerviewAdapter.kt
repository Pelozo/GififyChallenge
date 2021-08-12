package net.pelozo.gifify.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import net.pelozo.gifify.R
import net.pelozo.gifify.glideLoad
import net.pelozo.gifify.model.giphyApi.model.Gif


class GifRecyclerviewAdapter(private val gifs: List<Gif>, val viewmodel: HomeViewModel): RecyclerView.Adapter<GifRecyclerviewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gif, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = gifs[position].title
        holder.imageview.glideLoad(gifs[position].images.fixedHeight.url)
        holder.root.setOnClickListener { viewmodel.gifClicked(gifs[position])}

        holder.root.setOnLongClickListener{
            viewmodel.gifLongClicked(gifs[position])
            return@setOnLongClickListener true
        }
        //holder.root.setOnLongClickListener {viewmodel.gifLongClicked(gifs[position]).let { true }}

    }

    override fun getItemCount() = gifs.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val root: CardView = itemView.findViewById(R.id.card_view)
        val title: TextView = itemView.findViewById(R.id.tv_title)
        val imageview: ImageView = itemView.findViewById(R.id.iv_gif)
    }
}