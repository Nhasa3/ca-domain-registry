package com.CIRA_N.Domain_Registery.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Domain")
public class Domain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "domain_name", nullable = false, unique = true)
    private String domainName;

    @Column(nullable = false)
    private String status;

    @Column
    private LocalDate expiry;

    public Domain(){}

    public Domain(String domainName, String status, LocalDate expiry) {
        this.domainName = domainName;
        this.status = status;
        this.expiry = expiry;
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

    public LocalDate getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDate expiry) {
        this.expiry = expiry;
    }
}

}
