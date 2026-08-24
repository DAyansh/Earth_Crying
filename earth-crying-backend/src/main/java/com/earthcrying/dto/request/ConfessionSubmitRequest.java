package com.earthcrying.dto.request;

import com.earthcrying.entity.ImpactCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfessionSubmitRequest {

    @NotBlank(message = "Confession content is required")
    @Size(min = 10, max = 500, message = "Confession must be between 10 and 500 characters")
    private String content;

    private ImpactCategory impactCategory;
}