package com.ghani.qrscanner.qrcodereader;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.Image;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;

public class MyImageAnalyzer implements ImageAnalysis.Analyzer {

    private FragmentManager fragmentManager;
    private BottomDialog bottomDialog;

    public MyImageAnalyzer(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        bottomDialog = new BottomDialog();
    }

    @Override
    public void analyze(@NonNull ImageProxy imageProxy) {
        scanBarcode(imageProxy);
    }

    public void analyzeBitmap(Bitmap bitmap){

        if (bitmap != null)
        {
            InputImage image = InputImage.fromBitmap(bitmap, 0);

            BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                    .build();

            BarcodeScanner scanner = BarcodeScanning.getClient(options);

            Task<List<Barcode>> result = scanner.process(image)
                    .addOnSuccessListener(this::readBarcodeData)
                    .addOnFailureListener(e -> {
                        // Task failed with an exception
                        Toast.makeText(bottomDialog.getContext(),
                                "Failed due to: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void scanBarcode(ImageProxy imageProxy) {

        if(imageProxy!=null)
        {
            @SuppressLint("UnsafeOptInUsageError")
            Image mediaImage = imageProxy.getImage();

            if (mediaImage != null)
            {
                InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo()
                        .getRotationDegrees());

                BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                        .build();

                BarcodeScanner scanner = BarcodeScanning.getClient(options);

                // Task completed successfully
                Task<List<Barcode>> result = scanner.process(image)
                        .addOnSuccessListener(this::readBarcodeData)
                        .addOnFailureListener(e -> {
                            // Task failed with an exception

                        }).addOnCompleteListener(task -> {
                            imageProxy.close();

                        });
            }

        }

    }

    private void readBarcodeData(List<Barcode> barcodes) {

        barcodes.forEach(barcode -> {

            int valueType = barcode.getValueType();

            if (valueType == Barcode.TYPE_URL)
            {
                if (!bottomDialog.isAdded())
                {
                    bottomDialog.show(fragmentManager, "");
                }
                String url = barcode.getUrl().getUrl();
                bottomDialog.fetchUrl(url);

            } else if (valueType == Barcode.TYPE_WIFI) {

                if (!bottomDialog.isAdded())
                {
                    bottomDialog.show(fragmentManager, "");
                }
                String password = barcode.getWifi().getPassword();
                bottomDialog.fetchUrl(password);

            } else {
                if (!bottomDialog.isAdded())
                {
                    bottomDialog.show(fragmentManager, "");
                }
                bottomDialog.fetchUrl(barcode.getRawValue());
            }

        });
    }
}
