package com.example.app

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.models.Comic
import com.example.app.service.ComicService

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var comicAdapter: ComicAdapter
    private val comics = mutableListOf<Comic>()
    private val latestComicNum = 2884
    private var currentPageStart = 1
    private val comicService = ComicService()

    private lateinit var comicsLoadedText: TextView
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: ImageView
    private lateinit var openSearchButton: ImageView

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

        comicsLoadedText = findViewById(R.id.comicsLoadedText)
        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)
        openSearchButton = findViewById(R.id.openSearchButton)

        // Hides the search icon and sets a copy (searchButton) and input text field to visible
        openSearchButton.setOnClickListener {
            openSearchButton.visibility = View.GONE
            searchEditText.visibility = View.VISIBLE
            searchButton.visibility = View.VISIBLE
        }

        // Takes the input value from the search-field and uses the loadComics method.
        searchButton.setOnClickListener {
            val searchNumber = searchEditText.text.toString().toIntOrNull()
            if ((searchNumber != null) && (searchNumber >= 1) && (searchNumber <= latestComicNum)) {
                loadComics(searchNumber)
                currentPageStart = searchNumber
                searchEditText.setText("")
                searchEditText.visibility = View.GONE
                searchButton.visibility = View.GONE
                openSearchButton.visibility = View.VISIBLE

                // After typing in a number and clicking the search icon again this will close the keyboard
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(searchEditText.windowToken, 0)
            } else {
                Toast.makeText(this, "Please enter a comic number between 1 and $latestComicNum", Toast.LENGTH_SHORT).show()
            }
        }

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
                    comics.sortBy { it.num }
                    comicAdapter.notifyDataSetChanged()

                    // Updating the comics-loaded text above the RecyclerView
                    comicsLoadedText.text = "Comics: $startComicNumber -> ${(startComicNumber + comics.size) - 1}"
                }
            }
        }
    }
}