package com.example.moviejournal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NewEntryActivity extends AppCompatActivity {
    int entryIDCounter = 0;

    TextView tv_newEntryTitle;

    EditText et_newEntryMovieName, et_newEntryJournalEntry;

    Button btn_saveEntryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_entry);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_saveEntryButton = findViewById(R.id.btn_newEntrySaveEntryButton);
        tv_newEntryTitle = findViewById(R.id.tv_newEntryTitle);
        et_newEntryMovieName = findViewById(R.id.et_newEntryMovieName);
        et_newEntryJournalEntry = findViewById(R.id.et_newEntryJournalEntry);


        // button listener for save entry button
        btn_saveEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JournalEntryModel jeModel;
                try {
                    jeModel = new JournalEntryModel(entryIDCounter, et_newEntryMovieName.getText().toString(), et_newEntryJournalEntry.getText().toString());
                    entryIDCounter++;
                    Toast.makeText(NewEntryActivity.this, jeModel.toString(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) {
                    Toast.makeText(NewEntryActivity.this, "Error adding entry", Toast.LENGTH_SHORT).show();
                    jeModel = new JournalEntryModel(-1, "ERROR", "ERROR");
                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(NewEntryActivity.this);
                boolean success = dataBaseHelper.addEntry(jeModel);
                Toast.makeText(NewEntryActivity.this, "Success = " + success, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}