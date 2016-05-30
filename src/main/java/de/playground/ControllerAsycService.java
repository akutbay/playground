package de.playground;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerAsycService {

  private final Jdk8Repository jdk8Repository;
  private final LongRunningService service;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  public ControllerAsycService(Jdk8Repository jdk8Repository, LongRunningService service) {
    this.jdk8Repository = jdk8Repository;
    this.service = service;
  }

  @RequestMapping(path = "/longRunning", method = RequestMethod.GET)
  public ResponseEntity<String> longRunning() throws ExecutionException, InterruptedException {
    logger.info("start");
    Stream<CompletableFuture<String>> completableFutureStream =
        IntStream.rangeClosed(1, 1000).boxed().map(i -> service.doStuff(i.toString()));
    logger.info("stop");
    CompletableFuture.allOf(completableFutureStream.toArray(CompletableFuture[]::new)).join();
    return ResponseEntity.ok("done");

  }

}
