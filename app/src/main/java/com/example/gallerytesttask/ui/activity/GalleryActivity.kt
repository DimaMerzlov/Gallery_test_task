package com.example.gallerytesttask.ui.activity

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.example.gallerytesttask.R
import com.example.gallerytesttask.adapter.viewpager.ViewPagerGalleryAdapter
import com.example.gallerytesttask.databinding.ActivityGalleryBinding
import com.example.gallerytesttask.model.GalleryItem
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GalleryActivity : AppCompatActivity() {

    lateinit var binding: ActivityGalleryBinding
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_gallery
        )
        tabLayout = binding.tabLayout
        viewPager = binding.viewPager
        initViewPager()
        setUpActionBar()
    }

    private fun initViewPager() {
        viewPager.adapter = ViewPagerGalleryAdapter(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, index ->
            tab.text = when (index) {
                0 -> "Photos"
                1 -> "Videos"
                else -> throw Resources.NotFoundException("Position not found")
            }
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.premium_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    private fun setUpActionBar(){
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_arrow);//your icon here
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}