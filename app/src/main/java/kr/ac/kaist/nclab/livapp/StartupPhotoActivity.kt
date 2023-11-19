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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.ac.kaist.nclab.livapp.analyzer.createTempPictureUri

class StartupPhotoActivity : ComponentActivity() {
    @Composable
    fun normalButton(pv: PaddingValues, txt: String, onClick: () -> Unit) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .padding(pv)
                .height(55.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(size = 20.dp),
        ) {
            Text(
                text = txt,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.roboto)),
                    fontWeight = FontWeight(300),
                    textAlign = TextAlign.Center
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            val context = LocalContext.current
            // The most recently successfully taken photo
            var recentPhotoUri by remember { mutableStateOf(value = Uri.EMPTY) }
            // Temporary URL holder for ActivityResultContract
            var tempPhotoUri by remember { mutableStateOf(value = Uri.EMPTY) }
            val cameraLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture(), onResult = { success ->
                Log.d("TTT", "success?: $success, uri: $tempPhotoUri")
                if (success) {
                    recentPhotoUri = tempPhotoUri
                    navController.navigate("name")
                }
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, recentPhotoUri))
            })

            var name by remember { mutableStateOf(value = "")}
            var height by remember { mutableStateOf(value = "") }
            var weight by remember { mutableStateOf(value = "") }
            var age by remember { mutableStateOf(value = "") }

            val commonTextStyle = TextStyle(
                fontSize = 24.sp,
                lineHeight = 31.09.sp,
                fontFamily = FontFamily(Font(R.font.roboto)),
                fontWeight = FontWeight(300),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center
            )

            val commonTextInputColors = TextFieldDefaults.textFieldColors(
                textColor = Color(context.getColor(R.color.white)),
                cursorColor = Color(context.getColor(R.color.white)),
                focusedIndicatorColor = Color(context.getColor(R.color.evenLessDark)),
                unfocusedIndicatorColor = Color(context.getColor(R.color.lessDark))
            )

            NavHost(navController = navController, startDestination = "hi") {
                composable("hi") {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 28.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.weight(1.0f))
                        Text(
                            text = "Do you want to register a band test result?",
                            style = TextStyle(
                                fontSize = 26.sp,
                                lineHeight = 31.09.sp,
                                fontFamily = FontFamily(Font(R.font.roboto)),
                                fontWeight = FontWeight(800),
                                color = Color(0xFFFFFFFF),
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.wrapContentHeight()
                        )
                        Spacer(modifier = Modifier.weight(1.0f))

                        normalButton(pv = PaddingValues(bottom = 16.dp), txt = "Yes") {
                            tempPhotoUri = context.createTempPictureUri()
                            cameraLauncher.launch(tempPhotoUri)
                        }
                        normalButton(pv = PaddingValues(bottom = 26.dp), txt = "No") {
                            navController.navigate("working")
                        }
                    }
                }
                composable("name") {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 28.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Please enter your name.",
                            style = commonTextStyle,
                            modifier = Modifier.padding(top = 30.dp)
                        )
                        Spacer(modifier = Modifier.weight(0.4f))
                        TextField(
                            value = name,
                            onValueChange = {
                                name = it
                            },
                            colors = commonTextInputColors,
                            textStyle = commonTextStyle,
                            singleLine = true,
                        )
                        Spacer(modifier = Modifier.weight(0.6f))
                        normalButton(pv = PaddingValues(bottom = 26.dp), txt = "Next") {
                            navController.navigate("gender")
                        }
                    }
                }
                composable("gender") {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 28.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Please check your gender.",
                            style = commonTextStyle,
                            modifier = Modifier.padding(top = 30.dp)
                        )
                        Spacer(modifier = Modifier.weight(0.35f))
                        Button(
                            onClick = { navController.navigate("height") },
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                                .fillMaxWidth()
                                .height(55.dp),
                            shape = RoundedCornerShape(size = 20.dp),
                        ) {
                            Text(
                                text = "Male",
                                style = commonTextStyle
                            )
                        }
                        Button(
                            onClick = { navController.navigate("height") },
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                                .fillMaxWidth()
                                .height(55.dp),
                            shape = RoundedCornerShape(size = 20.dp),
                        ) {
                            Text(
                                text = "Female",
                                style = commonTextStyle
                            )
                        }
                        Spacer(modifier = Modifier.weight(0.65f))
                    }
                }
                composable("height") {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 28.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Please enter your height.",
                            style = commonTextStyle,
                            modifier = Modifier.padding(top = 30.dp)
                        )
                        Spacer(modifier = Modifier.weight(0.4f))
                        TextField(
                            value = height,
                            onValueChange = {
                                height = it
                            },
                            colors = commonTextInputColors,
                            textStyle = commonTextStyle,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Spacer(modifier = Modifier.weight(0.6f))
                        normalButton(pv = PaddingValues(bottom = 26.dp), txt = "Next") {
                            navController.navigate("weight")
                        }
                    }
                }
                composable("weight") {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 28.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Please enter your weight.",
                            style = commonTextStyle,
                            modifier = Modifier.padding(top = 30.dp)
                        )
                        Spacer(modifier = Modifier.weight(0.4f))
                        TextField(
                            value = weight,
                            onValueChange = {
                                weight = it
                            },
                            colors = commonTextInputColors,
                            textStyle = commonTextStyle,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Spacer(modifier = Modifier.weight(0.6f))
                        normalButton(pv = PaddingValues(bottom = 26.dp), txt = "Next") {
                            navController.navigate("age")
                        }
                    }
                }
                composable("age") {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 28.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Please enter your age.",
                            style = commonTextStyle,
                            modifier = Modifier.padding(top = 30.dp)
                        )
                        Spacer(modifier = Modifier.weight(0.4f))
                        TextField(
                            value = age,
                            onValueChange = {
                                age = it
                            },
                            colors = commonTextInputColors,
                            textStyle = commonTextStyle,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Spacer(modifier = Modifier.weight(0.6f))
                        normalButton(pv = PaddingValues(bottom = 26.dp), txt = "Next") {
                            navController.navigate("syncFitness")
                        }
                    }
                }
                composable("syncFitness") {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 28.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.weight(1.0f))
                        Text(
                            text = "Would you like to sync your fitness data?",
                            style = commonTextStyle,
                            modifier = Modifier.wrapContentHeight()
                        )
                        Spacer(modifier = Modifier.weight(1.0f))

                        normalButton(pv = PaddingValues(bottom = 16.dp), txt = "Yes") {
                            navController.navigate("working")
                        }
                        normalButton(pv = PaddingValues(bottom = 26.dp), txt = "No") {
                            navController.navigate("working")
                        }
                    }
                }

                composable("working") {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 28.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.weight(0.8f))
                        Text(
                            text = "Analyzing...",
                            style = commonTextStyle,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(vertical = 15.dp)
                        )
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            progress = 0.5f
                        )
                        Spacer(modifier = Modifier.weight(1.0f))
                    }
                }
            }
        }
    }
}
