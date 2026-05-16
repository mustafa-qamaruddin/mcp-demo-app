package org.mqubits;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class Repeater {
  public static McpServerFeatures.SyncToolSpecification repeat() {

    Map<String, Object> properties = Map.of(
      "prompt", String.class
    );

    List<String> required = List.of("prompt");

    McpSchema.JsonSchema mcpSchema = new McpSchema.JsonSchema(
      "object",
      properties,
      required,
      false,
      null,
      null
    );

    McpSchema.Tool tool = new McpSchema.Tool("repeater", "Repeater", "Just another repeater",
      mcpSchema, null, null, null);

    return new McpServerFeatures.SyncToolSpecification(
      tool,
      (exchange, req) -> {
        String prompt = (String) req.arguments().get("prompt");
        McpSchema.TextContent textContent = new McpSchema.TextContent(
          new StringBuilder().append("Prompt: ").append(prompt).toString()
        );
        McpSchema.CallToolResult result = McpSchema.CallToolResult.builder()
          .content(List.of(textContent))
          .isError(false)
          .build();

        return result;
    }
    );
  }
}
