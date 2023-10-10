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
        if(endpoint.contains("?")){
            endpoint+="&realip=116.25.146.177";
        }else{
            endpoint+="?realip=116.25.146.177";
        }
        return "http://localhost:3000"+endpoint;
    }

    public String url(String endpoint,int addTimeStamp){
        return url(endpoint)+"&timestamp="+System.currentTimeMillis();
    }

}
