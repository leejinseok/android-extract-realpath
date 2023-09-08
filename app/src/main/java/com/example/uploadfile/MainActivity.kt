package com.example.uploadfile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.example.uploadfile.ui.theme.UploadFileTheme


class MainActivity : ComponentActivity() {

    private val TAG = "$javaClass"

    companion object {
        val PERMISION_REQUEST_CODE = 100
    }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val fileUri = result.data?.data
                if (fileUri != null) {
                    val realPath = RealPathUtil.getRealPath(this, fileUri)
                    Toast.makeText(this, realPath, Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UploadFileTheme {
                val permission =
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                        PERMISION_REQUEST_CODE
                    )
                }

                HomeScreen(activityResultLauncher = activityResultLauncher)
            }
        }
    }
}

@Composable
fun HomeScreen(activityResultLauncher: ActivityResultLauncher<Intent>) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(onClick = {

            val intent = Intent()
            intent.action = Intent.ACTION_OPEN_DOCUMENT
            intent.setDataAndType(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "image/*")
            activityResultLauncher.launch(intent)
        }) {
            Text(text = "이미지 선택")
        }
    }
}

