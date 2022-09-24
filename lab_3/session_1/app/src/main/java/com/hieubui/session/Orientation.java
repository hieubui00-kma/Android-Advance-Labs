package com.hieubui.session;

import android.app.Activity;
import android.hardware.Camera;
import android.view.Surface;

@SuppressWarnings("deprecation")
public class Orientation {

    public static int getOrientation(Activity activity) {
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
}
