package com.divs.homehub.repository;

import com.divs.homehub.entity.FamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {
    boolean existsByUserIdAndFamilyId(Long userId, Long familyId);
}