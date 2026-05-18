package org.mqubits;

import io.modelcontextprotocol.spec.McpSchema;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import org.mqubits.mcp.Client;
import org.mqubits.mcp.Server;
import org.jboss.logging.Logger;

@QuarkusMain
public class App {

  public static void main(String[] args) {
    Quarkus.run(QApp.class,
      (exitCode, exception) -> {
        Logger.getLogger(App.class).error("Error:" + exitCode + ", " + exception.getMessage());
      },
      args);
  }

  public static class QApp implements QuarkusApplication {
    @Inject
    Server server;

    @Inject
    Client client;

    @Override
    public int run(String... args) throws Exception {
      System.out.println("Hello, MCP!");

      server.explainServer();

      client.explainClient();

      McpSchema.Tool repeater = server.getRepeater();
      client.callRepeater(repeater);

      Quarkus.waitForExit();
      return 0;
    }
  }
}
