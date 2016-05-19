package de.playground;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import de.playground.model.WithId;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0", "spring.data.mongodb.host=192.168.99.100"})
@SpringApplicationConfiguration(classes = {Application.class})
public class WithIdRepositoryTest {

  @Autowired
  private WithIdRepository withIdRepository;

  @Before
  public void setup() {
    withIdRepository.deleteAll();
  }

  @Test
  public void getOptional() throws IOException {
    withIdRepository.insert(new WithId("1", null));
    System.out.println("done");
    WithId one = withIdRepository.findOne("1");
    System.out.println(one);
  }

}