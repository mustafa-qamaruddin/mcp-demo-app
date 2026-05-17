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

  public static void main(String[] args) {
    System.out.println("Hello, MCP!");

    Server server = new Server();
    server.explainServer();

    Client client = new Client();
    client.explainClient();

    McpSchema.Tool repeater = server.getRepeater();
    client.callRepeater(repeater);
  }
}
