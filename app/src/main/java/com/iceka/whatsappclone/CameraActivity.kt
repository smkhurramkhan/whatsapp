package com.iceka.whatsappclone

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iceka.whatsappclone.adapters.GalleryAdapter

class CameraActivity : AppCompatActivity() {
    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private val REQUEST_CODE_PERMISSIONS = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        findViewById<RecyclerView>(R.id.rv_image_galery)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        val recyclerView = findViewById<RecyclerView>(R.id.rv_image_galery)
        recyclerView.hasFixedSize()
        recyclerView.layoutManager =
            GridLayoutManager(applicationContext, 1, RecyclerView.HORIZONTAL, false)
        var images = ArrayList<String?>()
        if (images.isEmpty()) {
            images = allImages
            val adapter = GalleryAdapter(applicationContext, images)
            recyclerView.adapter = adapter
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user. " + allPermissionsGranted(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun allPermissionsGranted(): Boolean {
        for (permissions in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permissions
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    private val allImages: ArrayList<String?>
        get() {
            var images = ArrayList<String?>()
            val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE
            )
            val cursor = this.contentResolver.query(uri, projection, null, null, null)
            try {
                cursor!!.moveToFirst()
                do {
                    images.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)))
                } while (cursor.moveToNext())
                cursor.close()
                val reSelection = ArrayList<String?>()
                for (i in images.size - 1 downTo 1) {
                    reSelection.add(images[i])
                }
                images = reSelection
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return images
        }
}