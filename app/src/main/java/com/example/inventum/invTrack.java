package com.example.inventum;

import java.io.Serializable;

/**
 * Inventum track object storing track specific information. This only holds track art, Title, and
 * artist.
 *
 * Primarily for testing purposes as of now, will likely use spotify "Track" class when ready
 *
 */
public class invTrack implements Serializable {

    String trackID; //API side track identification
    String image_url;

    //descriptive track meta data
    String trackTitle;
    String[] trackArtist;
    String trackArtistUI = "";
    String trackAlbum;
    String albumType;
    String popularity;
    String danceability;
    String liveness;
    String tempo;
    String acousticness;
    String valence;
    String energy;

    // Other metadata
    String speechiness;
    String loudness;
    String instrumentalness;
    String duration_ms;

    public invTrack(String id, String title, String[] artist, String album, String albumType, String image_url, String energy,
                    String danceability, String liveness, String acousticness, String popularity, String valence, String tempo,
                    String speechiness, String loudness, String instrumentalness, String duration_ms) {
        this.trackID = id;
        this.trackTitle = title;
        this.trackArtist = artist;
        this.trackAlbum = album;
        this.albumType = albumType;
        this.image_url = image_url;
        this.energy = energy;
        this.danceability = danceability;
        this.liveness = liveness;
        this.acousticness = acousticness;
        this.popularity = popularity;
        this.valence = valence;
        this.tempo = tempo;
        this.speechiness = speechiness;
        this.loudness = loudness;
        this.instrumentalness = instrumentalness;
        this.duration_ms = duration_ms;

        for (int i = 0; i < artist.length; i++) {
            if (artist.length == 1) {
                this.trackArtistUI = artist[0];
            } else {
                this.trackArtistUI = trackArtistUI + ", " + artist[i];
            }
        }

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
        return energy;
    }

    public String getAlbumType() {return albumType;}

    public String getDanceability() {return danceability;}

    public String getPopularityScore() {return popularity;}

    public String getLiveness() {return liveness;}

    public String getTempo() {return tempo;}

    public String getAcousticness() {return acousticness;}

    public String getValence() {return valence;}

    public String getTrackArtistUI() {return trackArtistUI;}
}
