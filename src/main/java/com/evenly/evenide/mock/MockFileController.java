package com.evenly.evenide.mock;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mock")
public class MockFileController {

    @PostMapping("/file")
    public ResponseEntity<?> createFile(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(Map.of(
                "fileId", "abc123",
                "filename", req.get("filename")
        ));
    }

    @GetMapping("/files")
    public ResponseEntity<?> getFiles() {
        List<Map<String, Object>> files = List.of(
                Map.of(
                        "fileId", "f001",
                        "name", "main.py",
                        "updatedAt", "2025-04-07T11:00:00Z",
                        "isLocked", false,
                        "isEditLocked", false
                ),
                Map.of(
                        "fileId", "f002",
                        "name", "utils.py",
                        "updatedAt", "2025-04-07T11:10:00Z",
                        "isLocked", true,
                        "isEditLocked", true
                )
        );

        return ResponseEntity.ok(files);
    }

    @GetMapping("/file/{fileId}")
    public ResponseEntity<?> accessFile(@PathVariable String fileId) {
        return ResponseEntity.ok(Map.of(
                "fileId", fileId,
                "name", "main.py",
                "content", "print('Hello')",
                "isLocked", false,
                "isEditLocked", false,
                "updatedAt", "2025-04-07T11:00:00Z"
        ));
    }

    @PatchMapping("/file/{fileId}")
    public ResponseEntity<?> updateFile(@PathVariable String fileId, @RequestBody Map<String, String> req) {
        return ResponseEntity.ok(Map.of(
                "fileId", fileId,
                "filename", req.get("filename")
        ));
    }

    @DeleteMapping("/file/{fileId}")
    public ResponseEntity<?> deleteFile(@PathVariable String fileId) {
        return ResponseEntity.ok(Map.of("message", "success"));
    }

    @PostMapping("/file/lock")
    public ResponseEntity<?> lockFile(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(Map.of("status", "success"));
    }

    @PostMapping("/file/unlock")
    public ResponseEntity<?> unlockFile(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(Map.of("status", "success"));
    }

    @PostMapping("/file/edit/lock")
    public ResponseEntity<?> lockEdit(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(Map.of("status", "success"));
    }

    @PostMapping("/file/edit/unlock")
    public ResponseEntity<?> unlockEdit(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(Map.of("status", "success"));
    }
}
