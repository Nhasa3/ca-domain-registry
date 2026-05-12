package com.CIRA_N.Domain_Registery.Repository;

import com.CIRA_N.Domain_Registery.model.Domain;
import org.springframework.stereotype.Repository;
import com.CIRA_N.Domain_Registery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DomainRepository extends JpaRepository<Domain, Long>{
    // checks if domain already exists
    boolean existByDomainName (String domainName);

    Optional<Domain> findByDomainName(String domainName);

    // Get all domains belonging to a specific user
    List<Domain> findByOwner(User owner);

    // Get all domains expiring before a certain date (for admin)
    List<Domain> findByExpiresAtBefore(LocalDate date);

    // Get all domains expiring between two dates (expiring soon)
    List<Domain> findByExpiresAtBetween(LocalDate start, LocalDate end);

    // Count how many domains a user has
    long countByOwner(User owner);
}
