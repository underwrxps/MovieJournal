package com.example.moviejournal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditOrDeleteActivity extends AppCompatActivity {

    TextView tv_viewEntryMovieName, tv_viewEntryJournalEntry;

    Button btn_editButton, btn_deleteButton;

    int entryID;
    String movieName, reviewText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_or_delete);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_editButton = findViewById(R.id.btn_editButton);
        btn_deleteButton = findViewById(R.id.btn_deleteButton);
        tv_viewEntryMovieName = findViewById(R.id.tv_viewEntryMovieName);
        tv_viewEntryJournalEntry = findViewById(R.id.tv_viewEntryJournalEntry);
        tv_viewEntryJournalEntry.setMovementMethod(new android.text.method.ScrollingMovementMethod());

        Bundle b = getIntent().getExtras();
        if (b != null)
        {
            entryID = b.getInt("entryID");
            movieName = b.getString("movieName");
            reviewText = b.getString("reviewText");
        }
        tv_viewEntryMovieName.setText(movieName);
        tv_viewEntryJournalEntry.setText(reviewText);

        btn_editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putInt("entryID", entryID);
                b.putString("movieName", movieName);
                b.putString("reviewText", reviewText);
                Intent intent = new Intent(EditOrDeleteActivity.this, EditEntryActivity.class);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });

        btn_deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a confirmation dialog
                new androidx.appcompat.app.AlertDialog.Builder(EditOrDeleteActivity.this)
                        .setTitle("Confirm Deletion")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            // User confirmed deletion
                            DataBaseHelper dataBaseHelper = new DataBaseHelper(EditOrDeleteActivity.this);
                            JournalEntryModel entryToDelete = dataBaseHelper.getSingleEntry(movieName);
                            dataBaseHelper.deleteEntry(entryToDelete);
                            Toast.makeText(EditOrDeleteActivity.this, "Deleted entry for " + movieName, Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            // User canceled, do nothing
                            dialog.dismiss();
                        })
                        .show();
            }
        });

    }
}