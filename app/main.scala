import java.nio.file.{Files, Paths}
import scala.jdk.CollectionConverters._

@main
def main(dir: String): Unit =

  assert(dir != null)

  Files.list(Paths.get(dir))
    .iterator()
    .asScala
    .flatMap(streamFile)
    .foreach(println)
