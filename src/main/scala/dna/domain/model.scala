package dna.domain

type TrackUri = String
type Artist = String
type Album = String

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