package com.json2pojo.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public class JsonParserUtil {

    public static Map<String, Object> parseJson(String jsonString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonString);
        return processNode(rootNode);
    }

    private static Map<String, Object> processNode(JsonNode node) {
        Map<String, Object> fields = new LinkedHashMap<>();

        node.fields().forEachRemaining(entry -> {
            String key = entry.getKey();
            JsonNode value = entry.getValue();

            if (value.isObject()) {
                fields.put(key, processNode(value));
            } else if (value.isArray()) {
                fields.put(key, getArrayType(value));
            } else {
                fields.put(key, getJavaType(value));
            }
        });

        return fields;
    }

    private static String getJavaType(JsonNode value) {
        if (value.isInt()) {
            return "Integer";
        } else if (value.isLong()) {
            return "Long";
        } else if (value.isDouble()) {
            return "Double";
        } else if (value.isBoolean()) {
            return "Boolean";
        } else if (value.isNull()) {
            return "Optional<Object>";
        } else {
            return "String";
        }
    }

    private static String getArrayType(JsonNode arrayNode) {
        if (arrayNode.isEmpty()) {
            return "List<Object>";
        }

        Set<String> elementTypes = new HashSet<>();

        arrayNode.forEach(element -> {
            if (element.isTextual()) {
                elementTypes.add("String");
            } else if (element.isInt()) {
                elementTypes.add("Integer");
            } else if (element.isLong()) {
                elementTypes.add("Long");
            } else if (element.isDouble()) {
                elementTypes.add("Double");
            } else if (element.isBoolean()) {
                elementTypes.add("Boolean");
            } else if (element.isObject()) {
                elementTypes.add("Object");
            } else if (element.isNull()) {
                elementTypes.add("null");
            }
        });

        if (elementTypes.size() > 1) {
            return "List<Object>";
        } else {
            return "List<" + elementTypes.iterator().next() + ">";
        }
    }
}
