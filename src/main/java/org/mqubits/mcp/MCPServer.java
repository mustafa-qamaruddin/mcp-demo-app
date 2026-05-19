package org.mqubits.mcp;

import io.modelcontextprotocol.json.jackson3.JacksonMcpJsonMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.HttpServletSseServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.jetty.server.Server;
import org.jboss.logging.Logger;
import org.mqubits.tools.Repeater;
import tools.jackson.databind.json.JsonMapper;

@Startup
@ApplicationScoped
public class MCPServer {

  McpSyncServer _server;
  Server _jettyMCPServer;
  HttpServletSseServerTransportProvider _transportProvider;

  private static final Logger logger = Logger.getLogger(MCPServer.class);
  public static final String SSE_EP = "/mcp/sse";
  public static final String MSG_EP = "/mcp/sse/msg";

  @PostConstruct
  public void init() throws Exception {
    JacksonMcpJsonMapper jsonMapper = new JacksonMcpJsonMapper(JsonMapper.builder().build());
    this._transportProvider = HttpServletSseServerTransportProvider.builder()
      .jsonMapper(jsonMapper)
      .sseEndpoint(SSE_EP)
      .messageEndpoint(MSG_EP)
      .build();

    McpSchema.ServerCapabilities serverCapabilities = McpSchema.ServerCapabilities.builder()
      .tools(true)
      .logging()
      .build();

    this._server = McpServer.sync(_transportProvider)
      .serverInfo("Mcp Server", "0.0.1")
      .capabilities(serverCapabilities)
      .tools(Repeater.repeat())
      .build();
  }

  public HttpServletSseServerTransportProvider transportProvider() {
    return this._transportProvider;
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
