package com.denobaba.qrcodescannerv2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

class WebsiteCreate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_website_create)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = "Website"


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Geri düğmesine tıklanırsa aktiviteyi sonlandır
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}