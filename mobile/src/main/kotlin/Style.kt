package com.idkwhodatis.ezmusic

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class Style{

    @Composable
    fun EZTheme(content:@Composable ()->Unit){
        val colorScheme=MaterialTheme.colorScheme.copy(
            primary=Color(0xFF222222),
            secondary=Color(0xFF444444),
            tertiary=Color(0xFF999999),
            onPrimary=Color(0xFF999999),
            background=Color(0xFF111111)
        )
        MaterialTheme(
            colorScheme=colorScheme,
            typography=MaterialTheme.typography,
            shapes=MaterialTheme.shapes,
            content=content
        )
        val systemUiController=rememberSystemUiController()
        systemUiController.setStatusBarColor(
                color=colorScheme.background
        )
    }

    companion object StyleStatic {
        fun sb():String{
            return "sb"
        }

    }
}