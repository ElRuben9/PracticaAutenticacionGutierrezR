package com.example.practicaautenticaciongutierrezr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import coil.load
import com.example.practicaautenticaciongutierrezr.model.Artist
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class HomeActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var llArtists: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        llArtists = findViewById(R.id.llArtists)

        findViewById<TextView>(R.id.tvEmail).text =
            auth.currentUser?.email ?: getString(R.string.no_email)

        findViewById<Button>(R.id.btnAddArtist).setOnClickListener { addRandomArtist() }

        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            auth.signOut()
            Toast.makeText(this, R.string.logout_success, Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }

        loadArtists()
    }

    private fun loadArtists() {
        db.collection(COLLECTION_ARTISTS)
            .get()
            .addOnSuccessListener { result ->
                val artists = result.documents.mapNotNull { snapshot ->
                    snapshot.toObject(Artist::class.java)
                }
                renderArtists(artists)
            }
            .addOnFailureListener { error ->
                Log.e(TAG, "Error al leer artistas", error)
                Toast.makeText(this, R.string.artists_load_error, Toast.LENGTH_LONG).show()
            }
    }

    private fun renderArtists(artists: List<Artist>) {
        llArtists.removeAllViews()

        findViewById<TextView>(R.id.tvEmptyArtists).visibility =
            if (artists.isEmpty()) View.VISIBLE else View.GONE

        artists.forEach { artist ->
            val view = layoutInflater.inflate(R.layout.item_artist, llArtists, false)
            val ivArtist = view.findViewById<ImageView>(R.id.ivArtist)
            val tvArtistName = view.findViewById<TextView>(R.id.tvArtistName)

            tvArtistName.text = artist.name.orEmpty()
            ivArtist.contentDescription = artist.name.orEmpty()

            if (artist.image.isNullOrBlank()) {
                ivArtist.setImageResource(R.drawable.ic_spotify)
            } else {
                ivArtist.load(artist.image) {
                    placeholder(R.drawable.ic_spotify)
                    error(R.drawable.ic_spotify)
                }
            }

            llArtists.addView(view)
        }
    }

    private fun addRandomArtist() {
        val random = Random.nextInt(1, 10_001)
        val artist = Artist(
            name = "Random $random",
            description = "Descripción random número $random",
            image = SAMPLE_IMAGE_URL,
            songs = emptyList(),
        )

        db.collection(COLLECTION_ARTISTS)
            .add(artist)
            .addOnSuccessListener {
                Toast.makeText(this, R.string.artist_added, Toast.LENGTH_SHORT).show()
                loadArtists()
            }
            .addOnFailureListener { error ->
                Log.e(TAG, "Error al agregar artista", error)
                Toast.makeText(this, R.string.artist_add_error, Toast.LENGTH_LONG).show()
            }
    }

    companion object {
        private const val TAG = "HomeActivity"
        private const val COLLECTION_ARTISTS = "artists"
        private const val SAMPLE_IMAGE_URL =
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTwyXeKDN29AmZgZPLS7n0Bepe8QmVappBwZCeA3XWEbWNdiDFB"
    }
}
