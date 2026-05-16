package org.mqubits;

public class App {
  public static void main() {
    Server.createServer();
    Client.createClient().initialize();
  }
}
