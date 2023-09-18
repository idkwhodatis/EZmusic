package com.idkwhodatis.ezmusic;

import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainUI(val activity:MainActivity){
    lateinit var navController:NavHostController

    companion object Res{
        var loadMsg by mutableStateOf("Initializing")
        var test by mutableStateOf("test")

    }


    fun setContent(){
        activity.setContent{
            Style().EZTheme{
                Content()
            }
        }
    }

    @Composable
    fun Content(){
        navController=rememberNavController()
        NavHost(navController=navController,startDestination="load"){
            composable("main"){
                MainScreen()
            }
            composable("load"){
                LoadScreen()
            }
        }
    }

    @Composable
    fun LoadScreen(){
        Box(
            modifier=Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment=Alignment.Center
        ){
            Column(horizontalAlignment=Alignment.CenterHorizontally){
                CircularProgressIndicator(
                    modifier=Modifier.wrapContentSize(),
                    color=MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier=Modifier.height(16.dp))
                Text(loadMsg,color=MaterialTheme.colorScheme.tertiary)
            }
        }

    }

    @Composable
    fun MainScreen(){
        Box(
            modifier=Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment=Alignment.Center
        ){
            Box(
                modifier=Modifier
                    .fillMaxHeight(0.15f)
                    .fillMaxWidth(0.9f)
                    .background(
                        color=MaterialTheme.colorScheme.primary,
                        shape=RoundedCornerShape(10)
                    )
            ){
            }
            Column(horizontalAlignment=Alignment.CenterHorizontally){
                Text(test,color=MaterialTheme.colorScheme.secondary)
                Button(onClick={activity.test()}){
                    Text("Fetch")
                }
            }
        }
    }
}