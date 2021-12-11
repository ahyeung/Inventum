package com.example.inventum;

/**
 * Inventum track object storing track specific information. This only holds track art, Title, and
 * artist.
 *
 * Primarily for testing purposes as of now, will likely use spotify "Track" class when ready
 *
 */
public class invTrack {

    String trackID; //API side track identification
    String image_url;

    //descriptive track meta data
    String trackTitle;
    String[] trackArtist;
    String trackArtistUI;
    String trackAlbum;
    String albumType;
    String popularity;
    String danceability;
    String liveness;
    String tempo;
    String acousticness;
    String valence;
    String trackEnergy;

    public invTrack(String id, String title, String[] artist, String album, String image_url, String energy) {
        this.trackID = id;
        this.trackTitle = title;
        this.trackArtist = artist;
        this.trackAlbum = album;
        this.image_url = image_url;
        this.trackEnergy = energy;
    }

    public String getID() {
        return trackID;
    }

    public String getTitle() {
        return trackTitle;
    }

    public String[] getTrackArtist() {
        return trackArtist;
    }

    public String getAlbum() {
        return trackAlbum;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getEnergy() {
        return trackEnergy;
    }

    public String getAlbumType() {return albumType;}

    public String getDanceability() {return danceability;}

    public String getPopularityScore() {return popularity;}

    public String getLiveness() {return liveness;}

    public String getTempo() {return tempo;}

    public String getAcousticness() {return acousticness;}

    public String getValence() {return valence;}
}
