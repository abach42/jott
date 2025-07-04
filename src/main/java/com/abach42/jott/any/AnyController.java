package com.abach42.jott.any;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Any Domain")
@RestController
@RequestMapping(path = "/api/any")
public class AnyController {

    @GetMapping("/bar")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> actionForAdminAndUser() {
        return ResponseEntity.ok().body("bar");
    }
}
