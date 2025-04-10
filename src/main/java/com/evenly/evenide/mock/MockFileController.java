package com.evenly.evenide.mock;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mock")
public class MockFileController {

    /**
     * project
     */
    @GetMapping("/projects")
    public ResponseEntity<?> getAllProjects() {
        return ResponseEntity.ok(List.of(
                Map.of("projectId", "123", "projectname", "even"),
                Map.of("projectId", "124", "projectname", "next")
        ));
    }

    @PostMapping("/projects")
    public ResponseEntity<?> createProject(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(Map.of(
                "projectId", "125",
                "projectname", req.get("projectname")
        ));
    }

    @PatchMapping("/projects/{projectId}")
    public ResponseEntity<?> updateProject(@PathVariable String projectId, @RequestBody Map<String, String> req) {
        // 특정 프로젝트 수정 처리
        return ResponseEntity.ok(Map.of(
                "projectId", projectId,
                "projectname", req.get("projectname")
        ));
    }

    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId) {
        // 프로젝트 삭제 처리
        return ResponseEntity.ok(Map.of("message", "success"));
    }


    @GetMapping("/projects/{projectId}/files")
    public ResponseEntity<?> getFiles() {
        List<Map<String, Object>> files = List.of(
                Map.of(
                        "fileId", "f001",
                        "name", "main.py",
                        "language", "python",
                        "isLocked", false,
                        "isEditLocked", false,
                        "updatedAt", "2025-04-07T11:00:00Z",
                        "ownerId", "user123"
                ),
                Map.of(
                        "fileId", "f002",
                        "name", "utils.py",
                        "language", "python",
                        "isLocked", true,
                        "isEditLocked", true,
                        "updatedAt", "2025-04-07T11:10:00Z",
                        "ownerId", "user123"
                )
        );

        return ResponseEntity.ok(files);
    }

    @PostMapping("/projects/{projectId}/files")
    public ResponseEntity<?> createFile(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(Map.of(
                "fileId", "abc123",
                "filename", req.get("filename")
        ));
    }



    @GetMapping("/projects/{projectId}/files/{fileId}")
    public ResponseEntity<?> accessFile(@PathVariable String projectId, @PathVariable String fileId) {
        return ResponseEntity.ok(Map.of(
                "fileId", fileId,
                "filename", "main.py",
                "language", "python",
                "content", "print('Hello')",
                "isLocked", false,
                "isEditLocked", false,
                "updatedAt", "2025-04-07T11:00:00Z",
                "ownerId", "user123"
        ));
    }

    @PatchMapping("/projects/{projectId}/files/{fileId}")
    public ResponseEntity<?> updateFile(@PathVariable String projectId, @PathVariable String fileId, @RequestBody Map<String, String> req) {
        return ResponseEntity.ok(Map.of(
                "fileId", fileId,
                "filename", req.get("filename")
        ));
    }

    @DeleteMapping("/projects/{projectId}/files/{fileId}")
    public ResponseEntity<?> deleteFile(@PathVariable String projectId, @PathVariable String fileId) {
        return ResponseEntity.ok(Map.of("message", "success"));
    }

    @PostMapping("/projects/{projectId}/files/{fileId}/lock")
    public ResponseEntity<?> lockFile(@PathVariable String projectId, @PathVariable String fileId) {
        return ResponseEntity.ok(Map.of("message", "success"));
    }

    @PostMapping("/projects/{projectId}/files/{fileId}/unlock")
    public ResponseEntity<?> unlockFile(@PathVariable String projectId, @PathVariable String fileId) {
        return ResponseEntity.ok(Map.of("message", "success"));
    }

    @PostMapping("/projects/{projectId}/files/{fileId}/edit/lock")
    public ResponseEntity<?> lockEdit(@PathVariable String projectId, @PathVariable String fileId) {
        return ResponseEntity.ok(Map.of("message", "success"));
    }

    @PostMapping("/projects/{projectId}/files/{fileId}/edit/unlock")
    public ResponseEntity<?> unlockEdit(@PathVariable String projectId, @PathVariable String fileId) {
        return ResponseEntity.ok(Map.of("message", "success"));
    }
}
