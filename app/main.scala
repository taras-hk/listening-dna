import java.nio.file.{Files, Paths}
import scala.jdk.CollectionConverters.*

@main
def main(dir: String): Unit =

  assert(dir != null)

  val source = Files.list(Paths.get(dir))
    .iterator()
    .asScala
    .flatMap(streamFile)
    .toList

  val totalPlayed = source.foldLeft(0) { (acc, entry) =>
    acc + entry.msPlayed
  }

  println(totalPlayed / (1000 * 60))
