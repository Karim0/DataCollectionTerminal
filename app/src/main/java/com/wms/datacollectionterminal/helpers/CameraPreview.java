package com.wms.datacollectionterminal.helpers;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    //    private SurfaceHolder mHolder;
    private Camera mCamera;

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;

    public CameraPreview(final Context context, Camera camera, final SurfaceView surfaceView, Detector.Processor<Barcode> processor) {
        super(context);
        mCamera = camera;
        mCamera.setDisplayOrientation(90);
//        mHolder = getHolder();
//        mHolder.addCallback(this);
//        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        surfaceView.getHolder().addCallback((new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(surfaceView.getHolder());
                } catch (
                        IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        }));

        Toast.makeText(context, "Barcode scanner started", Toast.LENGTH_SHORT)
                .show();

        barcodeDetector = new BarcodeDetector.Builder(context)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(context, barcodeDetector)
                .setRequestedPreviewSize(camera.getParameters().getPictureSize().width, camera.getParameters().getPictureSize().height)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        Log.i("myLogs", camera.getParameters().getPictureSize().width + " " + camera.getParameters().getPictureSize().height);
        barcodeDetector.setProcessor(processor);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d("myLogs", "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        cameraSource.stop();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
//        if (mHolder.getSurface() == null) {
//
//            return;
//        }

        try {
            mCamera.stopPreview();
        } catch (Exception e) {
        }

        try {
//            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e) {
            Log.d("myLogs", "Error starting camera preview: " + e.getMessage());
        }
    }

}
