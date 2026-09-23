package com.divs.homehub.service;
import com.divs.homehub.dto.CreateFamilyRequest;
import com.divs.homehub.dto.FamilyResponse;

public interface FamilyService {

    FamilyResponse createFamily(CreateFamilyRequest request, Long userId);
    FamilyResponse joinFamily(String inviteCode, Long userId);

}
