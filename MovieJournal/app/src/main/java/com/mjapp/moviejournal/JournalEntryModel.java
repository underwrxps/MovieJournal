package com.mjapp.moviejournal;

public class JournalEntryModel {

    // We might not need the entryID, not sure.
    private int entryID;
    private String movieName;
    private String reviewText;

    //constructors
    public JournalEntryModel(int entryID, String movieName, String reviewText) {
        this.entryID = entryID;
        this.movieName = movieName;
        this.reviewText = reviewText;
    }

    public JournalEntryModel() {
    }

    // toString
    @Override
    public String toString() {
        return movieName;
    }

    public int getEntryID() {
        return entryID;
    }

    public void setEntryID(int entryID) {
        this.entryID = entryID;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
