package com.ghani.qrscanner.qrcodereader.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ghani.qrscanner.qrcodereader.Adapter;
import com.ghani.qrscanner.qrcodereader.DataModel;
import com.ghani.qrscanner.qrcodereader.DatabaseHelper;
import com.ghani.qrscanner.qrcodereader.databinding.FragmentHistoryBinding;

import java.util.List;


public class HistoryFragment extends Fragment {

    FragmentHistoryBinding binding;

    DatabaseHelper databaseHelper;
    List<DataModel> dataList;
    Adapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        databaseHelper = new DatabaseHelper(getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvHistory.setLayoutManager(layoutManager);

        dataList = databaseHelper.getAllData();

        adapter = new Adapter(getContext(), dataList);
        binding.rvHistory.setAdapter(adapter);

        if (dataList.isEmpty()){

            binding.btnClear.setVisibility(View.GONE);
        } else {

            binding.btnClear.setVisibility(View.VISIBLE);

            binding.btnClear.setOnClickListener(v -> {

                clearAllHistory();
                binding.btnClear.setVisibility(View.GONE);
            });
        }


        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void clearAllHistory() {

        dataList.clear();
        adapter.notifyDataSetChanged();

        databaseHelper.clearTable();

        Toast.makeText(getContext(), "History Cleared", Toast.LENGTH_SHORT).show();
    }

}