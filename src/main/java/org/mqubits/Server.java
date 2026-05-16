package org.mqubits;

import io.modelcontextprotocol.json.jackson3.JacksonMcpJsonMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

public class Server {

  public McpSyncServer createServer() {
    JacksonMcpJsonMapper jsonMapper = new JacksonMcpJsonMapper((JsonMapper) new ObjectMapper());
    McpServerTransportProvider transportProvider = new StdioServerTransportProvider(jsonMapper);

    McpSchema.ServerCapabilities serverCapabilities = McpSchema.ServerCapabilities.builder()
      .tools(true)
      .logging()
      .build();

    McpSyncServer server = McpServer.sync(transportProvider)
      .serverInfo("Mcp Server", "0.0.1")
      .capabilities(serverCapabilities)
      .tools(Repeater.repeat())
      .build();

    return server;
  }
}
