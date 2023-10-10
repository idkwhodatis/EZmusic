package com.idkwhodatis.ezmusic;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity{
    LoginUI ui;
    String qrkey;
    String cookie;
    private final Handler handler=new Handler();
    private Runnable loginRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ui=new LoginUI(this);
        ui.setContent();
        loginCheckerInit(false);
        generateQRCode();
    }

    public void generateQRCode(){
        Bridge.getInstance().requestQueue.add(
            new JsonObjectRequest(
                Request.Method.GET,Bridge.getInstance().url("/login/qr/key",1),
                null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        try{
                            qrkey=response.getJSONObject("data").getString("unikey");
                        }catch(JSONException e){
                            Toast.makeText(getApplicationContext(),"Retrieve qrkey failed"+e,Toast.LENGTH_LONG).show();
                        }
                        QRCodeWriter qrCodeWriter=new QRCodeWriter();
                        Bitmap bitmap=null;
                        int size=1080;

                        try{
                            HashMap<EncodeHintType,Object> hints=new HashMap<>();
                            hints.put(EncodeHintType.CHARACTER_SET,"UTF-8");
                            hints.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.H);
                            BitMatrix bitMatrix=qrCodeWriter.encode("https://music.163.com/login?codekey="+qrkey,BarcodeFormat.QR_CODE,size,size,hints);
                            bitmap=Bitmap.createBitmap(size,size,Bitmap.Config.ARGB_8888);

                            for(int y=0;y<size;y++){
                                for(int x=0;x<size;x++){
                                    bitmap.setPixel(x,y,bitMatrix.get(x,y) ? 0xFF000000:0xFFFFFFFF);
                                }
                            }
                            ui.setQRCodeBitmap(bitmap);
                            handler.post(loginRunnable);
                        }catch(WriterException e){
                            Toast.makeText(getApplicationContext(),"Generate QR code failed "+e,Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(getApplicationContext(),"/login/qr/key failed "+error.toString(),Toast.LENGTH_LONG).show();
                    }
                }
            )
        );
    }

    public void loginCheckerInit(boolean noCookie){
        String url="/login/qr/check";
        if(noCookie){
            url+="?noCookie=true";
        }
        final String finalUrl=url;
        loginRunnable=new Runnable(){
            @Override
            public void run(){
                Bridge.getInstance().requestQueue.add(
                    new JsonObjectRequest(
                        Request.Method.GET,Bridge.getInstance().url(finalUrl,1),
                        null,
                        new Response.Listener<JSONObject>(){
                            @Override
                            public void onResponse(JSONObject response){
                                try{
                                    if(response.getInt("code")==502){
                                        Toast.makeText(getApplicationContext(),"Code 502, retrying",Toast.LENGTH_SHORT).show();
                                        loginCheckerInit(true);
                                        handler.postDelayed(loginRunnable,3000);
                                    }else if(response.getInt("code")==800){
                                        Toast.makeText(getApplicationContext(),"QR Code expired, please refresh",Toast.LENGTH_SHORT).show();
                                    }else if(response.getInt("code")==803){
                                        Toast.makeText(getApplicationContext(),"Login success",Toast.LENGTH_SHORT).show();
                                        cookie=response.getString("cookie");
                                        setResult(RESULT_OK);
                                        finish();
                                    }else{
                                        handler.postDelayed(loginRunnable,3000);
                                    }
                                }catch(JSONException e){
                                    Toast.makeText(getApplicationContext(),"Retrieve login status failed "+e,Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error){
                                Toast.makeText(getApplicationContext(),"/login/qr/check failed "+error.toString(),Toast.LENGTH_LONG).show();
                                loginCheckerInit(true);
                                handler.postDelayed(loginRunnable,3000);
                            }
                        }
                    )
                );
            }
        };
    }

    public void back(){
        setResult(RESULT_CANCELED);
        finish();
    }
}
