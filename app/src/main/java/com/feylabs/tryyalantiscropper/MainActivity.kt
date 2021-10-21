package com.feylabs.tryyalantiscropper

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.feylabs.tryyalantiscropper.databinding.ActivityMainBinding
import com.yalantis.ucrop.UCrop
import java.io.File

class MainActivity : AppCompatActivity() {

    var destinationUri = ""

    companion object {
        const val CODE_IMG_GALLERY = 7
        const val SAMPLE_IMG_CROPPED_NAME = "manyunyu7"
    }

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnPick.setOnClickListener {
            startActivityForResult(
                Intent().setAction(Intent.ACTION_GET_CONTENT)
                    .setType("image/*"), CODE_IMG_GALLERY
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (requestCode == CODE_IMG_GALLERY && resultCode == RESULT_OK) {
                val imageUri = data?.data
                if (imageUri != null) {
                    startCrop(imageUri)
                } else {
                    Toast.makeText(this, "Image URI Null", Toast.LENGTH_LONG).show()
                }
            } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
                val imageResCrop = UCrop.getOutput(data)
                if (imageResCrop != null) {
                    binding.img.setImageURI(imageResCrop)
                } else {
                    Toast.makeText(this, "Image Cropped Null", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(this, "Data Null", Toast.LENGTH_LONG).show()
        }

    }

    private fun startCrop(uri: Uri) {
        var destName = SAMPLE_IMG_CROPPED_NAME + ".jpg"
        val uCrop = UCrop.of(uri, Uri.fromFile(File(cacheDir, destName)))
        uCrop.withOptions(getOptions()).start(this)
    }

    private fun getOptions(): UCrop.Options {
        val options = UCrop.Options()
        options.setCompressionQuality(70)
        options.setFreeStyleCropEnabled(true)
        options.setToolbarColor(ContextCompat.getColor(this, R.color.green))
        options.setToolbarTitle("Edit Photo")
        return options
    }
}