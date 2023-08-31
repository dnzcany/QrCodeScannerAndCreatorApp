package com.denobaba.qrcodescannerv2

import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaScannerConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import java.io.File
import java.io.FileOutputStream

class PhoneCreate : AppCompatActivity() {

    private lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_create)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val qrheree = findViewById<ImageView>(R.id.qrhere)
        qrheree.visibility = View.INVISIBLE

        val buttonheree = findViewById<Button>(R.id.download)
        buttonheree.visibility = View.INVISIBLE

        findViewById<Button>(R.id.create).setOnClickListener {
            val phoneNumber = findViewById<EditText>(R.id.PhoneNumber).text.toString()
            if (phoneNumber.isNotBlank()) {
                bitmap = createQRCode(phoneNumber)
                findViewById<ImageView>(R.id.qrhere).setImageBitmap(bitmap)

                qrheree.visibility = View.VISIBLE
                buttonheree.visibility = View.VISIBLE




            } else {
                Toast.makeText(this, "Please enter a phone number", Toast.LENGTH_SHORT).show()
            }


        }

        findViewById<Button>(R.id.download).setOnClickListener {
            saveToGallery(bitmap)
        }
    }

    private fun createQRCode(content: String): Bitmap {
        val qrCodeWriter = QRCodeWriter()
        val bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 200, 200)

        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        return bmp
    }

    private fun saveToGallery(bitmap: Bitmap) {
        val imageFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "QRCode.png")
        val fileOutputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.close()

        MediaScannerConnection.scanFile(this, arrayOf(imageFile.absolutePath), null, null)
        Toast.makeText(this, "Saved to gallery", Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
