
package com.listeningdna;


import io.helidon.config.Config;
import io.helidon.logging.common.LogConfig;
import io.helidon.service.registry.Services;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.HttpRouting;
import jakarta.json.Json;
import jakarta.json.JsonValue;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Gatherer;
import java.util.stream.Gatherers;
import java.util.stream.Stream;


/**
 * The application main class.
 */
public class Main {


    /**
     * Cannot be instantiated.
     */
    private Main() {
    }

    record Session(SpotifyEntry start, List<SpotifyEntry> entries) {
    }

    /**
     * Application main entry point.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) throws Exception {

        var path = Paths.get("/Users/taras/Downloads/Spotify Extended Streaming History/data/Streaming_History_Audio_2020-2021_0.json");

        var fileStream = Files.newInputStream(path);

        var parser = Json.createParser(fileStream);

        final List<SpotifyEntry> spotifyEntries = parser.getValueStream()
                .map(JsonValue::asJsonArray)
                .map(Collection::stream)
                .flatMap(Stream::distinct)
                .map(JsonValue::asJsonObject)
                .map(SpotifyEntry::from)
                .toList();

        var sessionInterval = Duration.ofMinutes(30);

        final List<List<SpotifyEntry>> list = spotifyEntries.stream().gather(Gatherer.<SpotifyEntry, ArrayList<SpotifyEntry>, List<SpotifyEntry>>ofSequential(
                ArrayList::new,

                Gatherer.Integrator.ofGreedy((buffer, track, downstream) -> {
                    if (buffer.isEmpty()) {
                        buffer.add(track);

                        return true;
                    }

                    SpotifyEntry last = buffer.getLast();
                    Instant lastTs = last.timestampAsInstant();
                    Instant currentTs = track.timestampAsInstant();

                    Duration diff = Duration.between(lastTs, currentTs);

                    if (diff.compareTo(sessionInterval) >= 0) {
                        downstream.push(new ArrayList<>(buffer));

                        buffer.clear();
                        buffer.add(track);
                    } else {
                        buffer.add(track);
                    }

                    return true;
                }),

                (buffer, downstream) -> {
                    if (!buffer.isEmpty()) {
                        downstream.push(new ArrayList<>(buffer));
                    }
                }
        )).toList();

        System.out.println(list);

        /*

        1) take first element and compare it with the next one
        2) if the diff is bigger than 30 minutes - the session is over.
        3) if not, iterate to the third one and compare the diff between it and the previous one
            a) if bigger, then the session consist of the first one and the elements between it and the current
            b) if not, go to the next.

         */

        // load logging configuration
        LogConfig.configureRuntime();

        // initialize global config from default configuration
        Config config = Config.create();
        Services.set(Config.class, config);


        WebServer server = WebServer.builder()
                .config(config.get("server"))
                .routing(Main::routing)
                .build()
                .start();


        System.out.println("WEB server is up! http://localhost:" + server.port() + "/simple-greet");

    }


    /**
     * Updates HTTP Routing.
     */
    static void routing(HttpRouting.Builder routing) {
        routing
                .register("/greet", new GreetService())
                .get("/simple-greet", (req, res) -> res.send("Hello World!"));
    }
}