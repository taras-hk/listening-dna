package com.listeningdna;

import jakarta.json.JsonObject;

import java.time.Instant;
import java.util.Optional;

public record SpotifyEntry(
        String timestamp,
        int msPlayed,
        String track,
        String artist,
        String album,
        String reasonStart,
        String reasonEnd,
        Boolean skipped,
        boolean offline,
        boolean incognitoMode
) {

    public Instant timestampAsInstant() {
        return Instant.parse(timestamp);
    }

    public Optional<String> trackOpt() {
        return Optional.ofNullable(track);
    }

    public Optional<Boolean> skippedOpt() {
        return Optional.ofNullable(skipped);
    }

    public static SpotifyEntry from(JsonObject json) {
        return new SpotifyEntry(
                json.getString("ts"),
                json.getInt("ms_played"),
                // Handle Option/Null safely:
                json.isNull("master_metadata_track_name") ? null : json.getString("master_metadata_track_name"),
                json.isNull("master_metadata_album_artist_name") ? null : json.getString("master_metadata_album_artist_name"),
                json.isNull("master_metadata_album_album_name") ? null : json.getString("master_metadata_album_album_name"),
                json.getString("reason_start"),
                json.getString("reason_end"),
                // Handle Boolean Wrapper
                json.isNull("skipped") ? null : json.getBoolean("skipped"),
                json.getBoolean("offline"),
                json.getBoolean("incognito_mode")
        );
    }
}