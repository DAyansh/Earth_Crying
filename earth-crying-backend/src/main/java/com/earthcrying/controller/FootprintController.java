package com.earthcrying.controller;

import com.earthcrying.dto.FootprintResultDTO;
import com.earthcrying.dto.request.FootprintQuizRequest;
import com.earthcrying.service.FootprintCalculatorService;
import com.earthcrying.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Footprint", description = "Environmental footprint quiz calculation")
@RestController
@RequestMapping("/api/footprint")
@RequiredArgsConstructor
public class FootprintController {

    private final FootprintCalculatorService footprintCalculatorService;
    private final UserService userService;

    @PostMapping("/calculate")
    @Operation(summary = "Calculate environmental footprint from quiz answers")
    public ResponseEntity<FootprintResultDTO> calculate(@Valid @RequestBody FootprintQuizRequest quizRequest) {
        String userId = getCurrentUserId();
        return ResponseEntity.ok(footprintCalculatorService.calculateFootprint(quizRequest, userId));
    }

    @GetMapping("/previous")
    @Operation(summary = "Get the most recent footprint result for the current user")
    public ResponseEntity<FootprintResultDTO> getPrevious() {
        String userId = getCurrentUserId();
        return ResponseEntity.ok(footprintCalculatorService.getPreviousResults(userId));
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
                // JWT subject is the user's email; resolve to the UUID used by the service.
                try {
                    return userService.getUserEntityByEmail(userDetails.getUsername()).getId().toString();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }
}
