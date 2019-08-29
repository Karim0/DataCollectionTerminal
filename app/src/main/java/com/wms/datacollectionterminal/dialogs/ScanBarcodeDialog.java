package com.wms.datacollectionterminal.dialogs;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.wms.datacollectionterminal.R;
import com.wms.datacollectionterminal.helpers.CameraPreview;

public class ScanBarcodeDialog extends DialogFragment {

    Camera camera;
    CameraPreview cameraPreview;
    BarcodeScanResult barcodeScanResult;
    String barcode = "";

    public void setBarcodeScanResult(BarcodeScanResult barcodeScanResult) {
        this.barcodeScanResult = barcodeScanResult;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.scan_barcode_dialog, container);

        camera = Camera.open();
        cameraPreview = new CameraPreview(root.getContext(), camera, (SurfaceView) root.findViewById(R.id.surface),
                new Detector.Processor<Barcode>() {
                    @Override
                    public void release() {
//                        Log.i("myLogs", intentData);
                        Toast.makeText(root.getContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void receiveDetections(Detector.Detections<Barcode> detections) {
                        final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                        if (barcodes.size() != 0) {
                            barcode = barcodes.valueAt(0).displayValue;
                            Log.i("myLogs", barcodes.valueAt(0).displayValue);
                            dismiss();
                        }
                    }
                });
//        FrameLayout preview = root.findViewById(R.id.camera_preview);
//        preview.addView(cameraPreview);
//
//        detector = new BarcodeDetector.Builder(root.getContext())
//                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
//                .build();

        return root;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        camera.stopPreview();
        camera.release();
        barcodeScanResult.result(barcode);
        super.onDismiss(dialog);
    }
}
