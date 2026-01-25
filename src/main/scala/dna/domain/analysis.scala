package dna.domain

import java.time.Duration
import java.time.temporal.ChronoField

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
