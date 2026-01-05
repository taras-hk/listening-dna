import java.nio.file.Path


def streamFile(path: Path): List[SpotifyEntry] = {
  val reader = ujson.Readable.fromPath(path)
  val entries = upickle.default.read[List[SpotifyEntry]](reader)
  entries
}


