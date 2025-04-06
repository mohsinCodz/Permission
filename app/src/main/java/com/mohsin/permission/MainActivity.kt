package com.mohsin.permission

import android.Manifest
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mohsin.permission.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val cameraResultLauncher : ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()){
                isGranted ->
                if (isGranted) {
                    Toast.makeText(this,
                        "Permission granted for camera.", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this,
                        "Permission denied for camera.", Toast.LENGTH_LONG).show()
                }
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnRequestCameraPermission.setOnClickListener{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                showRationaleDialog("Permission Demo requires camera access",
                    "Camera cannot be used because Camera access is denied.")
            }else{
                cameraResultLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    /**
     * Shows rationale dialog for displaying why the app needs permission
     * Only shown if the user has denied the permission request previously
     */
    private fun showRationaleDialog(
        title: String,
        message: String,
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }
}