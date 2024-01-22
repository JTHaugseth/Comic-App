package com.example.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.models.Comic
import com.example.app.service.ComicService

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var comicAdapter: ComicAdapter
    private val comics = mutableListOf<Comic>()
    private var currentPageStart = 1
    private val comicService = ComicService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Here we initialize the RecyclerView and ComicAdapter, then we set the RecyclerView.adapter to our custom adapter and define the layoutManager
        recyclerView = findViewById(R.id.recyclerViewMain)
        comicAdapter = ComicAdapter(comics)
        recyclerView.adapter = comicAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Then we load our comics!
        loadComics(currentPageStart)

        // The Prev Page Button clears the comics list, decreases our variable, runs loadComics again and notifies our comicAdapter of the change.
        findViewById<Button>(R.id.prevPageButton).setOnClickListener {
            if (currentPageStart > 1) {
                comics.clear()
                currentPageStart -= 10
                loadComics(currentPageStart)
                comicAdapter.notifyDataSetChanged()
            }
        }

        // The Next Page Button clears the comics list, increases our variable, runs loadComics again and notifies our comicAdapter of the change.
        findViewById<Button>(R.id.nextPageButton).setOnClickListener {
            comics.clear()
            currentPageStart += 10
            loadComics(currentPageStart)
            comicAdapter.notifyDataSetChanged()
        }
    }

    // Runs the loadComics method from the ComicService. Since it has a Callback function it either shows a Toast message to the user on error, or adds all the comics
    // to the comics-list, and notifies the comicAdapter about the change.
    private fun loadComics(startComicNumber: Int) {
        comicService.loadComics(startComicNumber) { comicsList, error ->
            runOnUiThread {
                if (error != null) {
                    Toast.makeText(this@MainActivity, "Error loading comics: ${error.message}", Toast.LENGTH_LONG).show()
                } else if (comicsList != null) {
                    comics.clear()
                    comics.addAll(comicsList)
                    comicAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}