package com.duberlyguarnizo.clothingplaceholder.audit;

import com.duberlyguarnizo.clothingplaceholder.admin.Administrator;
import com.duberlyguarnizo.clothingplaceholder.admin.AdministratorRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomAuditorAware implements AuditorAware<Long> {
    private final AdministratorRepository administratorRepository;

    public CustomAuditorAware(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }


    @Override
    public @NotNull Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        } else {
            Administrator principal = (Administrator) authentication.getPrincipal();
            String username = principal.getUsername();
            Optional<Administrator> currentUser = administratorRepository.findByUsername(username);
            return currentUser.map(Administrator::getId);
        }
    }
}