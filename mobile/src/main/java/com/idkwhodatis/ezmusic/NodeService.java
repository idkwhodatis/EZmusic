package com.idkwhodatis.ezmusic;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class NodeService extends Service{
    static{
        System.loadLibrary("api");
        System.loadLibrary("node");
    }

    private String nodeDir;
    private final Handler nodeHandler=new Handler(Looper.getMainLooper());
    private final Handler statusHandler=new Handler();
    private Runnable statusRunnable;

    @Override
    public void onCreate(){
        super.onCreate();
        nodeDir=getApplicationContext().getFilesDir().getAbsolutePath()+"/javascript";
        if(wasAPKUpdated()){
            new Thread(new Runnable(){
                @Override
                public void run(){
                    MainUI.TextValue.setLoadMsg("installing Node.js API");
                    //Recursively delete any existing nodejs-project.
                    File nodeDirReference=new File(nodeDir);
                    if(nodeDirReference.exists()){
                        deleteFolderRecursively(new File(nodeDir));
                    }
                    //Copy the node project from assets into the application's data path.
                    copyAssetFolder(getApplicationContext().getAssets(),"javascript",nodeDir);
                    saveLastUpdateTime();

                    startNode();
                }
            }).start();
        }else{
            startNode();
        }
    }

    public void startNode(){
        nodeHandler.post(new Runnable(){
            @Override
            public void run(){
                MainUI.TextValue.setLoadMsg("Starting Node.js API");
                new Thread(new Runnable(){
                    @Override
                    public void run(){
                        startNodeWithArguments(new String[]{"node",nodeDir+"/api.js"});
                    }
                }).start();
                statusRunnable=new Runnable(){
                    @Override
                    public void run(){
                        Bridge.getInstance().requestQueue.add(
                            new StringRequest(Request.Method.GET,Bridge.getInstance().url("/status"),
                                new Response.Listener<String>(){
                                    @Override
                                    public void onResponse(String response){
                                        Bridge.getInstance().ui.navController.navigate("main");
                                    }
                                },
                                new Response.ErrorListener(){
                                    @Override
                                    public void onErrorResponse(VolleyError error){
                                        statusHandler.postDelayed(statusRunnable,1000);
                                    }
                                }
                            )
                        );
                    }
                };
                statusHandler.post(statusRunnable);
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        createNotificationChannel();
        Notification notification=new NotificationCompat.Builder(this,"NodeChannel")
                .setContentTitle("Foreground Service")
                .setContentText("This is a foreground service example.")
                .setSmallIcon(R.drawable.ic_cloud)
                .build();
        startForeground(1,notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        stopForeground(true);
        try{
            statusHandler.removeCallbacks(statusRunnable);
        }catch(Exception ignored){}
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    private void createNotificationChannel(){
        NotificationChannel serviceChannel=new NotificationChannel(
                "NodeChannel",
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_LOW
        );
        NotificationManager manager=getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);
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
