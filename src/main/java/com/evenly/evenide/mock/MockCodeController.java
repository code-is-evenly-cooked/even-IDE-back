package com.evenly.evenide.mock;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mock/code")
public class MockCodeController {

    @PostMapping("/execute")
    public ResponseEntity<?> runCode(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(Map.of(
                "output", "Hello",
                "status", "success"
        ));
    }
}
