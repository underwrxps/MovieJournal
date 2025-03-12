package com.mjapp.moviejournal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    // Constants for the name of the database and it's columns
    public static final String JOURNAL_TABLE = "JOURNAL_TABLE";
    public static final String COLUMN_MOVIE_NAME = "MOVIE_NAME";
    public static final String COLUMN_REVIEW_TEXT = "REVIEW_TEXT";
    public static final String COLUMN_ID = "ID";

    // Constructor
    public DataBaseHelper(@Nullable Context context) {
        super(context, "JournalEntry.db", null, 1);
    }

    // Called the first time a database is accessed.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + JOURNAL_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_MOVIE_NAME + " TEXT, " + COLUMN_REVIEW_TEXT + " TEXT)";

        db.execSQL(createTableStatement);
    }

    // Called whenever the version number changes.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Called to add an entry to the database
    public boolean addEntry(JournalEntryModel journalEntry) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Basically a hashmap
        ContentValues cv = new ContentValues();

        // Don't need to add the entry ID because it auto increments (See line 30)
        cv.put(COLUMN_MOVIE_NAME, journalEntry.getMovieName());
        cv.put(COLUMN_REVIEW_TEXT, journalEntry.getReviewText());

        // Returns 1 if the entry was added successfully, -1 otherwise
        long insert = db.insert(JOURNAL_TABLE, null, cv);

        db.close();
        return insert != -1;
    }


    // idk what's going on here, but the query works and updates the table, but the cursor.moveToFirst comes back as false so idk whats up with that.
    public boolean editEntry(int entryID, String movieName, String reviewText) {
        SQLiteDatabase db = this.getWritableDatabase();

        String queryString = String.format("UPDATE %s SET (%s, %s) = ('%s', '%s') WHERE %s = %s", JOURNAL_TABLE, COLUMN_MOVIE_NAME, COLUMN_REVIEW_TEXT, movieName, reviewText, COLUMN_ID, entryID);

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            db.close();
            cursor.close();
            return true;
        }
        else {
            db.close();
            cursor.close();
            return false;
        }
    }

    // Method to delete an entry
    public boolean deleteEntry (JournalEntryModel journalEntry) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Our query string
        String queryString = String.format("DELETE FROM %s WHERE %s = '%s'", JOURNAL_TABLE, COLUMN_MOVIE_NAME, journalEntry.getMovieName());

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            // Clean up
            cursor.close();
            db.close();

            return true;
        }
        else {
            // Clean up
            cursor.close();
            db.close();

            return false;
        }
    }

    // Method to get all the entries in the database
    public List<JournalEntryModel> getAllEntries(String sortOrder) {
        List<JournalEntryModel> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + JOURNAL_TABLE;

        if (sortOrder.equals("Alphabetically (A - Z)")) {
            queryString += " ORDER BY LOWER(" + COLUMN_MOVIE_NAME + ") ASC"; // Case-insensitive A-Z
        } else if (sortOrder.equals("Alphabetically (Z - A)")) {
            queryString += " ORDER BY LOWER(" + COLUMN_MOVIE_NAME + ") DESC"; // Case-insensitive Z-A
        } else if (sortOrder.equals("Date Created (New to Old)")) {
            queryString += " ORDER BY " + COLUMN_ID + " DESC";
        } else if (sortOrder.equals("Date Created (Old to New)")) {
            queryString += " ORDER BY " + COLUMN_ID + " ASC";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int entryID = cursor.getInt(0);
                String movieName = cursor.getString(1);
                String reviewText = cursor.getString(2);
                returnList.add(new JournalEntryModel(entryID, movieName, reviewText));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return returnList;
    }

    public List<JournalEntryModel> searchEntries(String query, List<JournalEntryModel> allEntries) {
        List<JournalEntryModel> filteredList = new ArrayList<>();
        for (JournalEntryModel entry : allEntries) {
            if (entry.getMovieName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(entry);
            }
        }
        return filteredList;
    }

    // Method to get a single journal entry from the database
    public JournalEntryModel getSingleEntry(String movieNameToGet) {
        JournalEntryModel journalEntry = new JournalEntryModel();

        // Query string for the database
        String queryString = "SELECT * FROM " + JOURNAL_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        // The query returns a cursor which is basically a collection of data
        Cursor cursor = db.rawQuery(queryString, null);

        // If the entries are successfully extracted from the database
        if (cursor.moveToFirst()) {
            // Iterate through all the entries
            do {
                int entryID = cursor.getInt(0);
                String movieNameFromDB = cursor.getString(1);
                String reviewText = cursor.getString(2);

                // If the movie name from the database matched the movie name we are tyring to get
                if (movieNameFromDB.equals(movieNameToGet)) {
                    journalEntry.setEntryID(entryID);
                    journalEntry.setMovieName(movieNameFromDB);
                    journalEntry.setReviewText(reviewText);
                    break;
                }
            } while (cursor.moveToNext());
        }
        else {
            // The query failed. Don't add anything
        }

        // Clean up
        cursor.close();
        db.close();

        return journalEntry;
    }
}
