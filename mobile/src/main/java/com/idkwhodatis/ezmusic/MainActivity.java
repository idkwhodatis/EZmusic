package com.idkwhodatis.ezmusic;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.res.AssetManager;

import androidx.appcompat.app.AppCompatActivity;
import static androidx.activity.compose.ComponentActivityKt.setContent;

import android.os.Bundle;

import java.net.*;
import java.io.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.idkwhodatis.ezmusic.MainUI;

public class MainActivity extends AppCompatActivity{
    static{
        System.loadLibrary("api");
        System.loadLibrary("node");
    }

    public static boolean _startedNodeAlready=false;
    public MainActivity instance;
    public RequestQueue requestQueue;
    public MainUI ui;

    public StringRequest testRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        instance=this;
        ui=new MainUI(this);
        ui.setContent();

        if(!_startedNodeAlready){
            _startedNodeAlready=true;
            new Thread(new Runnable(){
                @Override
                public void run(){
                    //The path where we expect the node project to be at runtime.
                    String nodeDir=getApplicationContext().getFilesDir().getAbsolutePath()+"/javascript";
                    if(wasAPKUpdated()){
                        //Recursively delete any existing nodejs-project.
                        File nodeDirReference=new File(nodeDir);
                        if(nodeDirReference.exists()){
                            deleteFolderRecursively(new File(nodeDir));
                        }
                        //Copy the node project from assets into the application's data path.
                        copyAssetFolder(getApplicationContext().getAssets(),"javascript",nodeDir);

                        saveLastUpdateTime();
                    }

                    startNodeWithArguments(new String[]{"node",
                            nodeDir+"/api.js"
                    });
                }
            }).start();
        }
        requestQueue=Volley.newRequestQueue(this);

        testRequest=new StringRequest(Request.Method.GET,"http://localhost:3000/status",
            new Response.Listener<String>(){
                @Override
                public void onResponse(String response){
                    ui.setTest(response);
                }
            },
            new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error){
                    ui.setTest(error.toString());
                }
            });

    }

    public native Integer startNodeWithArguments(String[] arguments);

    private boolean wasAPKUpdated(){
        SharedPreferences prefs=getApplicationContext().getSharedPreferences("NODEJS_MOBILE_PREFS",Context.MODE_PRIVATE);
        long previousLastUpdateTime=prefs.getLong("NODEJS_MOBILE_APK_LastUpdateTime",0);
        long lastUpdateTime=1;
        try{
            PackageInfo packageInfo=getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(),0);
            lastUpdateTime=packageInfo.lastUpdateTime;
        }catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return (lastUpdateTime!=previousLastUpdateTime);
    }

    private void saveLastUpdateTime(){
        long lastUpdateTime=1;
        try{
            PackageInfo packageInfo=getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(),0);
            lastUpdateTime=packageInfo.lastUpdateTime;
        }catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        SharedPreferences prefs=getApplicationContext().getSharedPreferences("NODEJS_MOBILE_PREFS",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit();
        editor.putLong("NODEJS_MOBILE_APK_LastUpdateTime",lastUpdateTime);
        editor.commit();
    }

    private static boolean deleteFolderRecursively(File file){
        try{
            boolean res=true;
            for(File childFile: file.listFiles()){
                if(childFile.isDirectory()){
                    res&=deleteFolderRecursively(childFile);
                }else{
                    res&=childFile.delete();
                }
            }
            res&=file.delete();
            return res;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private static boolean copyAssetFolder(AssetManager assetManager,String fromAssetPath,String toPath){
        try{
            String[] files=assetManager.list(fromAssetPath);
            boolean res=true;

            if(files.length==0){
                //If it's a file, it won't have any assets "inside" it.
                res&=copyAsset(assetManager,
                        fromAssetPath,
                        toPath);
            }else{
                new File(toPath).mkdirs();
                for(String file: files)
                    res&=copyAssetFolder(assetManager,
                            fromAssetPath+"/"+file,
                            toPath+"/"+file);
            }
            return res;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private static boolean copyAsset(AssetManager assetManager,String fromAssetPath,String toPath){
        InputStream in=null;
        OutputStream out=null;
        try{
            in=assetManager.open(fromAssetPath);
            new File(toPath).createNewFile();
            out=new FileOutputStream(toPath);
            copyFile(in,out);
            in.close();
            in=null;
            out.flush();
            out.close();
            out=null;
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private static void copyFile(InputStream in,OutputStream out) throws IOException{
        byte[] buffer=new byte[1024];
        int read;
        while((read=in.read(buffer))!=-1){
            out.write(buffer,0,read);
        }
    }

}