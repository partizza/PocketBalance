package ua.agwebs.tests.integration.root;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ua.agwebs.root.entity.AppUser;
import ua.agwebs.root.service.AppUserService;

import javax.validation.ConstraintViolationException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode= DirtiesContext.ClassMode.BEFORE_CLASS)
public class AppUserServiceTests {

    @Autowired
    private AppUserService userService;

    @Test
    public void test_createAppUser() {
        AppUser appUser = new AppUser("bf@u.com", "An", "Xe");
        AppUser createdAppUser = userService.createAppUser(appUser);

        assertNotNull("Incorrect created user", createdAppUser.getId());
        assertEquals("Incorrect created user.", appUser.getEmail(), createdAppUser.getEmail());
        assertEquals("Incorrect created user.", appUser.getName(), createdAppUser.getName());
        assertEquals("Incorrect created user.", appUser.getSurname(), createdAppUser.getSurname());

    }

    @Test(expected = ConstraintViolationException.class)
    public void test_createAppUser_NullEmail(){
        AppUser appUser = new AppUser();
        AppUser createdAppUser = userService.createAppUser(appUser);
    }

    @Test(expected = ConstraintViolationException.class)
    public void test_createAppUser_BlankEmail(){
        AppUser appUser = new AppUser("");
        AppUser createdAppUser = userService.createAppUser(appUser);
    }

    @Test(expected = ConstraintViolationException.class)
    public void test_createAppUser_NotValidEmail(){
        AppUser appUser = new AppUser("bf.u.net");
        AppUser createdAppUser = userService.createAppUser(appUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_createAppUser_AlreadyExistingEmail(){
        AppUser appUser = new AppUser("bf@u.com", "An", "Xe");
        AppUser createdAppUser = userService.createAppUser(appUser);

        userService.createAppUser(appUser);
    }

    @Test
    public void test_existByEmail(){
        AppUser appUser = new AppUser("existU1@aau.com.agwebs", "An", "Xe");
        AppUser createdAppUser = userService.createAppUser(appUser);

        assertTrue("Incorrect result.", userService.existsByEmail(appUser.getEmail()));
        assertFalse("Incorrect result.", userService.existsByEmail("unexistU@aau.com.agwebs"));
    }

}
