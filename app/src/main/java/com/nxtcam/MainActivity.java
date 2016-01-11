package com.nxtcam;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.textureview.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.turbomanage.httpclient.HttpResponse;
import org.opencv.android.*;

public class MainActivity extends Activity implements SurfaceTextureListener {
    private static TextureView myTexture;
    private Camera mCamera;
    private static Activity instance = null;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        myTexture = new TextureView(this);
        myTexture.setSurfaceTextureListener(this);
//        TextureView p = (TextureView) findViewById(R.id.textureView1);
        setContentView(myTexture);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return take_and_send();
    }

    public static boolean take_and_send() {
        try {
            Bitmap p = myTexture.getBitmap();

            File outputDir = instance.getCacheDir(); // context being the Activity pointer
            File file = File.createTempFile("com.NXT", "file", outputDir);

            FileOutputStream fop = new FileOutputStream(file);
            p.compress(Bitmap.CompressFormat.JPEG, 40, fop);
            fop.flush();
            fop.close();






//            Ion.with(instance)
//                    .load("http://172.16.128.219:5000/")
//                    .setMultipartParameter("abc", "def")
//                    .setMultipartFile("image", "image/jpeg", file)
//                    .asJsonObject()
//                    .setCallback(new FutureCallback<JsonObject>() {
//                        @Override
//                        public void onCompleted(Exception e, JsonObject result) {
//                            if(e !=null)
//                                e.printStackTrace();
//                            else
//                                Log.e("OK", result.toString());
//                        }
//                    });
//
//            //        new And
            return true;
        } catch (IOException io){
            Log.e("noo","noo");
            io.printStackTrace();
        }
        return false;
    }

    @SuppressLint("NewApi")
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture arg0, int arg1, int arg2) {
        mCamera = Camera.open();
        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        myTexture.setLayoutParams(new FrameLayout.LayoutParams(
                width, height, Gravity.CENTER));

        try {
            mCamera.setPreviewTexture(arg0);
        }

        catch (IOException t) {
        }
        mCamera.startPreview();
        myTexture.setAlpha(1.0f);
//        myTexture.setRotation(90.0f);
        new GoGo().execute();
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture arg0) {
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture arg0, int arg1,
                                            int arg2) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture arg0) {
        // TODO Auto-generated method stub
    }

    public void launchInfos(View view) {
        take_and_send();
    }

    private class GoGo extends AsyncTask<Void, Void, Boolean> {
        protected Boolean doInBackground(Void... urls) {
            Log.e("init","init");
            return take_and_send();
        }


        protected void onPostExecute(Boolean result) {
            System.out.print(result);
        }
    }

}