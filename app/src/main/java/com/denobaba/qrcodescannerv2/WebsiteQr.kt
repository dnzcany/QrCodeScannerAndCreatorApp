package com.denobaba.qrcodescannerv2

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.denobaba.qrcodescannerv2.databinding.ActivityCreateQrCodeBinding
import com.denobaba.qrcodescannerv2.databinding.ActivityWebsiteQrBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter

class WebsiteQr : AppCompatActivity() {
    private lateinit var binding: ActivityWebsiteQrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebsiteQrBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = "Website QR"



        // Başlangıçta ImageView ve indirme düğmesini gizleyin
        binding.imageView5.visibility = View.INVISIBLE
        binding.download.visibility = View.INVISIBLE

        // QR kodunu oluşturmak ve göstermek için düğmeye tıklama işleyicisi
        binding.button.setOnClickListener {
            val text = binding.editTextTextPersonName.text.toString()
            if (text.isNotEmpty()) {
                val qrCode = createQRCode(text)
                binding.imageView5.setImageBitmap(qrCode)
                binding.imageView5.visibility = View.VISIBLE
                binding.download.visibility = View.VISIBLE
            }


        }



        // QR kodunu galeriye kaydetmek için indirme düğmesine tıklama işleyicisi
        binding.download.setOnClickListener {
            val bitmap = (binding.imageView5.drawable as BitmapDrawable).bitmap
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

    private fun createQRCode(text: String): Bitmap {
        val width = 200 // İstediğiniz genişliği belirleyin
        val height = 200 // İstediğiniz yüksekliği belirleyin

        val qrCodeWriter = QRCodeWriter()
        val hints = mapOf<EncodeHintType, Any>(EncodeHintType.CHARACTER_SET to "UTF-8")
        val bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }

        return bitmap
    }
}
