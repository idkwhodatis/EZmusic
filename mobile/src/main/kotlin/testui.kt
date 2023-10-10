package com.idkwhodatis.ezmusic

import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.Divider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
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
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

class testui {

    var url by mutableStateOf("url")
    var username by mutableStateOf("username")
    var level by mutableStateOf("lvl")
    var vip by mutableStateOf("vip")
    var fav by mutableStateOf(SongList())
    val myLists=mutableStateListOf<SongList>()
    val likedLists=mutableStateListOf<SongList>()
    val likedRadio=mutableStateListOf<SongList>()

    var url1="https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png"
    val test=listOf(SongList(url1,"test",23),SongList(url1,"test",23),SongList(url1,"test",23))

    @OptIn(ExperimentalMaterial3Api::class,ExperimentalFoundationApi::class)
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
                        AsyncImage(
                            model=url,
                            contentDescription="user icon",
                            placeholder=painterResource(id=R.drawable.ic_user),
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
                        .padding(top=20.dp)
                        .background(
                            color=MaterialTheme.colorScheme.primary,
                            shape=RoundedCornerShape(10)
                        )
                        .fillMaxHeight(0.13f)
                        .fillMaxWidth(0.9f)
                ){
                    Row(modifier=Modifier.align(Alignment.CenterStart)){
                        AsyncImage(
                            model=fav.cover,
                            contentDescription="Favorite Songs",
                            placeholder=painterResource(id=R.drawable.ic_music),
                            modifier=Modifier
                                .padding(start=15.dp)
                                .height(65.dp)
                                .width(65.dp)
                        )
                        Column(
                            modifier=Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start=25.dp)
                        ){
                            Text(
                                text=fav.name,
                                fontWeight=FontWeight.SemiBold,
                                fontSize=20.sp,
                                color=MaterialTheme.colorScheme.tertiary
                            )
                            Text(
                                text=fav.songCount.toString()+" songs",
                                fontSize=15.sp,
                                color=MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                }

                Box(
                    modifier=Modifier
                        .padding(top=20.dp)
                        .background(
                            color=MaterialTheme.colorScheme.primary,
                            shape=RoundedCornerShape(3)
                        )
                        .fillMaxWidth(0.9f)
                ){
                    val scope=rememberCoroutineScope()
                    val listState=rememberLazyListState()
                    LazyColumn(state=listState){
                        stickyHeader{
                            Row(
                                modifier=Modifier.fillMaxWidth(),
                                horizontalArrangement=Arrangement.SpaceBetween
                            ){
                                Button(onClick={scope.launch{listState.animateScrollToItem(getIndex(0))}}){
                                    Text("我的歌单")
                                }
                                Button(onClick={scope.launch{listState.animateScrollToItem(getIndex(1))}}){
                                    Text("收藏歌单")
                                }
                                Button(onClick={scope.launch{listState.animateScrollToItem(getIndex(2))}}){
                                    Text("收藏电台")
                                }
                            }
                            Divider(color=MaterialTheme.colorScheme.tertiary)
                        }
                        item{
                            ShowHeader("我的歌单")
                        }
                        items(myLists){i->
                            ShowList(i)
                        }
                        item{
                            Divider(color=MaterialTheme.colorScheme.tertiary)
                        }
                        item{
                            ShowHeader("收藏歌单")
                        }
                        items(likedLists){i->
                            ShowList(i)
                        }
                        item{
                            Divider(color=MaterialTheme.colorScheme.tertiary)
                        }
                        item{
                            ShowHeader("收藏电台")
                        }
                        items(likedRadio){i->
                            ShowList(i)
                        }
                    }
                }

            }
        }
    }

    @Composable
    fun ShowHeader(s:String){
        Box(
            modifier=Modifier
                .fillMaxHeight(0.1f)
                .fillMaxWidth()
        ){
            Text(
                text=s,
                fontSize=12.sp,
                color=MaterialTheme.colorScheme.tertiary,
                modifier=Modifier.padding(start=15.dp)
            )
        }
        Divider(color=MaterialTheme.colorScheme.tertiary)
    }

    @Composable
    fun ShowList(i:SongList){
        Row(modifier=Modifier.padding(top=2.dp,bottom=2.dp)){
            AsyncImage(
                model=i.cover,
                contentDescription="Favorite Songs",
                placeholder=painterResource(id=R.drawable.ic_music),
                modifier=Modifier
                    .padding(start=15.dp)
                    .height(60.dp)
                    .width(60.dp)
            )
            Column(
                modifier=Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start=25.dp)
            ){
                Text(
                    text=i.name,
                    fontWeight=FontWeight.SemiBold,
                    fontSize=20.sp,
                    color=MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text=i.songCount.toString()+" songs",
                    fontSize=15.sp,
                    color=MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }

    private fun getIndex(count:Int):Int{
        return when(count){
            0->0
            1->myLists.size
            2->myLists.size+1+likedLists.size
            else->-1
        }
    }


    var QRCode by mutableStateOf(ImageBitmap(1000,1000));

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun LoginScreen(){
        Scaffold(
            topBar={TopAppBar(
                colors=TopAppBarDefaults.topAppBarColors(
                    containerColor=MaterialTheme.colorScheme.background
                ),
                title={},
                navigationIcon={
                    IconButton(onClick={}){
                        Icon(
                            painter=painterResource(id=R.drawable.ic_back),
                            contentDescription="",
                            tint=MaterialTheme.colorScheme.secondary,
                            modifier=Modifier.size(30.dp)
                        )
                    }
                },
                actions={
                    IconButton(onClick={}){
                        Icon(
                            painter=painterResource(id=R.drawable.ic_refresh),
                            contentDescription="",
                            tint=MaterialTheme.colorScheme.secondary,
                            modifier=Modifier.size(30.dp)
                        )
                    }
                }
            )}
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

    @Preview(showBackground = true,name="test")
    @Composable
    fun TestPreview(){
        Style().EZTheme {
            LoginScreen()
        }
    }
}