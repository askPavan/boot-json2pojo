package com.json2pojo.controller;

import com.json2pojo.util.JsonParserUtil;
import com.json2pojo.util.PojoGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/json2pojo")
public class PojoController {

    @PostMapping("/generate")
    public ResponseEntity<?> generatePojo(@RequestBody String jsonInput) {
        try {
            Map<String, Object> fields = JsonParserUtil.parseJson(jsonInput);
            Map<String, String> pojoClasses = PojoGenerator.generatePojo("GeneratedClass", fields);

            StringBuilder response = new StringBuilder();
            pojoClasses.values().forEach(response::append);

            return ResponseEntity.ok(response.toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid JSON input");
        }
    }
}
