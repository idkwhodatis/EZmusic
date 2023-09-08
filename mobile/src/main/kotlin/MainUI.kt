package com.idkwhodatis.ezmusic;

import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

class MainUI(val activity:MainActivity){
    var test by mutableStateOf("test")

    fun setContent(){
        activity.setContent{
            Style().EZTheme{
                Content()
            }
        }
    }

    @Composable
    fun Content(){
            Box(
                modifier=Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                    contentAlignment=Alignment.Center
            ){
                Box(
                    modifier=Modifier
                        .fillMaxHeight(0.2f)
                        .fillMaxWidth(0.9f)
                        .background(MaterialTheme.colorScheme.primary)
                ){
                }
                Column(horizontalAlignment=Alignment.CenterHorizontally){
                    Text(test,color=MaterialTheme.colorScheme.secondary)
                    Button(onClick={activity.requestQueue.add(activity.testRequest)}){
                        Text("Fetch")
                    }
                }
            }
    }
}