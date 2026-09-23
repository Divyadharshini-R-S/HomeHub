package com.divs.homehub.repository;

import com.divs.homehub.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface FamilyRepository extends JpaRepository<Family, Long> {
    boolean existsByInviteCode(String inviteCode);
    Optional<Family> findByInviteCode(String inviteCode);
}