package org.mqubits.mcp;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.json.jackson3.JacksonMcpJsonMapper;
import io.modelcontextprotocol.spec.McpClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class Client {
  McpSyncClient _client;

  private static final Logger logger = Logger.getLogger(Client.class);

  public Client() {
    JacksonMcpJsonMapper jsonMapper = new JacksonMcpJsonMapper(JsonMapper.builder().build());
    ServerParameters params = ServerParameters.builder("npx")
      .args("-y", "@modelcontextprotocol/server-everything")
      .build();

    McpClientTransport transport = new StdioClientTransport(params, jsonMapper);
    this._client = McpClient.sync(transport).build();
    this._client.initialize();
  }

  public void explainClient() {
    logger.info("*** Client Info:");
    logger.info(this._client.getClientInfo().name());
    logger.info(this._client.getClientInfo().title());
    logger.info(this._client.getClientInfo().version());
    logger.info("**** Tools");
    for (McpSchema.Tool tool : this._client.listTools().tools()) {
      logger.info("+ " + tool.title());
      logger.info("\t " + tool.description());
    }
  }

  public void callRepeater(McpSchema.Tool tool) {
    Map<String, Object> payload = Map.of("prompt", "lorem ipsum amet dolor");
    McpSchema.CallToolRequest req = new McpSchema.CallToolRequest(tool.name(), payload);
    McpSchema.CallToolResult response = this._client.callTool(req);

    if (response.isError()) {
      List<String> errors = response.content().stream()
        .filter(l -> l.type().equals(McpSchema.TextContent.class.getTypeName()))
        .map(l -> ((McpSchema.TextContent) l).text())
        .toList();
      for (String err :
        errors) {
        logger.error(err);
      }
      return;
    }

    response.content().stream()
      .filter(content -> content.type().equals(McpSchema.TextContent.class.getTypeName()))
      .iterator()
      .forEachRemaining(content -> logger.info(content.toString()));
  }
}
