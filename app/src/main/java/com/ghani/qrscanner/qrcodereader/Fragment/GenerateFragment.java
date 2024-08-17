package com.ghani.qrscanner.qrcodereader.Fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghani.qrscanner.qrcodereader.DatabaseHelper;
import com.ghani.qrscanner.qrcodereader.databinding.FragmentGenerateBinding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


public class GenerateFragment extends Fragment {

    FragmentGenerateBinding binding;

    DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGenerateBinding.inflate(inflater, container, false);

        databaseHelper = new DatabaseHelper(getContext());

        binding.btnGen.setOnClickListener(v -> {

            String text = binding.edQrTxt.getText().toString();

            generateQrCode(text);

            databaseHelper.insertData("Generated", text);

            binding.helperText.setVisibility(View.GONE);
            binding.qrImage.setVisibility(View.VISIBLE);

        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void generateQrCode(String content)
    {

        try {
            // create Qr Writer

            Writer writer = new QRCodeWriter();

            // Encode the content as a BitMatrix

            BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, 300, 300);

            // Convert the BitMatrix to a Bitmap

            int width = matrix.getWidth();
            int height = matrix.getHeight();

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            for (int i = 0; i < width; i++) {

                for (int j = 0; j < height; j++) {

                    bitmap.setPixel(i, j, matrix.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }

            binding.qrImage.setImageBitmap(bitmap);

        } catch (WriterException e){

            e.printStackTrace();
        }
    }
}