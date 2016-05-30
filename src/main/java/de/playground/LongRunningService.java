package de.playground;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.google.common.base.Throwables.propagate;

@Component
class LongRunningService {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Async
  public CompletableFuture<String> doStuff(String name) {
    logger.info("start dostuff: " + name);
    try {
      Thread.sleep(10000);
      logger.info("stop dostuff: " + name);
      return CompletableFuture.completedFuture("done");
    }
    catch (InterruptedException e) {
      throw propagate(e);
    }
  }
}
