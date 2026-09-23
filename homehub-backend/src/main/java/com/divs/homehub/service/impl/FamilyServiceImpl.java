package com.divs.homehub.service.impl;
import com.divs.homehub.entity.Family;
import com.divs.homehub.entity.FamilyMember;
import com.divs.homehub.exception.AlreadyFamilyMemberException;
import com.divs.homehub.repository.UserRepository;
import com.divs.homehub.repository.FamilyRepository;
import com.divs.homehub.repository.FamilyMemberRepository;
import com.divs.homehub.service.FamilyService;

import com.divs.homehub.dto.CreateFamilyRequest;
import com.divs.homehub.dto.FamilyResponse;
import com.divs.homehub.entity.User;

import java.util.UUID;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FamilyServiceImpl implements FamilyService {
    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;
    private final FamilyMemberRepository familyMemberRepository;

    public FamilyServiceImpl(UserRepository userRepository,FamilyRepository familyRespository,FamilyMemberRepository familyMemberRepository){
    this.userRepository=userRepository;
    this.familyRepository=familyRespository;
    this.familyMemberRepository=familyMemberRepository;
    }

    private String generateInviteCode(){
        return UUID.randomUUID().toString().replace("-","").substring(0,6).toUpperCase();
    }

    @Transactional
    @Override
    public FamilyResponse createFamily(CreateFamilyRequest request, Long userId){
        //Getting user details from DB
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //Creating structure to save family details to save it in DB
        Family family = new Family();
        String inviteCode;
        do {
            inviteCode = generateInviteCode();
        } while (familyRepository.existsByInviteCode(inviteCode));
        family.setName(request.getFamilyName());
        family.setInviteCode(inviteCode);

        Family savedFamily = familyRepository.save(family);

        //Saving the familymember to DB
        FamilyMember member = new FamilyMember();
        member.setUser(user);
        member.setFamily(savedFamily);
        member.setRole("OWNER");
        familyMemberRepository.save(member);

        FamilyResponse response = new FamilyResponse();
        response.setId(savedFamily.getId());
        response.setFamilyName(savedFamily.getName());
        response.setInviteCode(savedFamily.getInviteCode());

        return response;
    }

    @Override
    public FamilyResponse joinFamily(String inviteCode, Long userId) {
        // logic comes here
        Family family = familyRepository.findByInviteCode(inviteCode).orElseThrow(
                ()-> new EntityNotFoundException("Invite Code not found"));
        if(familyMemberRepository.existsByUserIdAndFamilyId(userId,family.getId())){
            throw new AlreadyFamilyMemberException("User is already part of this family ");
        }
        else{
            FamilyMember member = new FamilyMember();
            member.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
            member.setFamily(family);
            member.setRole("MEMBER");

            familyMemberRepository.save(member);
            FamilyResponse response = new FamilyResponse();
            response.setId(family.getId());
            response.setFamilyName(family.getName());
            response.setInviteCode(inviteCode);
            return response;
        }

    }
}
