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
    public ResponseEntity<String> bar() {
        return ResponseEntity.ok().body("{\"bar\": 42}");
    }

    @GetMapping("/baz")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<String> baz() {
        return ResponseEntity.ok().body("{\"baz\": 43}");
    }

    @GetMapping("/boo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> onlyAdmin() {
        return ResponseEntity.ok().body("{\"bar\": 44}");
    }
}
