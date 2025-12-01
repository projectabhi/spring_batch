package com.example.batch.scheduler;

import java.util.concurrent.CountDownLatch;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class KeepAliveRunner implements ApplicationRunner {
  @Override
  public void run(ApplicationArguments args) throws Exception {
    new CountDownLatch(1).await();
  }
}
