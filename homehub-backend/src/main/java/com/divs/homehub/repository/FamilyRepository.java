package com.divs.homehub.repository;

import com.divs.homehub.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyRepository extends JpaRepository<Family, Long> {
    boolean existsByInviteCode(String inviteCode);
}