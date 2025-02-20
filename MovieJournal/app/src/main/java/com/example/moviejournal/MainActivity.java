package com.example.moviejournal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    // references to buttons and other controls on the layout
    TextView tv_title;
    Button btn_newEntry;
    ListView lv_previousEntries;
    DataBaseHelper dataBaseHelper;
    ArrayAdapter entriesArrayAdapter;

    int entryIDCounter = 0;

    @Override
    protected void onResume() {
        super.onResume();
        ShowAllEntriesInListView(dataBaseHelper);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tv_title = findViewById(R.id.tv_title);
        btn_newEntry = findViewById(R.id.btn_newEntry);
        lv_previousEntries = findViewById(R.id.lv_previousEntries);

        dataBaseHelper = new DataBaseHelper(MainActivity.this);
        ShowAllEntriesInListView(dataBaseHelper);

        // button listener for new entry button
        btn_newEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // launch new entry activity
                Intent intent = new Intent(MainActivity.this, NewEntryActivity.class);
                startActivity(intent);
            }
        });

        lv_previousEntries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JournalEntryModel clickedEntry = (JournalEntryModel) parent.getItemAtPosition(position);
                Bundle b = new Bundle();
                b.putInt("entryID", clickedEntry.getEntryID());
                b.putString("movieName", clickedEntry.getMovieName());
                b.putString("reviewText", clickedEntry.getReviewText());
                Intent intent = new Intent(MainActivity.this, EditOrDeleteActivity.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    private void ShowAllEntriesInListView(DataBaseHelper dataBaseHelper) {
        entriesArrayAdapter = new ArrayAdapter<JournalEntryModel>(MainActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper.getAllEntries());
        lv_previousEntries.setAdapter(entriesArrayAdapter);
    }
}