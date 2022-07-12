package com.example.gallerytesttask.viewmodel

import android.content.ContentValues
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.BaseColumns
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import java.util.*
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val fileProvider = "com.example.gallerytesttask.fileprovider"

    fun getBitmap(uriString: String): Bitmap? {
        val bitmap: Bitmap?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            val options = BitmapFactory.Options()
            options.inMutable = true
            bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(Uri.fromFile(File(uriString))),
                null,
                options
            )

            //bitmap = ImageDecoder.decodeBitmap(source)
        } else {
            if (File(uriString).exists()){
                bitmap =
                    MediaStore.Images.Media.getBitmap(
                        context.contentResolver,
                        Uri.fromFile(File(uriString))
                    )
            }else{
                return null
            }
        }
        return bitmap
    }


    fun getContentUri(uriString: String): Uri? {
        val bitmap: Bitmap
        getBitmap(uriString)
        if (getBitmap(uriString)!=null){
            bitmap=getBitmap(uriString)!!
        }else{
            return null
        }

        val imageFolder = File(context.cacheDir, "images")
        var contentUri: Uri? = null
        try {
            imageFolder.mkdirs()
            val file = File(imageFolder, "shared_image.png")
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream)
            stream.flush()
            stream.close()
            contentUri = FileProvider.getUriForFile(context, fileProvider, file)
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
        return contentUri
    }

    fun saveImage(displayName: String, bmp: Bitmap): Boolean {
        val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
            put(MediaStore.Images.Media.WIDTH, bmp.width)
            put(MediaStore.Images.Media.HEIGHT, bmp.height)
        }
        return try {
            context.contentResolver.insert(imageCollection, contentValues)?.also { uri ->
                context.contentResolver.openOutputStream(uri).use { outputStream ->
                    if (!bmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)) {
                        throw IOException("Couldn't save bitmap")
                    }
                }
            } ?: throw IOException("Couldn't create Media store")
            Toast.makeText(context, "image was saved", Toast.LENGTH_SHORT).show()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun deleteImage(uri: String,callback:(Uri?)->Unit) {
        val contentUri = getContentUri(uri)
        if (contentUri != null) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val uriID = getContentUriId(Uri.fromFile(File(uri)))

                    callback.invoke(uriID)
                    //getContentUri(uri)?.let { context.contentResolver.delete(it,null,null) }
                    //Log.d("deleteImage", delete.toString())
                } catch (e: Exception) {
                    Log.d("Error", e.toString())
                }
            }
        }
    }

    private fun getContentUriId(imageUri: Uri): Uri? {
        val projections = arrayOf(MediaStore.MediaColumns._ID)
        val cursor: Cursor? = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projections,
            MediaStore.MediaColumns.DATA + "=?", arrayOf(imageUri.path), null
        )
        var id: Long = 0
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst()
                id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            }
        }
        cursor?.close()
        return Uri.withAppendedPath(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            id.toInt().toString()
        )
    }

    fun saveVideo(uri: String):Boolean {
        try {
            val newfile: File
            val videoAsset: AssetFileDescriptor? =
                context.contentResolver.openAssetFileDescriptor(Uri.fromFile(File(uri)), "r")
            val inputStream: FileInputStream? = videoAsset?.createInputStream()
            val filepath = Environment.getExternalStorageDirectory()
            val dir = File(filepath.absolutePath + "/" + "Your Folder Name" + "/")
            if (!dir.exists()) {
                dir.mkdirs()
            }
            newfile = File(dir, "video_" + System.currentTimeMillis() + ".mp4")
            if (newfile.exists()) newfile.delete()
            val out: OutputStream = FileOutputStream(newfile)

            // Copy the bits from instream to outstream
            val buf = ByteArray(1024)
            var len: Int
            if (inputStream!=null){
                while (inputStream.read(buf).also { len = it } > 0) {
                    out.write(buf, 0, len)
                }
                inputStream.close()
                out.close()
            }
            Log.v("", "Copy file successful.")
            return true
        } catch (e: java.lang.Exception) {
            return false
            e.printStackTrace()
        }

    }

    fun deleteVideo(uri: String): Boolean {
        return File(uri).delete()
    }

}