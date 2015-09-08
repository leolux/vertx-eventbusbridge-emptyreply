package com.test;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

/**
 *
 */
public class MainVertx {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    // Create consumer
    vertx.deployVerticle(new Consumer());

    // Create Webserver
    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);
    router.route().method(HttpMethod.GET).path("/test.html").handler(routingContext -> {
      routingContext.response().sendFile("webroot/test.html");
    });

    // Add SockJS Handler
    SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
    BridgeOptions bridgeOptions = new BridgeOptions();
    PermittedOptions inboundPermitted = new PermittedOptions();
    inboundPermitted.setAddress("inbound.address");
    bridgeOptions.addInboundPermitted(inboundPermitted);
    sockJSHandler.bridge(bridgeOptions);
    router.route("/eventbus/*").handler(sockJSHandler);

    // Start server
    server.requestHandler(router::accept).listen(8888);
    System.out.println("Now open http://localhost:8888/test.html in your browser");
  }
}
