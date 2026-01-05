import com.fasterxml.jackson.core.{JsonFactory, JsonToken}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.nio.file.Path

val factory = new JsonFactory()
val mapper = new ObjectMapper(factory)


def streamFile(path: Path): Iterator[SpotifyEntry] = {
  mapper.registerModule(DefaultScalaModule)

  val parser = factory.createParser(path.toFile)

  require(parser.nextToken() == JsonToken.START_ARRAY)

  new Iterator[SpotifyEntry] {
    private var nextToken = parser.nextToken()

    override def hasNext: Boolean =
      nextToken == JsonToken.START_OBJECT

    override def next(): SpotifyEntry = {
      val value = mapper.readValue(parser, classOf[SpotifyEntry])
      nextToken = parser.nextToken()
      value
    }
  }
}


