package com.denobaba.qrcodescannerv2

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import com.denobaba.qrcodescannerv2.databinding.ActivityResultBinding
import com.denobaba.qrcodescannerv2.databinding.ActivityWifiResultBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class WifiResult : AppCompatActivity() {
    private lateinit var binding: ActivityWifiResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_wifi_result)
        binding = ActivityWifiResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val qrCodeString = intent.getStringExtra("QR_CODE")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = "Wi-fi QR"



        val qrCodeBitmap = generateQrCode(qrCodeString)
        val imageView: ImageView = findViewById(R.id.imageView8)
        imageView.setImageBitmap(qrCodeBitmap)

        binding.button.setOnClickListener {
            val bitmap = (binding.imageView8.drawable as BitmapDrawable).bitmap
            saveImageToGallery(bitmap)
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

    private fun generateQrCode(data: String?): Bitmap? {
        // QR kod oluşturma işlemi için ZXing kütüphanesini kullan
        val qrCodeWriter = QRCodeWriter()
        val bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 300, 300)
        val bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.RGB_565)
        for (x in 0 until 300) {
            for (y in 0 until 300) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        return bitmap
    }
}
