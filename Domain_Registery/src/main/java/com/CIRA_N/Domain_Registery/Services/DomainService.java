package com.CIRA_N.Domain_Registery.Services;

import com.CIRA_N.Domain_Registery.Repository.DomainRepository;
import com.CIRA_N.Domain_Registery.model.Domain;
import com.CIRA_N.Domain_Registery.model.DomainStatus;
import com.CIRA_N.Domain_Registery.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DomainService {

    @Autowired
    private DomainRepository domainRepository;

    // ── CHECK AVAILABILITY ────────────────────────────────────────────
    public boolean isAvailable(String domainName) {
        return !domainRepository.existsByDomainName(domainName.toLowerCase());
    }

    // ── REGISTER A DOMAIN ─────────────────────────────────────────────
    public Domain registerDomain(String domainName, User owner) {

        domainName = domainName.toLowerCase().trim();

        if (!isAvailable(domainName)) {
            throw new RuntimeException("Domain '" + domainName + "' is already registered.");
        }

        if (!domainName.endsWith(".ca")) {
            throw new RuntimeException("Only .ca domains are supported.");
        }

        Domain domain = new Domain();
        domain.setDomainName(domainName);
        domain.setOwner(owner);
        domain.setStatus(DomainStatus.ACTIVE);
        domain.setRegisteredAt(LocalDate.now());
        domain.setExpiresAt(LocalDate.now().plusYears(1));

        return domainRepository.save(domain);
    }

    // ── RENEW A DOMAIN ────────────────────────────────────────────────
    public Domain renewDomain(Long domainId, User currentUser) {

        Domain domain = domainRepository.findById(domainId)
                .orElseThrow(() -> new RuntimeException("Domain not found."));

        if (!domain.getOwner().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You do not own this domain.");
        }

        LocalDate baseDate = domain.getExpiresAt().isAfter(LocalDate.now())
                ? domain.getExpiresAt()
                : LocalDate.now();

        domain.setExpiresAt(baseDate.plusYears(1));
        domain.setStatus(DomainStatus.ACTIVE);

        return domainRepository.save(domain);
    }

    // ── GET USER'S DOMAINS ────────────────────────────────────────────
    public List<Domain> getDomainsForUser(User user) {
        return domainRepository.findByOwner(user);
    }

    // ── GET SINGLE DOMAIN BY ID ───────────────────────────────────────
    public Domain getDomainById(Long id) {
        return domainRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Domain not found."));
    }

    // ── GET SINGLE DOMAIN BY NAME ─────────────────────────────────────
    public Domain getDomainByName(String domainName) {
        return domainRepository.findByDomainName(domainName)
                .orElseThrow(() -> new RuntimeException("Domain not found."));
    }

    // ── GET ALL DOMAINS (Admin only) ──────────────────────────────────
    public List<Domain> getAllDomains() {
        return domainRepository.findAll();
    }

    // ── GET EXPIRING DOMAINS (Admin only) ─────────────────────────────
    public List<Domain> getExpiringDomains() {
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysFromNow = today.plusDays(30);
        return domainRepository.findByExpiresAtBetween(today, thirtyDaysFromNow);
    }

    // ── DELETE A DOMAIN (Admin only) ──────────────────────────────────
    public void deleteDomain(Long domainId) {
        domainRepository.deleteById(domainId);
    }

    // ── UPDATE EXPIRED DOMAINS ────────────────────────────────────────
    public void updateExpiredDomains() {
        List<Domain> allDomains = domainRepository.findAll();
        for (Domain domain : allDomains) {
            if (domain.getExpiresAt().isBefore(LocalDate.now())
                    && domain.getStatus() == DomainStatus.ACTIVE) {
                domain.setStatus(DomainStatus.EXPIRED);
                domainRepository.save(domain);
            }
        }
    }
}