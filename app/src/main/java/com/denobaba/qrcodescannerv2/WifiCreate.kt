package com.denobaba.qrcodescannerv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner

class WifiCreate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi_create)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = "Wi-fi"



        val spinner: Spinner = findViewById(R.id.spinner_security_type)
        val options = arrayOf("WPA/WPA2", "WEP", "None")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            val ssid = findViewById<EditText>(R.id.editTextTextPersonName).text.toString()
            val password = findViewById<EditText>(R.id.editTextTextPersonName2).text.toString()
            val securityType = spinner.selectedItem.toString()

            val qrCodeString = generateQrCodeString(ssid, password, securityType)

            val intent = Intent(this, WifiResult::class.java)
            intent.putExtra("QR_CODE", qrCodeString)
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

    private fun generateQrCodeString(ssid: String, password: String, securityType: String): String {
        // QR kod formatına uygun bir şekilde WiFi bilgilerini dönüştür
        return "WIFI:S:$ssid;T:$securityType;P:$password;;"
    }
}