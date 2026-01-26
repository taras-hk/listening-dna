package dna.domain

import java.time.{Duration, ZoneId}

object Analysis:

  def detectSessions(listens: List[ListenEntry], sessionDuration: Duration): List[List[ListenEntry]] =
    listens match
      case Nil => Nil
      case head :: tail =>
        tail
          .foldLeft(List(List(head))) { (sessions, entry) =>
            val currentSession = sessions.head

            val gap = Duration.between(currentSession.head.timestamp, entry.timestamp)

            if gap.compareTo(sessionDuration) >= 0
            then List(entry) :: sessions
            else (entry :: currentSession) :: sessions.tail
          }
          .map(_.reverse)
          .reverse

  def calculateLongestSessionByTracks(sessions: List[List[ListenEntry]]): List[ListenEntry] =
    sessions.maxBy(_.length)

  def calculateShortestSessionByTracks(sessions: List[List[ListenEntry]]): List[ListenEntry] =
    sessions.minBy(_.length)

  def calculateLongestSessionByDuration(sessions: List[List[ListenEntry]]): List[ListenEntry] =
    sessions.maxBy(_.map(_.msPlayed).sum)

  def calculateShortestSessionByDuration(sessions: List[List[ListenEntry]]): List[ListenEntry] =
    sessions.minBy(_.map(_.msPlayed).sum)

  def allTimeTopArtistByListenTime(listens: List[ListenEntry], top: Int = 10): List[(Artist, MsPlayed)] =
    case class ArtistListenTime(name: Artist, msPlayed: MsPlayed)
    listens
      .flatMap(entry => entry.artist.map(artist => ArtistListenTime(artist, entry.msPlayed)))
      .groupMapReduce(_.name)(_.msPlayed)(_ + _)
      .toList
      .sortBy(-_._2)
      .take(top)

  def allTimeTopArtists(listens: List[ListenEntry], top: Int = 10): List[(Artist, Int)] =
    allTimeTopCalculator(_.artist, listens, top)

  def allTimeTopTracks(listens: List[ListenEntry], top: Int = 10): List[(Artist, Int)] =
    allTimeTopCalculator(_.track, listens, top)

  def allTimeTopAlbums(listens: List[ListenEntry], top: Int = 10): List[(Artist, Int)] =
    allTimeTopCalculator(_.album, listens, top)

  private def allTimeTopCalculator(keyExtractor: ListenEntry => Option[String], listens: List[ListenEntry], top: Int): List[(Artist, Int)] =
    listens
      .flatMap(keyExtractor)
      .groupMapReduce(identity)(_ => 1)(_ + _)
      .toList
      .sortBy(-_._2)
      .take(top)

  // TODO good for now as it is not going to move beyond my data
  private val myZone = ZoneId.of("Europe/Kyiv")

  def favouriteHourOfDay(listens: List[ListenEntry]): List[(Int, Int)] =
    listens
      .map(_.timestamp.atZone(myZone).getHour)
      .groupMapReduce(identity)(_ => 1)(_ + _)
      .toList
      .sortBy(-_._2)
      .take(1)

  def nightDwellerScore(listens: List[ListenEntry]): Double =
    listens
      .map(_.timestamp.atZone(myZone).getHour)
      .count(hour => hour >= 5 && hour <= 8) / listens.length.toDouble

  def mostSkippable(listens: List[ListenEntry], top: Int = 10): List[(Track, Int)] =
    case class FlopTrack(track: Track, reasonEnd: String)

    listens
      .filter(_.isSkipped)
      .flatMap(entry => entry.track.map(t => FlopTrack(t, entry.reasonEnd)))
      .groupMapReduce(identity)(_ => 1)(_ + _)
      .toList
      .sortBy(-_._2)
      .take(top)
      .map((ft, count) => (ft.track, count))

  def mostEnd2EndTrack(listens: List[ListenEntry], top: Int = 10): List[(Track, Int)] =
    case class TrackDone(track: Track, reasonEnd: String)

    listens
      .filter(_.isTrackDone)
      .flatMap(entry => entry.track.map(t => TrackDone(t, entry.reasonEnd)))
      .groupMapReduce(identity)(_ => 1)(_ + _)
      .toList
      .sortBy(-_._2)
      .take(top)
      .map((td, count) => (td.track, count))

  def tooAfraidTooShare(listens: List[ListenEntry], top: Int = 10): List[(Track, Int)] =
    listens
      .filter(_.incognitoMode)
      .flatMap(entry => entry.track)
      .groupMapReduce(identity)(_ => 1)(_ + _)
      .toList
      .sortBy(-_._2)
      .take(top)
      .map((t, count) => (t, count))
