import upickle.default.*

case class SpotifyEntry(timestamp: String,
                        msPlayed: Int,
                        track: Option[String],
                        artist: Option[String],
                        album: Option[String],
                        reasonStart: String,
                        reasonEnd: String,
                        skipped: Option[Boolean],
                        offline: Boolean,
                        incognitoMode: Boolean)

given ReadWriter[SpotifyEntry] =
  readwriter[ujson.Obj].bimap[SpotifyEntry] (
    e =>
      ujson.Obj(
        "ts" -> e.timestamp,
        "ms_played" -> e.msPlayed,
        "master_metadata_track_name" -> e.track,
        "master_metadata_album_artist_name" -> e.artist,
        "master_metadata_album_album_name" -> e.album,
        "reason_start" -> e.reasonStart,
        "reason_end" -> e.reasonEnd,
        "incognito_mode" -> e.incognitoMode,
        "skipped" -> e.skipped,
        "offline" -> e.offline
      ),
    json =>
      SpotifyEntry(
        timestamp = json("ts").str,
        msPlayed = json("ms_played").num.toInt,
        track = json("master_metadata_track_name").strOpt,
        artist = json("master_metadata_album_artist_name").strOpt,
        album = json("master_metadata_album_album_name").strOpt,
        reasonStart = json("reason_start").str,
        reasonEnd = json("reason_end").str,
        incognitoMode = json("incognito_mode").bool,
        skipped = json("skipped").boolOpt,
        offline = json("offline").bool
      )
  )