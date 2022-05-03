package com.example.myapplication

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.Camera
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.PixelMap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.app.ActivityCompat
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //Greeting("Android")
                    myScreen(myclick = ::myclick)
                }
            }
        }
    }

    fun myclick(){

            requestPermissionLauncher.launch(arrayOf(CAMERA, READ_EXTERNAL_STORAGE))



    }

    fun check(): Boolean{

        Toast.makeText(applicationContext,"check",Toast.LENGTH_LONG).show()
        return ActivityCompat.checkSelfPermission(applicationContext,CAMERA ) == PackageManager.PERMISSION_GRANTED &&
           ActivityCompat.checkSelfPermission(applicationContext, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED



    }



    // Register the permissions callback, which handles the user's response to the
// system permissions dialog. Save the return value, an instance of
// ActivityResultLauncher. You can use either a val, as shown in this snippet,
// or a lateinit var in your onAttach() or onCreate() method.
    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
         { permissions ->
        val granted = permissions.entries.all {
            it.value == true
        }
        if (granted) {
            takePhoto()
        }
        }

    var takePhotoLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        Toast.makeText(applicationContext,"ok",Toast.LENGTH_LONG).show()
        val bmp:Bitmap=result.data?.extras?.get("data") as Bitmap
        img=bmp.asImageBitmap()


        shouldShowPhoto.value=true


    }

    fun takePhoto(){
        Toast.makeText(applicationContext,"拍",Toast.LENGTH_LONG).show()
        takePhotoLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
    }


}


lateinit var img :ImageBitmap


private var shouldShowPhoto: MutableState<Boolean> = mutableStateOf(false)



@Composable 
fun myScreen(myclick: ()->Unit  ){



    Column(){


        Button(onClick = {
            myclick()

        }){
            Text(text = "拍照")
        }
        if ( shouldShowPhoto.value) {
            Image(painter = BitmapPainter(img), contentDescription = "")
        }
    }
    
}



@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}