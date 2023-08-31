package com.denobaba.qrcodescannerv2
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.budiyev.android.codescanner.*
import com.denobaba.qrcodescannerv2.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView


private const val CAMERA_REQUEST_CODE = 101

class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle

    private lateinit var codeScanner: CodeScanner
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupPermissions()
        codeScanner()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        supportActionBar?.title = "Qr Code Scanner"


        val drawerLayout : DrawerLayout = findViewById(R.id.drawerlayout)

        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close)
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_result ->{
                    val intent = Intent(this@MainActivity,ResultActivity::class.java)
                    startActivity(intent)
                }

                R.id.nav_create -> {
                    val intent = Intent(this@MainActivity,CreateQrCode::class.java)
                    startActivity(intent)
                }

                R.id.nav_gallery -> {
                    val intent = Intent(this@MainActivity,QrCodeFromGallery::class.java)
                    startActivity(intent)
                }

            }
            true
        }



        binding.create.setOnClickListener {
            val intent = Intent(this@MainActivity, CreateQrCode::class.java)
            startActivity(intent)
        }

        binding.gogallery.setOnClickListener {
            val intent = Intent(this@MainActivity, QrCodeFromGallery::class.java)
            startActivity(intent)
        }
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }

        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun codeScanner(){
        codeScanner = CodeScanner(this,binding.codeScannerView)
        codeScanner.apply {
            camera= CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode= AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled= false
            decodeCallback = DecodeCallback {
                runOnUiThread {
                    val intent = Intent(this@MainActivity, ResultActivity::class.java)
                    intent.putExtra("result", it.text)
                    startActivity(intent)
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main","Camera initialization error: ${it.message}")
                }
            }
        }

        binding.codeScannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermissions(){
        val permission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    private fun makeRequest(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE)

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode){
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"You need the camera permission to be able to use this app",Toast.LENGTH_SHORT).show()

                }else{

                }
            }
        }
    }
}