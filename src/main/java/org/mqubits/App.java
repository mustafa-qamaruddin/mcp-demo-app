package org.mqubits;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.server.McpSyncServer;
import org.mqubits.mcp.Client;
import org.mqubits.mcp.Server;

public class App {
  public static void main(String[] args) {
    System.out.println("Hello, MCP!");
    McpSyncServer server = Server.createServer();
    System.out.println(server.getServerInfo());
    McpSyncClient client = Client.createClient();
    client.initialize();
    System.out.println(client.getClientInfo());
  }
}
