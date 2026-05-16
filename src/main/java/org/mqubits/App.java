package org.mqubits;

import org.mqubits.mcp.Client;
import org.mqubits.mcp.Server;

public class App {
  public static void main(String[] args) {
    System.out.println("Hello, MCP!");
    new Thread(() -> {
      Server.createServer();
    }).start();
    Client.createClient().initialize();
  }
}
