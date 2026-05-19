package com.CIRA_N.Domain_Registery.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "domains")
public class Domain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "domain_name", nullable = false, unique = true)
    private String domainName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DomainStatus status;

    @Column(nullable = false)
    private LocalDate registeredAt;

    @Column(nullable = false)
    private LocalDate expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    // ── CONSTRUCTORS ──────────────────────────────────────────────────
    public Domain() {}

    public Domain(String domainName, DomainStatus status,
                  LocalDate registeredAt, LocalDate expiresAt) {
        this.domainName = domainName;
        this.status = status;
        this.registeredAt = registeredAt;
        this.expiresAt = expiresAt;
    }

    // ── GETTERS & SETTERS ─────────────────────────────────────────────
    public Long getId() { return id; }

    public String getDomainName() { return domainName; }
    public void setDomainName(String domainName) { this.domainName = domainName; }

    public DomainStatus getStatus() { return status; }
    public void setStatus(DomainStatus status) { this.status = status; }

    public LocalDate getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(LocalDate registeredAt) { this.registeredAt = registeredAt; }

    public LocalDate getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDate expiresAt) { this.expiresAt = expiresAt; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    // ── HELPER METHODS ────────────────────────────────────────────────
    public boolean isExpiringSoon() {
        return expiresAt.isBefore(LocalDate.now().plusDays(30))
                && status == DomainStatus.ACTIVE;
    }

    public long daysUntilExpiry() {
        return ChronoUnit.DAYS.between(LocalDate.now(), expiresAt);
    }
}