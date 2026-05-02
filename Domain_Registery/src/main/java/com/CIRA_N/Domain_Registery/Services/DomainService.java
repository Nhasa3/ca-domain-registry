package com.CIRA_N.Domain_Registery.Services;

import com.CIRA_N.Domain_Registery.Repository.DomainRepository;
import com.CIRA_N.Domain_Registery.model.Domain;
import com.CIRA_N.Domain_Registery.model.DomainStatus;
import com.CIRA_N.Domain_Registery.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DomainService {

    @Autowired
    private DomainRepository domainRepository;

    // -- CHECK AVAILABILITY ---
    public boolean isAvailable(String domainName){
        return !domainRepository.existByDomainName(domainName.toLowerCase());
    }

    // -- REGISTER A DOMAIN
    public Domain registerDomain(String domainName, User owner){
        // -- Clean the input
        domainName = domainName.toLowerCase().trim();

        // -- Check if already taken
        if(!isAvailable(domainName)){
            throw new RuntimeException("Domain '" + domainName + "' is already registered.");
        }

        //Validate format
        if(!domainName.endsWith(".ca")){
            throw new RuntimeException("Only .ca domains are supported");
        }

        // -- Build domain object
        Domain domain = new Domain();
        domain.setDomainName(domainName);
        domain.setOwner(owner);
        domain.setStatus(DomainStatus.ACTIVE);
        domain.setRegister(LocalDate.now());
        domain.setExpiry(LocalDate.now().plusYears(1));

        return domainRepository.save(domain);
    }

    // -- RENEW A DOMAIN
    public Domain renewDomain(Long domainId, User currentUser){

        Domain domain = domainRepository.findById(domainId)
                .orElseThrow(() -> new RuntimeException("Domain not found"));

        // Make sure the user owns this domain
        if(!domain.getOwner().getId().equals(currentUser.getId())){
            throw new RuntimeException("You do not own this domain");
        }

        // Extend expiry by 1 year from today or from current expiry
        LocalDate baseDate = domain.getExpiry().isAfter(LocalDate.now())
                ? domain.getExpiry()
                : LocalDate.now();

        domain.setExpiry(baseDate.plusYears(1));
        domain.setStatus(DomainStatus.ACTIVE);

        return domainRepository.save(domain);

    }
}
