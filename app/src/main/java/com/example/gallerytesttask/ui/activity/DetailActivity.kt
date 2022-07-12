package com.example.gallerytesttask.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.gallerytesttask.R
import com.example.gallerytesttask.databinding.ActivityDetailBinding
import com.example.gallerytesttask.ui.fragment.ImageFragment
import com.example.gallerytesttask.ui.fragment.VideoFragment
import com.example.gallerytesttask.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var uri = ""

    val viewModel by viewModels<DetailViewModel>()

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
        with(binding) {
            cardViewShare.setOnClickListener {
                shareImage()
            }
            cardViewSave.setOnClickListener {
                saveImage()
            }
            cardViewDelete.setOnClickListener {
                deleteImage()
            }
        }
    }

    private val intentSender =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if (it.resultCode == RESULT_OK) {
                binding.cardViewDelete.visibility=View.GONE
                finish()
            }
        }

    private fun deleteImage() {
        viewModel.deleteImage(uri) { uriID ->
            if (uriID != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val uriList: MutableList<Uri> = ArrayList()
                    uriList.add(uriID)
                    val pendingIntent =
                        MediaStore.createDeleteRequest(this.contentResolver, uriList)
                    val intentSenderRequest = IntentSenderRequest
                        .Builder(pendingIntent).build()
                    intentSender.launch(intentSenderRequest)
                } else {
                    val delete = this.contentResolver.delete(uriID, null, null)
                }
            }
        }
    }

    private fun saveImage() {
        val bitmap = viewModel.getBitmap(uri)
        val saveImage = bitmap?.let { viewModel.saveImage(UUID.randomUUID().toString(), it) }
        if (saveImage == true) binding.cardViewSave.visibility = View.GONE
    }

    private fun shareImage() {
        val contentUri = viewModel.getContentUri(uri)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/png"
        intent.putExtra(Intent.EXTRA_STREAM, contentUri)
        val chooser = Intent.createChooser(intent, "Share File")

        val resInfoList =
            this.packageManager.queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)

        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            grantUriPermission(
                packageName,
                contentUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }

        startActivity(chooser)
    }

    private fun getIntentData() {
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