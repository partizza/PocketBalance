package ua.agwebs.root.service;


import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import ua.agwebs.root.entity.AppUser;

import javax.validation.Valid;


@Validated
public interface AppUserService {

    public boolean existsByEmail(@Email String email);

    public AppUser findByEmail(@Email String email);

    public AppUser createAppUser(@Valid AppUser appUser);
}
