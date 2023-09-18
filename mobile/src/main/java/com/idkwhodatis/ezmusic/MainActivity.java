package com.idkwhodatis.ezmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


public class MainActivity extends AppCompatActivity{
    public Intent NodeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        MainUI ui=new MainUI(this);
        ui.setContent();
        Bridge.getInstance().ui=ui;

        NodeIntent=new Intent(this,NodeService.class);
        startForegroundService(NodeIntent);

        Bridge.getInstance().initRequestQueue(this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        stopService(NodeIntent);
    }

    public void test(){
        Bridge.getInstance().requestQueue.add(
            new StringRequest(Request.Method.GET,Bridge.getInstance().url("/status"),
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        MainUI.Res.setTest(response);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        MainUI.Res.setTest(error.toString());
                    }
                }
            )
        );
    }
}