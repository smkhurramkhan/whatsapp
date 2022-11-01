package com.iceka.whatsappclone.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.Rational
import android.util.Size
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.core.CameraX.LensFacing
import androidx.camera.core.Preview.OnPreviewOutputUpdateListener
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iceka.whatsappclone.EditStatusActivity
import com.iceka.whatsappclone.R
import com.iceka.whatsappclone.adapters.GalleryAdapter
import java.io.File

class CameraTabFragment : Fragment() {
    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private var flashType = 1
    private var mRecyclerView: RecyclerView? = null
    private var mTextureView: TextureView? = null
    private var mFlash: ImageView? = null
    private var mCapture: ImageView? = null
    private var mSwitch: ImageView? = null
    private var mLensFacing = LensFacing.BACK
    private var mImageCapture: ImageCapture? = null
    private var flashMode = FlashMode.AUTO
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_camera, container, false)
        mRecyclerView = rootView.findViewById(R.id.rv_image_galery)
        mTextureView = rootView.findViewById(R.id.textureview)
        mFlash = rootView.findViewById(R.id.img_flash)
        mCapture = rootView.findViewById(R.id.img_capture)
        mSwitch = rootView.findViewById(R.id.img_switch)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                activity!!,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        } else {
            startCamera()
        }
        mRecyclerView?.hasFixedSize()
        mRecyclerView?.layoutManager = GridLayoutManager(
            context,
            1,
            RecyclerView.HORIZONTAL,
            false
        )
        var images = ArrayList<String?>()
        if (images.isEmpty()) {
            images = allImages
            val adapter = GalleryAdapter(requireContext(), images)
            mRecyclerView?.adapter = adapter
        }
        mFlash?.setOnClickListener {
            flashToggle()
            Log.i("MYTAG", "flash no : $flashType")
            setFlashIcon()
        }
        return rootView
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    context,
                    "Permissions not granted by the user. " + allPermissionsGranted(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun allPermissionsGranted(): Boolean {
        for (permissions in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    context!!,
                    permissions
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    private fun startCamera() {
        bindCamera()
        mSwitch?.setOnClickListener {
            mLensFacing = if (mLensFacing == LensFacing.FRONT) {
                LensFacing.BACK
            } else {
                LensFacing.FRONT
            }
            try {
                CameraX.getCameraWithLensFacing(mLensFacing)
                CameraX.unbindAll()
                bindCamera()
            } catch (e: CameraInfoUnavailableException) {
                e.printStackTrace()
            }
        }
        mCapture?.setOnClickListener {
            val file = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/" + System.currentTimeMillis() + ".jpg"
            )
            mImageCapture?.flashMode = flashMode
            mImageCapture?.takePicture(file, object : ImageCapture.OnImageSavedListener {
                override fun onImageSaved(file: File) {
                    val intent = Intent(context, EditStatusActivity::class.java)
                    intent.putExtra("from_activity", requireActivity().javaClass.simpleName)
                    intent.putExtra("file", file.absolutePath)
                    startActivity(intent)
                }

                override fun onError(
                    useCaseError: ImageCapture.UseCaseError,
                    message: String,
                    cause: Throwable?
                ) {
                    val msg = "Pic captured failed : $message"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    cause?.printStackTrace()
                }
            })
        }
    }

    private fun bindCamera() {
        CameraX.unbindAll()
        val aspectRatio = Rational(mTextureView!!.width, mTextureView!!.height)
        val screenSize = Size(mTextureView!!.width, mTextureView!!.height)
        val previewConfig = PreviewConfig.Builder()
            .setTargetAspectRatio(aspectRatio)
            .setTargetResolution(screenSize)
            .setLensFacing(mLensFacing)
            .build()
        val preview = Preview(previewConfig)
        preview.onPreviewOutputUpdateListener = OnPreviewOutputUpdateListener { output ->
            val parent = mTextureView!!.parent as ViewGroup
            parent.removeView(mTextureView)
            parent.addView(mTextureView, 0)
            mTextureView!!.setSurfaceTexture(output.surfaceTexture)
        }
        val imageCaptureConfig = ImageCaptureConfig.Builder()
            .setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
            .setTargetRotation(requireActivity().windowManager.defaultDisplay.rotation)
            .setLensFacing(mLensFacing)
            .build()
        mImageCapture = ImageCapture(imageCaptureConfig)
        CameraX.bindToLifecycle(this, preview, mImageCapture)
    }

    private fun setFlashIcon() {
        if (flashType == 1) {
            mFlash!!.setImageResource(R.drawable.ic_flash_auto_white_24dp)
            flashMode = FlashMode.AUTO
        } else if (flashType == 2) {
            mFlash!!.setImageResource(R.drawable.ic_flash_on_white_24dp)
            flashMode = FlashMode.ON
        } else if (flashType == 3) {
            mFlash!!.setImageResource(R.drawable.ic_flash_off_white_24dp)
            flashMode = FlashMode.OFF
        }
    }

    private fun flashToggle() {
        if (flashType == 1) {
            flashType = 2
        } else if (flashType == 2) {
            flashType = 3
        } else if (flashType == 3) {
            flashType = 1
        }
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
            val cursor = activity!!.contentResolver.query(uri, projection, null, null, null)
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

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}