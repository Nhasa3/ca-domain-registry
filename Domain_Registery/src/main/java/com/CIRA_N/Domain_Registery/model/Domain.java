package com.CIRA_N.Domain_Registery.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "Domains")
public class Domain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "domain_name", nullable = false, unique = true)
    private String domainName;

    @Column(nullable = false)
    private String status;

    @Column
    private LocalDate expiresAt;

    // Many domains belong to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    public Domain(){}

    public Domain(String domainName, String status, LocalDate expiresAt) {
        this.domainName = domainName;
        this.status = status;
        this.expiresAt = expiresAt;
    }

    public Long getId() {
        return id;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getExpiresAt() {
        return expiresAt;
    }

    public void setExpiry(LocalDate expiresAt) {
        this.expiresAt = expiresAt;
    }

    // Is this domain expiring within the next 30 days?
    public boolean isExpiringSoon(){
        return expiresAt.isBefore(LocalDate.now().plusDays(30))
                && status == DomainStatus.ACTIVE;
    }

    // How many days until expiry
    public long daysUntillExpiry(){
        return ChronoUnit.DAYS.between(LocalDate.now(), expiresAt);
    }
}


