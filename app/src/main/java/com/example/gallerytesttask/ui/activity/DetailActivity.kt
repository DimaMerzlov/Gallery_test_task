package com.example.gallerytesttask.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.gallerytesttask.R
import com.example.gallerytesttask.databinding.ActivityDetailBinding
import com.example.gallerytesttask.ui.fragment.ImageFragment
import com.example.gallerytesttask.ui.fragment.VideoFragment

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_detail
        )
        setUpActionBar()
        getIntentData()
        observeClick()

    }

    private fun observeClick() {
        with(binding){
            cardViewShare.setOnClickListener {

            }
        }
    }

    private fun getIntentData() {
        var uri = ""
        var type = ""
        if (intent.getStringExtra(ImageFragment.IMAGE_URI)?.isNotEmpty() == true) {
            type = "Image"
            uri = intent.getStringExtra(ImageFragment.IMAGE_URI)!!
        } else if (intent.getStringExtra(VideoFragment.VIDEO_URI)?.isNotEmpty() == true) {
            type = "Video"
            uri = intent.getStringExtra(VideoFragment.VIDEO_URI)!!
        }

        binding.type = type
        Glide.with(this)
            .load(uri)
            .into(binding.ivDetail)
    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_arrow);//your icon here
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show()
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.premium_menu, menu)
        return true
    }
}