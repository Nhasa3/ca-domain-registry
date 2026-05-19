package com.CIRA_N.Domain_Registery.Repository;

import com.CIRA_N.Domain_Registery.model.Domain;
import com.CIRA_N.Domain_Registery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DomainRepository extends JpaRepository<Domain, Long> {

    // Check if domain name already exists
    boolean existsByDomainName(String domainName);

    // Find a domain by its name
    Optional<Domain> findByDomainName(String domainName);

    // Get all domains belonging to a specific user
    List<Domain> findByOwner(User owner);

    // Get all domains expiring before a certain date
    List<Domain> findByExpiresAtBefore(LocalDate date);

    // Get all domains expiring between two dates
    List<Domain> findByExpiresAtBetween(LocalDate start, LocalDate end);

    // Count how many domains a user has
    long countByOwner(User owner);
}