package com.example.moviejournal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    // Method to delete an entry
    public boolean deleteEntry (JournalEntryModel journalEntry) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Our query string
        String queryString = "DELETE FROM " + JOURNAL_TABLE + " WHERE " + COLUMN_MOVIE_NAME + " = " + journalEntry.getMovieName();

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

    // Method to update an existing entry for a movie. Takes in a JournalEntryModel object. That object should have the movie name be the same
    // as the one we are trying to update. The @reviewText of the object will be the new entry
    public boolean updateEntry(JournalEntryModel journalEntry) {
        if (deleteEntry(journalEntry)) {
            // Return true if the old entry for the movie was successfully deleted and the new entry was successfully added. Otherwise return false
            return addEntry(journalEntry);
        }
        else {
            // A journal entry doesn't exist for that movie name
            return false;
        }
    }

    // Method to get all the entries in the database
    public List<JournalEntryModel> getAllEntries() {
        List<JournalEntryModel> returnList = new ArrayList<>();

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
                String movieName = cursor.getString(1);
                String reviewText = cursor.getString(2);

                JournalEntryModel newJournalEntry = new JournalEntryModel(entryID, movieName, reviewText);
                returnList.add(newJournalEntry);

            } while (cursor.moveToNext());
        }
        else {
            // The query failed. Don't add anything
        }

        // Clean up
        cursor.close();
        db.close();

        return returnList;
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
