package org.mqubits;

public class App {
  public static void main(String[] args) {
    new Thread(() -> {
      Server.createServer();
    }).start();
    Client.createClient().initialize();
  }
}
