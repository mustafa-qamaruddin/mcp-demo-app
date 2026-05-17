package org.mqubits;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.spec.McpSchema;
import jakarta.inject.Inject;
import org.mqubits.mcp.Client;
import org.mqubits.mcp.Server;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class App {

  @Inject
  static Logger logger;

  public static void main(String[] args) {
    System.out.println("Hello, MCP!");
    McpSyncServer server = Server.createServer();

    System.out.println("*** Server Information ***");
    System.out.println(server.getServerInfo());
    for (McpSchema.Tool tool : server.listTools()) {
      System.out.println("+ " + tool.title());
      System.out.println("\t" + tool.description());
    }

    McpSyncClient client = Client.createClient();
    client.initialize();
    System.out.println("*** Client Information ***");
    System.out.println(client.getClientInfo());
    for (McpSchema.Tool tool : client.listTools().tools()) {
      System.out.println("+ " + tool.title());
      System.out.println("\t" + tool.description());
    }

    McpSchema.Tool tool = server.listTools().get(0);
    Map<String, Object> payload = Map.of("prompt", "lorem ipsum amet dolor");
    McpSchema.CallToolRequest req = new McpSchema.CallToolRequest(tool.name(), payload);
    McpSchema.CallToolResult response = client.callTool(req);
    if (response.isError()) {
      List<String> errors = response.content().stream()
        .filter(l -> l.type().equals(McpSchema.TextContent.class.getTypeName()))
        .map(l -> ((McpSchema.TextContent) l).text())
        .collect(Collectors.toList());
      for (String err :
        errors) {
        logger.error(err);
      }
    }
  }
}
