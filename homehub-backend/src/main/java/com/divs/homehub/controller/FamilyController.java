package com.divs.homehub.controller;

import com.divs.homehub.dto.JoinFamilyRequest;
import com.divs.homehub.service.FamilyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.divs.homehub.dto.CreateFamilyRequest;
import com.divs.homehub.dto.FamilyResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/families")
public class FamilyController {

    private final FamilyService familyService;

    public FamilyController(FamilyService familyService) {
        this.familyService = familyService;
    }

    @PostMapping
    public FamilyResponse createFamily(
            @Valid @RequestBody CreateFamilyRequest request) {

        return familyService.createFamily(request, 1L);
    }

    @PostMapping("/join")
    public FamilyResponse joinFamily(@Valid @RequestBody JoinFamilyRequest joinFamilyRequest) {
        return familyService.joinFamily(joinFamilyRequest.getInviteCode(), 2L);
    }
}