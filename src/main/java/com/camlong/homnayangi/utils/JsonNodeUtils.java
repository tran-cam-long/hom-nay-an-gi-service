package com.camlong.homnayangi.utils;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.function.Consumer;

@UtilityClass
public class JsonNodeUtils {

  public static void applyTextField(JsonNode node, String field, Consumer<String> setter) {
    if (node == null || !node.has(field)) return;
    final JsonNode valueNode = node.get(field);
    if (valueNode == null || valueNode.isNull()) return;
    final String value = valueNode.asText("");
    if (!value.isBlank()) {
      setter.accept(value.trim());
    }
  }

  public static void applyTextField(Map<String, Object> payload, String field, Consumer<String> setter) {
    if (payload == null || !payload.containsKey(field)) return;
    final Object valueObject = payload.get(field);
    if (valueObject == null) return;
    final String value = String.valueOf(valueObject);
    if (!value.isBlank()) {
      setter.accept(value.trim());
    }
  }
}
