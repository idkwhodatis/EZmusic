package com.idkwhodatis.ezmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity{
    public Intent NodeIntent;
    private boolean isLoggedIn=false;
    private int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        MainUI ui=new MainUI(this);
        ui.setContent();

        Bridge.getInstance().ui=ui;
        Bridge.getInstance().initRequestQueue(this);

        NodeIntent=new Intent(this,NodeService.class);
        startForegroundService(NodeIntent);

        Bridge.getInstance().requestQueue.add(
            new JsonObjectRequest(
                Request.Method.GET,Bridge.getInstance().url("/login/status"),
                null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        try{
                            if(!response.getJSONObject("data").isNull("account")){
                                isLoggedIn=true;
                                getUserInfo();
                                getUserLists();
                            }
                        }catch(JSONException e){
                            Toast.makeText(getApplicationContext(),"retrieve login status failed "+e,Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(getApplicationContext(),"/login/status failed "+error.toString(),Toast.LENGTH_LONG).show();
                    }
                }
            )
        );
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        stopService(NodeIntent);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode==1){
            if(resultCode==RESULT_OK){
                isLoggedIn=true;
                getUserInfo();
                getUserLists();
            }
        }
    }

    public void getUserInfo(){
        Bridge.getInstance().requestQueue.add(
            new JsonObjectRequest(
                Request.Method.GET,Bridge.getInstance().url("/login/status"),
                null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        try{
                            uid=response.getJSONObject("data").getJSONObject("account").getInt("id");
                        }catch(JSONException e){
                            Toast.makeText(getApplicationContext(),"retrieve login status failed "+e,Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(getApplicationContext(),"/login/status failed "+error.toString(),Toast.LENGTH_LONG).show();
                    }
                }
            )
        );
    }

    public void getUserLists(){

    }

    public void startLogin(){
        if(!isLoggedIn){
            startActivityForResult(new Intent(this,LoginActivity.class),1);
        }
    }

}