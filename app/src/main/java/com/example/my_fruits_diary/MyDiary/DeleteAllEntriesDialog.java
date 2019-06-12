package com.example.my_fruits_diary.MyDiary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.my_fruits_diary.DataHandling.DataHandler;
import com.example.my_fruits_diary.MainActivity;

public class DeleteAllEntriesDialog extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Information")
                .setMessage("Are you sure you would like to delete all entries?")
                .setPositiveButton("ok", (dialog, which) -> {
                    DataHandler dataHandler = new DataHandler();
                    dataHandler.onDeleteAllEntries();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton("cancel", (dialog, which) -> dismiss());
        return builder.create();
    }
}
