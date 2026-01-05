import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonProperty}

@JsonIgnoreProperties(ignoreUnknown = true)
case class SpotifyEntry(@JsonProperty("ts")timestamp: String,
                        @JsonProperty("master_metadata_track_name")track: String,
                        @JsonProperty("master_metadata_album_artist_name")artist: String,
                        @JsonProperty("master_metadata_album_album_name")album: String,
                        @JsonProperty("reason_start")reasonStart: String,
                        @JsonProperty("reason_end")reasonEnd: String,
                        skipped: Boolean,
                        offline: Boolean,
                        @JsonProperty("incognito_mode")incognitoMode: Boolean)