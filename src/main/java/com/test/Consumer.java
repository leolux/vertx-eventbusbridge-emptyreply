package com.test;

import io.vertx.core.AbstractVerticle;

public class Consumer extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    vertx.eventBus().consumer("inbound.address", msg -> {
      System.out.println("Consumer received a message.");

      // Reply with empty string
        msg.reply("");
        // msg.reply("{}");
      });
  }


}
