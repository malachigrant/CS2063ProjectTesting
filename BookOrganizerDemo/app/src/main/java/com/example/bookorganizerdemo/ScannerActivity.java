package com.example.bookorganizerdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.example.bookorganizerdemo.model.Book;
import com.example.bookorganizerdemo.util.HttpRequestUtil;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScannerActivity extends AppCompatActivity {

    private static final String TAG = "ScannerActivity";

    Button button;
    SurfaceView surface;
    CameraSource camera;

    String isbn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 0);

        button = findViewById(R.id.scanButton);
        surface = findViewById(R.id.cameraSurface);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Book.isISBN(isbn)) {
                    HttpRequestUtil httpRequestUtil = new HttpRequestUtil(ScannerActivity.this);
                    httpRequestUtil.requestByISBN(isbn, books -> {
                        if (books.size() > 0) {
                            Intent intent = new Intent();
                            intent.putExtra("scannedBook", books.get(0));
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                    });
                }
            }
        });

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.EAN_8 | Barcode.EAN_13 | Barcode.CODE_128 | Barcode.UPC_A
                        | Barcode.UPC_E | Barcode.CODE_39 | Barcode.CODE_93 | Barcode.ITF | Barcode.CODABAR)
                .build();

        camera = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(640, 480)
                .build();

        surface.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    camera.start(surface.getHolder());
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                camera.stop();
            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() > 0) {
                    button.post(() -> button.setEnabled(true));
                    isbn = barcodes.valueAt(0).displayValue;
                } else {
                    button.post(() -> button.setEnabled(false));
                }
            }
        });
    }
}
