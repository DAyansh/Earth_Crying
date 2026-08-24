package com.earthcrying.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FootprintQuizRequest {

    @NotNull
    @Min(1)
    @Max(5)
    private Integer electricityUsage;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer transportationMode;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer meatConsumption;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer waterUsage;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer shoppingHabits;
}
