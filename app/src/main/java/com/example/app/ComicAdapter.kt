package com.example.app

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app.models.Comic

// 3rd party library Picasso for loading images
import com.squareup.picasso.Picasso

class ComicAdapter(private val comics: MutableList<Comic>) : RecyclerView.Adapter<ComicAdapter.ComicViewHolder>() {

    // ViewHolder class for each individual comic-item in the RecyclerView
    class ComicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.comicItemImage)
        val titleView: TextView = view.findViewById(R.id.comicItemTitle)
        val dateView: TextView = view.findViewById(R.id.comicItemDate)
        val comicNumView: TextView = view.findViewById(R.id.comicItemNumber)
        val transcriptView: TextView = view.findViewById(R.id.comicItemTranscript)
        val readMoreButton: TextView = view.findViewById(R.id.comicItemReadMoreButton)
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

        // We don't want the whole transcript unless the user wants to see it, so we only show the limited one (only 100 characters)
        val limitedTranscript = getLimitedTranscript(comic.transcript, 100)
        holder.transcriptView.text = limitedTranscript

        // If the user clicks on an image, they pass the image-url as an Intent to the FullScreenImageActivity and starts the activity.
        holder.imageView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, FullscreenImageActivity::class.java)
            intent.putExtra("IMAGE_URL", comic.img)
            context.startActivity(intent)
        }

        // The user can toggle on or off the full transcript
        holder.readMoreButton.setOnClickListener {
            if (holder.readMoreButton.text == "Read More") {
                holder.transcriptView.text = comic.transcript
                holder.readMoreButton.text = "Read Less"
            } else {
                holder.transcriptView.text = limitedTranscript
                holder.readMoreButton.text = "Read More"
            }
        }
    }

    // Method that returns the amount of items in the RecyclerView
    override fun getItemCount() = comics.size

    // Short method that only returns the 100 first characters of the transcript if its longer than the charLimit (100).
    private fun getLimitedTranscript(fullTranscript: String, charLimit: Int): String {
        return if (fullTranscript.length > charLimit) {
            fullTranscript.substring(0, charLimit) + "..."
        } else {
            fullTranscript
        }
    }
}