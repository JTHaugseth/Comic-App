package com.example.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app.Models.Comic

// 3rd party library Picasso for loading images
import com.squareup.picasso.Picasso

class ComicAdapter(private val comics: MutableList<Comic>) : RecyclerView.Adapter<ComicAdapter.ComicViewHolder>() {

    // ViewHolder class for each individual comic-item in the RecyclerView
    class ComicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.comicItemImage)
        val titleView: TextView = view.findViewById(R.id.comicItemTitle)
        val dateView: TextView = view.findViewById(R.id.comicItemDate)
        val comicNumView: TextView = view.findViewById(R.id.comicItemNumber)
    }

    // Method to create new ViewHolder-instances
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_comic, parent, false)
        return ComicViewHolder(view)
    }

    // Method to bind our comic data to the ViewHolder
    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        val comic = comics[position]
        holder.titleView.text = comic.title
        holder.dateView.text = "${comic.day}.${comic.month}.${comic.year}"
        holder.comicNumView.text = "Comic #${comic.num}"
        Picasso.get().load(comic.img).into(holder.imageView)
    }

    // Method that returns the amount of items in the RecyclerView
    override fun getItemCount() = comics.size
}