package com.abach42.jott.any;

import com.abach42.jott.config.methodsecurity.IsAdmin;
import com.abach42.jott.config.methodsecurity.IsUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Any Domain")
@RestController
@RequestMapping(path = "/api/any")
public class AnyController {

    @GetMapping("/bar")
    @IsUser
    public ResponseEntity<String> bar() {
        return ResponseEntity.ok().body("{\"bar\": 42}");
    }

    @GetMapping("/baz")
    @IsUser
    public ResponseEntity<String> baz() {
        return ResponseEntity.ok().body("{\"baz\": 43}");
    }

    @GetMapping("/boo")
    @IsAdmin
    public ResponseEntity<String> onlyAdmin() {
        return ResponseEntity.ok().body("{\"bar\": 44}");
    }
}
