package org.mqubits;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import org.mqubits.mcp.MCPServer;

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
    MCPServer mcpServer;

    @Override
    public int run(String... args) throws Exception {
      mcpServer.explainServer();
      Quarkus.waitForExit();
      return 0;
    }
  }
}
