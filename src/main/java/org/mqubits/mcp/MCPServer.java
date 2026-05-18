package org.mqubits.mcp;

import io.modelcontextprotocol.json.jackson3.JacksonMcpJsonMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.HttpServletSseServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.servlet.Servlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.jboss.logging.Logger;
import org.mqubits.tools.Repeater;
import tools.jackson.databind.json.JsonMapper;

@ApplicationScoped
public class MCPServer {

  McpSyncServer _server;

  private static final Logger logger = Logger.getLogger(MCPServer.class);
  private static final String SSE_EP = "/mcp/sse";
  private static final String MSG_EP = "/mcp/sse/msg";

  public MCPServer() throws Exception {
    JacksonMcpJsonMapper jsonMapper = new JacksonMcpJsonMapper(JsonMapper.builder().build());
    McpServerTransportProvider transportProvider = HttpServletSseServerTransportProvider.builder()
      .jsonMapper(jsonMapper)
      .sseEndpoint(SSE_EP)
      .messageEndpoint(MSG_EP)
      .build();

    McpSchema.ServerCapabilities serverCapabilities = McpSchema.ServerCapabilities.builder()
      .tools(true)
      .logging()
      .build();

    this._server = McpServer.sync(transportProvider)
      .serverInfo("Mcp Server", "0.0.1")
      .capabilities(serverCapabilities)
      .tools(Repeater.repeat())
      .build();

    Server jettyMCPServer = new Server(9090);
    ServletContextHandler context = new ServletContextHandler(
      ServletContextHandler.SESSIONS
    );
    context.setContextPath("/");
    jettyMCPServer.setHandler(context);

    ServletHolder servletHolder = new ServletHolder((Servlet) transportProvider);
    context.addServlet(servletHolder, SSE_EP);
    context.addServlet(servletHolder, MSG_EP);

    jettyMCPServer.start();
    System.out.println("✅ MCP Server started on http://localhost:9090"+SSE_EP);
    jettyMCPServer.join();
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
