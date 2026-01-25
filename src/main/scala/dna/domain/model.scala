package dna.domain

import java.time.Instant

type TrackUri = String
type Artist = String
type Album = String
type Track = String
type MsPlayed = Int
type Country = String

case class ListenEntry(timestamp: Instant,
                       msPlayed: MsPlayed,
                       track: Option[Track],
                       artist: Option[Artist],
                       album: Option[Album],
                       trackUri: Option[TrackUri],
                       platform: String,
                       country: Country,
                       reasonStart: String,
                       reasonEnd: String,
                       skipped: Option[Boolean],
                       offline: Boolean,
                       incognitoMode: Boolean)