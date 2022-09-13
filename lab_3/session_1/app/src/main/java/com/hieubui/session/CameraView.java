package com.hieubui.session;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.io.IOException;

@SuppressWarnings("deprecation")
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {
    private Camera camera;

    public CameraView(Context context) {
        super(context);
        setupComponents();
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupComponents();
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupComponents();
    }

    private void setupComponents() {
        camera = Camera.open();
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        setupCamera(surfaceHolder);
    }

    private void setupCamera(@NonNull SurfaceHolder surfaceHolder) {
        int orientation = getOrientation();

        try {
            camera.setDisplayOrientation(orientation);
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getOrientation() {
        Activity activity = (Activity) getContext();
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Camera.CameraInfo cameraInfo = new android.hardware.Camera.CameraInfo();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                break;

            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        Camera.getCameraInfo(0, cameraInfo);
        return (cameraInfo.orientation - degrees + 360) % 360;
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        camera.stopPreview();
        setupCamera(surfaceHolder);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera.release();
    }

    public void setZoom(float zoomRatio) {
        Camera.Parameters parameters = camera.getParameters();

        if (parameters.isZoomSupported()) {
            int maxZoom = parameters.getMaxZoom();
            int zoomValue = (int) zoomRatio * maxZoom;

            parameters.setZoom(zoomValue);
            camera.setParameters(parameters);
        }
    }

    public void takePhoto(Camera.PictureCallback pictureCallback) {
        camera.takePicture(null, null, pictureCallback);
    }
}
