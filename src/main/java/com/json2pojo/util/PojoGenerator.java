package com.json2pojo.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PojoGenerator {
    public static Map<String, String> generatePojo(String className, Map<String, Object> fields) {
        Map<String, String> classes = new LinkedHashMap<>();
        StringBuilder mainClass = new StringBuilder();
        mainClass.append("@Data\n");
        mainClass.append("@NoArgsConstructor\n");
        mainClass.append("@AllArgsConstructor\n");
        mainClass.append("public class ").append(className).append(" {\n\n");

        fields.forEach((name, type) -> {
            if (type instanceof Map) {
                String nestedClassName = capitalize(name);
                mainClass.append("    private ").append(nestedClassName).append(" ").append(name).append(";\n");
                classes.putAll(generatePojo(nestedClassName, (Map<String, Object>) type));
            } else if (type instanceof List) {
                String nestedClassName = capitalize(name) + "Item";
                mainClass.append("    private List<").append(nestedClassName).append("> ").append(name).append(";\n");
                classes.put(nestedClassName, generateSimpleClass(nestedClassName));
            } else {
                mainClass.append("    private ").append(type).append(" ").append(name).append(";\n");
            }
        });

        mainClass.append("}\n\n");
        classes.put(className, mainClass.toString());
        return classes;
    }

    private static String generateSimpleClass(String className) {
        return "@Data\n@NoArgsConstructor\n@AllArgsConstructor\npublic class " + className + " {\n    private String value;\n}\n";
    }

    private static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}