package com.idkwhodatis.ezmusic;

import android.content.Context;

import androidx.navigation.NavController;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Bridge{
    private static final Bridge instance=new Bridge();
    public MainUI ui;
    public RequestQueue requestQueue;

    public Bridge(){}
    public static Bridge getInstance(){
        return instance;
    }

    public void initRequestQueue(Context context){
        requestQueue=Volley.newRequestQueue(context.getApplicationContext());
    }

    public String url(String endpoint){
        return "http://localhost:3000"+endpoint;
    }

}
