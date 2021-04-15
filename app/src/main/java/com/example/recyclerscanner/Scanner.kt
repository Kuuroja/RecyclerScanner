package com.example.recyclerscanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode

class Scanner : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        var cancelButton = findViewById<Button>(R.id.cancel_button)
        cancelButton.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        codeScanner()   // run the scanner
    }

    //scanner function
    private fun codeScanner() {
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        codeScanner = CodeScanner(this, scannerView)

        //set focus, scan mode and flash
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            //when code is scanned
            decodeCallback = DecodeCallback {
                runOnUiThread {
                    val scannerText = findViewById<TextView>(R.id.scanner_text)
                    scannerText.text = it.text
                }
            }

            //in case of errors
            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("main", "camera initialization error: ${it.message}")
                }
            }
        }

        //if scanner is set on "single" mode this is used to refresh the scanning function
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    //when app is focused resume scanning
    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    //when app is not focused release resources
    override fun onPause() {
        super.onPause()
        codeScanner.releaseResources()
    }

}