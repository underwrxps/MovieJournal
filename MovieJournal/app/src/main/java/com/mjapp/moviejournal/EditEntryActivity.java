package com.mjapp.moviejournal;

import android.content.Intent;
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

public class EditEntryActivity extends AppCompatActivity {
    TextView tv_editEntryTitle;

    EditText et_editEntryMovieName, et_editEntryJournalEntry;

    Button btn_cancelEntryButton;
    Button btn_saveEntryButton;

    int entryID;
    String movieName, reviewText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_entry);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_cancelEntryButton = findViewById(R.id.btn_editEntryCancel);
        btn_saveEntryButton = findViewById(R.id.btn_editEntrySaveEntryButton);
        tv_editEntryTitle = findViewById(R.id.tv_editEntryTitle);
        et_editEntryMovieName = findViewById(R.id.et_editEntryMovieName);
        et_editEntryJournalEntry = findViewById(R.id.et_editEntryJournalEntry);

        Bundle b = getIntent().getExtras();
        if (b != null)
        {
            entryID = b.getInt("entryID");
            movieName = b.getString("movieName");
            reviewText = b.getString("reviewText");
        }

        et_editEntryMovieName.setText(movieName);
        et_editEntryJournalEntry.setText(reviewText);

        // Cancel Button Click Listener
        btn_cancelEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the View Entry (EditOrDeleteActivity)
                Intent intent = new Intent(EditEntryActivity.this, EditOrDeleteActivity.class);

                // Pass back the same entry details so it can be displayed
                intent.putExtra("entryID", entryID);
                intent.putExtra("movieName", et_editEntryMovieName.getText().toString());
                intent.putExtra("reviewText", et_editEntryJournalEntry.getText().toString());

                startActivity(intent); // Open View Entry Page
                finish(); // Close Edit Entry Page
            }
        });

        // button listener for save entry button
        btn_saveEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(EditEntryActivity.this);
                dataBaseHelper.editEntry(entryID, et_editEntryMovieName.getText().toString(), et_editEntryJournalEntry.getText().toString());
                finish();
            }
        });
    }
}