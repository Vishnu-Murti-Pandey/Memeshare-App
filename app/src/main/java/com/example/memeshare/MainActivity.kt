package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {

    companion object {
        const val MEME_IMAGE_API_URL = "https://meme-api.herokuapp.com/gimme"
    }
    var currMemeUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()

        val shareButton = findViewById<Button>(R.id.shareButton)

        shareButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, "Hey, checkout this cool meme!! $currMemeUrl")
                val chooser = Intent.createChooser(intent, "share")

                startActivity(chooser)
            }
        })

        val nextButton = findViewById<Button>(R.id.nextButton)

        nextButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                loadMeme()
            }
        })

    }

    private fun loadMeme() {

        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        val textView = findViewById<TextView>(R.id.error_text_view)

        progressBar.visibility = View.VISIBLE
        textView.visibility = View.GONE


        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, MEME_IMAGE_API_URL, null,
            { response ->
                currMemeUrl = response.getString("url")
                val memeImage = findViewById<ImageView>(R.id.memeImageView)
                Glide.with(this).
                load(currMemeUrl).
                listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                }).
                into(memeImage)

            },
            { error ->
                progressBar.visibility = View.GONE
                textView.visibility = View.VISIBLE
            }
        )

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }

}