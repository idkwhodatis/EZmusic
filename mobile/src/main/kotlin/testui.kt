package com.idkwhodatis.ezmusic

import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class testui {

    var userIcon by mutableStateOf(R.drawable.ic_user)
    var username by mutableStateOf("username")
    var level by mutableStateOf("lvl")
    var vip by mutableStateOf("vip")

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreen(){
        Scaffold(
            topBar={TopAppBar(
                colors=TopAppBarDefaults.topAppBarColors(
                    containerColor=MaterialTheme.colorScheme.background
                ),
                title={},
                navigationIcon={
                    IconButton(onClick={}){
                        Icon(
                            painter=painterResource(id=R.drawable.ic_menu),
                            contentDescription="",
                            tint=MaterialTheme.colorScheme.secondary,
                            modifier=Modifier.size(30.dp)
                        )
                    }
                },
                actions={
                    IconButton(onClick={}){
                        Icon(
                            painter=painterResource(id=R.drawable.ic_search),
                            contentDescription="",
                            tint=MaterialTheme.colorScheme.secondary,
                            modifier=Modifier.size(30.dp)
                        )
                    }
                }
            )}
        ){innerPadding->
            Column(
                modifier=Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment=Alignment.CenterHorizontally
            ){
                Box(
                    modifier=Modifier
                        .background(
                            color=MaterialTheme.colorScheme.primary,
                            shape=RoundedCornerShape(10)
                        )
                        .fillMaxHeight(0.15f)
                        .fillMaxWidth(0.9f)
                ){
                    Row(modifier=Modifier.align(Alignment.CenterStart)){
                        Image(
                            painter=painterResource(id=userIcon),
                            contentDescription="User",
                            contentScale=ContentScale.Fit,
                            modifier=Modifier
                                .padding(start=15.dp)
                                .height(65.dp)
                                .width(65.dp)
                        )
                        Column(modifier=Modifier.padding(start=15.dp)){
                            Text(
                                text=username,
                                fontWeight=FontWeight.SemiBold,
                                fontSize=20.sp,
                                color=MaterialTheme.colorScheme.tertiary
                            )
                            Row(modifier=Modifier.padding(top=5.dp)){
                                Text(
                                    text=level,
                                    fontSize=15.sp,
                                    color=MaterialTheme.colorScheme.tertiary
                                )
                                Text(
                                    text=vip,
                                    fontSize=15.sp,
                                    color=MaterialTheme.colorScheme.tertiary,
                                    modifier=Modifier.padding(start=25.dp)
                                )
                            }
                        }
                    }
                }
                Box(
                    modifier=Modifier
                        .padding(top=30.dp)
                        .background(
                            color=MaterialTheme.colorScheme.primary,
                            shape=RoundedCornerShape(10)
                        )
                        .fillMaxHeight(0.13f)
                        .fillMaxWidth(0.9f)
                ){

                }
                Row(
                    modifier=Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement=Arrangement.SpaceBetween
                ){
                    Button(onClick={}){
                        Text("我的歌单")
                    }
                    Button(onClick={}){
                        Text("收藏歌单")
                    }
                    Button(onClick={}){
                        Text("收藏电台")
                    }
                }
            }
        }

    }

    @Preview(showBackground = true,name="test")
    @Composable
    fun TestPreview(){
        Style().EZTheme {
            MainScreen()
        }
    }
}