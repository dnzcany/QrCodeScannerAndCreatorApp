package com.denobaba.qrcodescannerv2

import android.content.*
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import com.denobaba.qrcodescannerv2.databinding.ActivityMainBinding
import com.denobaba.qrcodescannerv2.databinding.ActivityResultBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.*

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = "Result"


        val result = intent.getStringExtra("result")
        binding.textView4.text = result

        // Create a QR code Bitmap and set it as the image for the ImageView
        val qrCodeBitmap = generateQRCode(result)
        binding.imageView8.setImageBitmap(qrCodeBitmap)

        binding.download.setOnClickListener {
            val bitmap = (binding.imageView8.drawable as BitmapDrawable).bitmap
            saveImageToGallery(bitmap)
        }

        binding.imageView4.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("QRCode", binding.textView4.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
        }

        binding.imageView7.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, binding.textView4.text.toString())
            startActivity(Intent.createChooser(shareIntent, "Share via"))
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



    private fun generateQRCode(text: String?): Bitmap? {
        if (text == null) return null

        val width = 300 // Width of the QR code
        val height = 300 // Height of the QR code

        val hints: MutableMap<EncodeHintType, Any> = EnumMap(EncodeHintType::class.java)
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.L
        hints[EncodeHintType.MARGIN] = 2 // Margin (border)

        val writer = MultiFormatWriter()
        val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height, hints)

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        return bitmap
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
}
