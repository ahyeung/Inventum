package com.example.inventum;

/**
 * Inventum track object storing track specific information. This only holds track art, Title, and
 * artist.
 *
 * Primarily for testing purposes as of now, will likely use spotify "Track" class when ready
 *
 */
public class invTrack {

    String trackTitle;
    String trackArtist;

    public invTrack(String title, String artist) {
        this.trackTitle = title;
        this.trackArtist = artist;
    }

}
