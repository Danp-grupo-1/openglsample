package com.idnp.openglsample;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraView extends SurfaceView implements Callback {
    @TargetApi(21)
    private Camera camera;

    public CameraView(Context context) {
        super(context);
        // We're implementing the Callback interface and want to get notified
        // about certain surface events.
        getHolder().addCallback(this);
        // We're changing the surface to a PUSH surface, meaning we're receiving
        // all buffer data from another component - the camera, in this case.
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @TargetApi(21)
    public void surfaceCreated(SurfaceHolder holder) {
        // Once the surface is created, simply open a handle to the camera hardware.
        camera = Camera.open(0);
    }

    @TargetApi(21)
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // This method is called when the surface changes, e.g. when it's size is set.
        // We use the opportunity to initialize the camera preview display dimensions.
        Camera.Parameters p = camera.getParameters();
        p.setPreviewSize(width, height);
        camera.setParameters(p);

        // We also assign the preview display to this surface...
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // ...and start previewing. From now on, the camera keeps pushing preview
        // images to the surface.
        camera.startPreview();
    }

    @TargetApi(21)
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Once the surface gets destroyed, we stop the preview mode and release
        // the whole camera since we no longer need it.
        camera.stopPreview();
        camera.release();
        camera = null;
    }
}