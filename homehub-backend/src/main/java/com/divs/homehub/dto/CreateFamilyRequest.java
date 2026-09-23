package com.divs.homehub.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFamilyRequest {
    @NotBlank
    private String familyName;
}
