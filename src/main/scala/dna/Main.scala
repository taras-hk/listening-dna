package dna

import dna.domain.ListenEntry
import dna.infrastructure.SpotifyJsonParse

import java.nio.file.{Files, Paths}
import java.time.Duration
import scala.io.Source
import scala.util.Using
import scala.jdk.CollectionConverters.*


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
  println(a)