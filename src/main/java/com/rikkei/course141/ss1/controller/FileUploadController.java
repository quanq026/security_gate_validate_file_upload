package com.rikkei.course141.ss1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.Map;

import com.rikkei.course141.ss1.exception.InvalidFileException;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {
    private final Path uploadDir = Paths.get("uploads");

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("image") MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "File rỗng"));
        }
        String original = file.getOriginalFilename();
        if (original == null || !(original.toLowerCase().endsWith(".png") || original.toLowerCase().endsWith(".jpg"))) {
            throw new InvalidFileException("Chỉ chấp nhận ảnh");
        }
        Files.createDirectories(uploadDir);
        Path target = uploadDir.resolve(original);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return ResponseEntity.ok(Map.of("message", "Upload thành công", "fileName", original));
    }
}
