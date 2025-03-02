package com.example.moviejournal;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
    EditText et_searchBar;
    Spinner sp_sortOptions;
    ListView lv_previousEntries;
    DataBaseHelper dataBaseHelper;
    ArrayAdapter<JournalEntryModel> entriesArrayAdapter;
    List<JournalEntryModel> journalEntries;

    int entryIDCounter = 0;

    @Override
    protected void onResume() {
        super.onResume();
        updateListView();
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
        et_searchBar = findViewById(R.id.et_searchBar);
        sp_sortOptions = findViewById(R.id.sp_sortOptions);

        dataBaseHelper = new DataBaseHelper(MainActivity.this);
        // Initialize sorting options
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Alphabetically", "Date Created (New to Old)", "Date Created (Old to New)"});
        sp_sortOptions.setAdapter(sortAdapter);

        updateListView();

        // Search functionality
        et_searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterList(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Sorting functionality
        sp_sortOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

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

    // Updates ListView based on sorting and search
    private void updateListView() {
        String sortOrder = sp_sortOptions.getSelectedItem().toString();
        journalEntries = dataBaseHelper.getAllEntries(sortOrder);
        entriesArrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, journalEntries);
        lv_previousEntries.setAdapter(entriesArrayAdapter);
        filterList(et_searchBar.getText().toString());
    }

    // Filters the list based on search input
    private void filterList(String query) {
        List<JournalEntryModel> filteredList = dataBaseHelper.searchEntries(query, journalEntries);
        entriesArrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, filteredList);
        lv_previousEntries.setAdapter(entriesArrayAdapter);
    }
}