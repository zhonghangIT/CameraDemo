package com.lz.longyang.camerademo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;

import com.lz.longyang.camerademo.R;

public class MainActivity extends AppCompatActivity {

    private Camera camera;//相机

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceView.setKeepScreenOn(true);//保持屏幕的高亮
        //获取SurfaceHolder
        SurfaceHolder holder = surfaceView.getHolder();
        //让数据直接输出
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //给SurfaceView添加创建监听
        holder.addCallback(new MyCallback());
    }

    private class MyCallback implements Callback{

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            /**
             * 步骤：
             * 1 打开相机
             * 2 设置预览显示
             * 3 开始预览
             */
            try {
                //1 打开相机   选择的是硬件
                camera = Camera.open();
                //2 设置预览显示
                camera.setPreviewDisplay(holder);
                //3 开始预览
                camera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            camera.release();//释放硬件资源
            camera = null;
        }

    }
    public void focus(View v){
        //对焦
        camera.autoFocus(null);//自动对焦都是硬件来完成
    }
    public void takepicture(View v){
        //拍照
        //shutter 快门, raw 相机捕获的原始数据, jpeg 相机处理后的数据   其实这三个参数都是接口
        camera.takePicture(null, null, new MyPictureCallback());
    }

    //相机处理完成照片后的监听类
    private class MyPictureCallback implements PictureCallback{

        //处理完成照片后  自动调用该方法   相机只能同时干一件事件   在处理照片的时候  相机是被占用的
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try {
                //把相机的数据保存在sdcard   权限
                File file = new File(Environment.getExternalStorageDirectory(),System.currentTimeMillis()+"bjlingzhuo.jpg");
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(data);
                fos.close();
                //重新预览
                camera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
