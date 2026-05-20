package org.mqubits.servlets;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mqubits.mcp.MCPServer;
import jakarta.enterprise.inject.spi.CDI;

import java.io.IOException;

import static org.mqubits.mcp.MCPServer.MSG_EP;
import static org.mqubits.mcp.MCPServer.SSE_EP;

@WebServlet(
  loadOnStartup = 1,
  urlPatterns = {SSE_EP, MSG_EP},
  asyncSupported = true
)
public class MCPServlet extends HttpServlet {
  private MCPServer mcpServer;

  @Override
  public void init(ServletConfig config)
    throws ServletException {
    super.init(config);
    this.mcpServer = CDI.current().select(MCPServer.class).get();
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
    mcpServer.transportProvider().service(req, resp);
  }
}