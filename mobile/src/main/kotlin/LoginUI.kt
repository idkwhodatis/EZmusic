package com.idkwhodatis.ezmusic

import android.graphics.Bitmap
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginUI(val activity:LoginActivity){
    var QRCode by mutableStateOf(ImageBitmap(1000,1000));
    var isButtonEnabled by mutableStateOf(true);

    fun setContent(){
        activity.setContent{
            Style().EZTheme{
                Content()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Content(){
        Scaffold(
            topBar={
                TopAppBar(
                colors=TopAppBarDefaults.topAppBarColors(
                    containerColor=MaterialTheme.colorScheme.background
                ),
                title={},
                navigationIcon={
                    IconButton(onClick={activity.back()}){
                        Icon(
                            painter=painterResource(id=R.drawable.ic_back),
                            contentDescription="",
                            tint=MaterialTheme.colorScheme.secondary,
                            modifier=Modifier.size(30.dp)
                        )
                    }
                },
                actions={
                    IconButton(onClick={
                        activity.generateQRCode()
                        isButtonEnabled=false
                        CoroutineScope(Dispatchers.Main).launch{
                            delay(2000)
                            isButtonEnabled=true
                        }
                    }){
                        Icon(
                            painter=painterResource(id=R.drawable.ic_refresh),
                            contentDescription="",
                            tint=MaterialTheme.colorScheme.secondary,
                            modifier=Modifier.size(30.dp)
                        )
                    }
                }
            )
            }
        ){innerPadding->
            Box(
                modifier=Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment=Alignment.Center
            ){
                Image(
                    painter=BitmapPainter(QRCode),
                    contentDescription=null,
                    modifier=Modifier
                        .offset(y=(-50).dp)
                        .size(300.dp,300.dp),
                    contentScale=ContentScale.FillBounds
                )
            }
        }
    }

    fun setQRCodeBitmap(b:Bitmap){
        QRCode=b.asImageBitmap()
    }

}