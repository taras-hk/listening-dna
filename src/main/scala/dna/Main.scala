package dna

import dna.infrastructure.SpotifyJsonParse

import java.nio.file.{Files, Paths}
import scala.io.Source
import scala.jdk.CollectionConverters.*
import scala.util.Using


@main def hello(): Unit =
  val dir = Paths.get("data")

  val listens = Files.list(dir)
    .iterator()
    .asScala
    .filter(Files.isRegularFile(_))
    .flatMap { path =>
      val content = Using(Source.fromFile(path.toFile))(_.mkString).get
      SpotifyJsonParse.parseFile(content).getOrElse(Nil)
    }
    .toList
    .sortBy(_.timestamp)

  val a = domain.Analysis.allTimeTopArtists(listens)
  val b = domain.Analysis.allTimeTopTracks(listens)
  val c = domain.Analysis.allTimeTopAlbums(listens)
  val d = domain.Analysis.allTimeTopArtistByListenTime(listens)
  val e = domain.Analysis.favouriteHourOfDay(listens)
  val f = domain.Analysis.nightDwellerScore(listens)
  val g = domain.Analysis.mostSkippable(listens)
  val h = domain.Analysis.mostEnd2EndTrack(listens)
  val i = domain.Analysis.tooAfraidTooShare(listens)
  println(a)