package ua.agwebs.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.stereotype.Component;
import ua.agwebs.root.entity.AppUser;
import ua.agwebs.root.service.AppUserService;

import java.util.Map;

@Component
public class AppPrincipalExtractor implements PrincipalExtractor{

    private static final Logger logger = LoggerFactory.getLogger(AppPrincipalExtractor.class);

    private AppUserService userService;

    @Autowired
    public AppPrincipalExtractor(AppUserService userService) {
        this.userService = userService;
    }

    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        String email = (String) map.get("email");
        AppUser appUser = userService.findByEmail(email);
        if(appUser == null){
            logger.info("Persist new user");

            appUser = new AppUser(email);
            String name = (String) map.get("given_name");
            appUser.setName(name);
            String surname = (String) map.get("family_name");
            appUser.setSurname(surname);
            appUser = userService.createAppUser(appUser);

        }
        return new AppUserDetails(appUser.getEmail(), appUser.getName(), appUser.getSurname());
    }
}
