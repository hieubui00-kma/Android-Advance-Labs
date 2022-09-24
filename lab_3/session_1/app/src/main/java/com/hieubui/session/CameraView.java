package com.hieubui.session;

import static com.hieubui.session.Orientation.getOrientation;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.io.IOException;

@SuppressWarnings("deprecation")
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {
    private Camera camera;

    public CameraView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        if (camera == null) {
            return;
        }

        setupCamera(surfaceHolder);
    }

    private void setupCamera(@NonNull SurfaceHolder surfaceHolder) {
        if (camera == null) {
            Log.w("CameraView", "The camera is not open!");
            return;
        }

        Activity activity = (Activity) getContext();
        int orientation = getOrientation(activity);

        try {
            camera.setDisplayOrientation(orientation);
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (camera == null) {
            return;
        }

        camera.stopPreview();
        setupCamera(surfaceHolder);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        if (camera != null) {
            camera.stopPreview();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (camera != null) {
            camera.release();
        }
        super.onDetachedFromWindow();
    }

    public void setup() {
        camera = Camera.open();
        setupCamera(getHolder());
    }

    public void setZoom(float zoomRatio) {
        if (camera == null) {
            Log.w("CameraView", "The camera is not setup!");
            return;
        }

        Camera.Parameters parameters = camera.getParameters();

        if (parameters.isZoomSupported()) {
            int maxZoom = parameters.getMaxZoom();
            int zoomValue = (int) zoomRatio * maxZoom;

            parameters.setZoom(zoomValue);
            camera.setParameters(parameters);
        }
    }

    public void takePhoto(Camera.PictureCallback pictureCallback) {
        if (camera == null) {
            Log.w("CameraView", "The camera is not setup!");
            return;
        }

        camera.takePicture(null, null, (bytes, camera) -> {
            pictureCallback.onPictureTaken(bytes, camera);
            camera.startPreview();
        });
    }
}
