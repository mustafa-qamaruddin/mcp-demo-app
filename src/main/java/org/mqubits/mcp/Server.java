package org.mqubits.mcp;

import io.modelcontextprotocol.json.jackson3.JacksonMcpJsonMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import org.mqubits.tools.Repeater;
import tools.jackson.databind.json.JsonMapper;

@ApplicationScoped
public class Server {

  McpSyncServer _server;

  private static final Logger logger = Logger.getLogger(Server.class);


  public Server() {
    JacksonMcpJsonMapper jsonMapper = new JacksonMcpJsonMapper(JsonMapper.builder().build());
    McpServerTransportProvider transportProvider = new StdioServerTransportProvider(jsonMapper);

    McpSchema.ServerCapabilities serverCapabilities = McpSchema.ServerCapabilities.builder()
      .tools(true)
      .logging()
      .build();

    this._server = McpServer.sync(transportProvider)
      .serverInfo("Mcp Server", "0.0.1")
      .capabilities(serverCapabilities)
      .tools(Repeater.repeat())
      .build();
  }

  public void explainServer() {
    logger.info("*** Server Information ***");
    logger.info(this._server.getServerInfo().name());
    logger.info(this._server.getServerInfo().title());
    logger.info(this._server.getServerInfo().version());
    logger.info("***** Server Tools:");
    for (McpSchema.Tool tool : this._server.listTools()) {
      logger.info("+ " + tool.title());
      logger.info("\t " + tool.description());
    }
  }

  public McpSchema.Tool getRepeater() {
    return this._server.listTools().get(0);
  }
}
