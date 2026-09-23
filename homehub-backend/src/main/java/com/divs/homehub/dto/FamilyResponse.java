package com.divs.homehub.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FamilyResponse {
    private Long id;
    private String familyName;
    private String inviteCode;
}
