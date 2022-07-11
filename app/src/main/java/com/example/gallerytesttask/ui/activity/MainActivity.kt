package com.example.gallerytesttask.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.gallerytesttask.BuildConfig
import com.example.gallerytesttask.R
import com.example.gallerytesttask.databinding.ActivityMainBinding
import com.example.gallerytesttask.showGalleryDialog
import com.example.gallerytesttask.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private val viewModel by viewModels<MainViewModel>()


    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    viewModel.isCheckPermission(checkPermission())
                } else {
                    viewModel.isCheckPermission(checkPermission())
                }
            } else {
                //android is below R
            }
        }

    private companion object {
        private const val STORAGE_PERMISSION_CODE = 100
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )
        setUpActionBar()
        viewModel.isCheckPermission(checkPermission())
        binding.tvGalelery.setOnClickListener {
            if (checkPermission()) startGalleryActivity()
            else {
                viewModel.getCheckPermission().observe(this) {
                    if (it) startGalleryActivity()
                    else showGalleryDialog()
                }
            }
        }
    }

    private fun startGalleryActivity() {
        startActivity(Intent(this, GalleryActivity::class.java))
    }

    private fun showGalleryDialog() {
        showGalleryDialog {
            if (it && !checkPermission()) {
                permissionRequest()
            } else if (it && checkPermission()) startGalleryActivity()
        }
    }

    private fun permissionRequest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intent = try {
                intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                intent.setData(Uri.parse("package:${BuildConfig.APPLICATION_ID}"))
            } catch (e: Exception) {
                intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                intent
            }

            launcher.launch(intent)
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), STORAGE_PERMISSION_CODE
            )
        }
    }


    private fun setUpActionBar() {
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.open_burger,
            R.string.close_burger
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.burger);//your icon here
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val write =
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read =
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            write.equals(PackageManager.PERMISSION_GRANTED) && read.equals(PackageManager.PERMISSION_GRANTED)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty()) {
                val write = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val read = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (write && read) {
                    Log.d("Create folder", "Create folder")
                } else {
                    Log.d("External storage", "permission denied")
                }
            }
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