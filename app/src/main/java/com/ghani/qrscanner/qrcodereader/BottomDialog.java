package com.ghani.qrscanner.qrcodereader;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BottomDialog extends BottomSheetDialogFragment {

    private String fetchUrl;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_dialog, container, false);

        TextView time = view.findViewById(R.id.txt_date);
        TextView result = view.findViewById(R.id.txt_result);
        CardView btn_search = view.findViewById(R.id.search);
        CardView btn_share = view.findViewById(R.id.share);
        CardView btn_copy = view.findViewById(R.id.copy);
        ImageView btn_close = view.findViewById(R.id.close);

        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());

        result.setText(fetchUrl);

        databaseHelper.insertData("Scanned", fetchUrl);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy hh:mm a");
        String dateTime = dateFormat.format(calendar.getTime());

        time.setText(dateTime+ " EAN_8");

        btn_close.setOnClickListener(v -> dismiss());

        btn_search.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, fetchUrl); // query contains search string
            startActivity(intent);

        });

        btn_share.setOnClickListener(v -> {
            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share));
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, fetchUrl);
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
        });

        btn_copy.setOnClickListener(v -> {

            ClipboardManager clipboardManager =
                    (ClipboardManager) requireContext().getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("label", fetchUrl);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(getContext(), "text copied...", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    public void fetchUrl(String url)
    {
        ExecutorService service = Executors.newSingleThreadExecutor();

        service.execute(() -> fetchUrl = url);
    }
}
