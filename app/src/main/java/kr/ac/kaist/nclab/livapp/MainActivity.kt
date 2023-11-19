package kr.ac.kaist.nclab.livapp

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kr.ac.kaist.nclab.livapp.analyzer.createTempPictureUri


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            // The most recently successfully taken photo
            var recentPhotoUri by remember { mutableStateOf(value = Uri.EMPTY) }
            // Temporary URL holder for ActivityResultContract
            var tempPhotoUri by remember { mutableStateOf(value = Uri.EMPTY) }
            val cameraLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture(), onResult = { success ->
                Log.d("TTT", "success?: $success, uri: $tempPhotoUri")
                if (success) {
                    recentPhotoUri = tempPhotoUri
                }
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, recentPhotoUri))
            })

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    tempPhotoUri = context.createTempPictureUri()
                    cameraLauncher.launch(tempPhotoUri)
                }) {
                    Text(text = "Take a photo with Camera")
                }
            }
        }
    }
}
