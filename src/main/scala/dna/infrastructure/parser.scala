package dna.infrastructure

import dna.domain.ListenEntry
import io.circe.Decoder
import io.circe.parser.*
import io.circe.generic.semiauto.deriveDecoder

import java.time.Instant

object SpotifyJsonParse:

  private case class RawListenEntry(
                                     ts: String,
                                     ms_played: Int,
                                     master_metadata_track_name: Option[String],
                                     master_metadata_album_artist_name: Option[String],
                                     master_metadata_album_album_name: Option[String],
                                     spotify_track_uri: Option[String],
                                     platform: String,
                                     conn_country: String,
                                     reason_start: String,
                                     reason_end: String,
                                     skipped: Option[Boolean],
                                     incognito_mode: Boolean,
                                     offline: Boolean
                                   )

  private given Decoder[RawListenEntry] = deriveDecoder[RawListenEntry]

  def parseFile(content: String): Either[io.circe.Error, List[ListenEntry]] =
    parse(content)
      .flatMap(_.as[List[RawListenEntry]])
      .map(_.map(toDomain))

  private def toDomain(raw: RawListenEntry): ListenEntry =
    ListenEntry(
      timestamp = Instant.parse(raw.ts),
      track = raw.master_metadata_track_name,
      artist = raw.master_metadata_album_artist_name,
      album = raw.master_metadata_album_album_name,
      msPlayed = raw.ms_played,
      trackUri = raw.spotify_track_uri,
      platform = raw.platform,
      country = raw.conn_country,
      reasonStart = raw.reason_start,
      reasonEnd = raw.reason_end,
      skipped = raw.skipped,
      incognitoMode = raw.incognito_mode,
      offline = raw.offline
    )