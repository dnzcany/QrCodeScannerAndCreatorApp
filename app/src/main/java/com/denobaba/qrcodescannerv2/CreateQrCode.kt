package com.denobaba.qrcodescannerv2

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.Toast
import com.denobaba.qrcodescannerv2.databinding.ActivityCreateQrCodeBinding
import com.denobaba.qrcodescannerv2.databinding.ActivityQrCodeFromGalleryBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.*

class CreateQrCode : AppCompatActivity() {
    private lateinit var binding: ActivityCreateQrCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_create_qr_code)
        binding = ActivityCreateQrCodeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Create"

        binding.phone.setOnClickListener {
            val intent = Intent(this@CreateQrCode,PhoneCreate::class.java)
            startActivity(intent)
        }





        binding.editTextTextPersonName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text = s?.toString() ?: return
                if (text.isNotEmpty()) {
                    val bitmap = generateQRCode(text)
                    binding.animationView.setImageBitmap(bitmap)
                } else {
                    binding.animationView.setImageBitmap(null) // Clear the ImageView if the text is empty
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.download.setOnClickListener {
            val bitmap = (binding.animationView.drawable as BitmapDrawable).bitmap
            saveImageToGallery(bitmap)
        }

        binding.gowifi.setOnClickListener {
            val intent = Intent(this@CreateQrCode,WifiCreate::class.java)
            startActivity(intent)
        }

        binding.gowebsite.setOnClickListener {
            val intent = Intent(this@CreateQrCode,WebsiteQr::class.java)
            startActivity(intent)
        }




    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Geri düğmesine tıklanırsa aktiviteyi sonlandır
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    private fun saveImageToGallery(bitmap: Bitmap) {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "QR_Code")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Generated QR Code")
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")

        val url = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (url != null) {
            val imageOut = contentResolver.openOutputStream(url)
            if (imageOut != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageOut)
                imageOut.flush()
                imageOut.close()
            }
        }

        Toast.makeText(this, "Saved to Gallery", Toast.LENGTH_SHORT).show()
    }

    private fun generateQRCode(text: String): Bitmap? {
        val width = 250 // QR code width
        val height = 250 // QR code height

        val hints = Hashtable<EncodeHintType, ErrorCorrectionLevel>()
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H

        val bitMatrix = MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints)
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            for (x in 0 until width) {
                pixels[y * width + x] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
            }
        }

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }

}