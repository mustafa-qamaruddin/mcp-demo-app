package org.mqubits.mcp;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.json.jackson3.JacksonMcpJsonMapper;
import io.modelcontextprotocol.spec.McpClientTransport;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

public class Client {
  public static McpSyncClient createClient() {
    JacksonMcpJsonMapper jsonMapper = new JacksonMcpJsonMapper(JsonMapper.builder().build());
    ServerParameters params = ServerParameters.builder("npx")
      .args("-y", "@modelcontextprotocol/server-everything")
      .build();

    McpClientTransport transport = new StdioClientTransport(params, jsonMapper);
    McpSyncClient client = McpClient.sync(transport).build();

    return client;
  }
}
