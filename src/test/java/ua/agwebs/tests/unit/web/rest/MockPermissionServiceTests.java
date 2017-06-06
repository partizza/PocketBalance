package ua.agwebs.tests.unit.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import ua.agwebs.root.entity.AppUser;
import ua.agwebs.root.entity.BSCategory;
import ua.agwebs.root.entity.BalanceAccount;
import ua.agwebs.root.entity.BalanceBook;
import ua.agwebs.root.service.CoaService;
import ua.agwebs.web.rest.PermissionProvider;
import ua.agwebs.web.rest.PermissionService;
import ua.agwebs.web.rest.accounts.BalanceAccountDTO;
import ua.agwebs.web.rest.accounts.BalanceAccountProvider;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MockPermissionServiceTests {

    @Mock
    private CoaService coaService;

    private PermissionService permissionService;

    private AppUser appUser;

    private BalanceBook book;

    @Before
    public void setUpMock() {
        appUser = new AppUser("b@u.cn");
        appUser.setId(777L);
        book = new BalanceBook("test", "Mockito test", appUser);
        book.setId(10L);

        permissionService = new PermissionProvider(coaService);
    }

    @Test
    public void test_AccessDenied() {
        when(coaService.findBalanceBookById(book.getId())).thenReturn(book);
        boolean result = permissionService.checkPermission(book.getId(), appUser.getId() + 1);
        assertFalse("Incorrect result", result);
    }

    @Test
    public void test_AccessPermitted(){
        when(coaService.findBalanceBookById(book.getId())).thenReturn(book);
        boolean result = permissionService.checkPermission(book.getId(), appUser.getId());
        assertTrue("Incorrect result", result);
    }

    @Test
    public void test_AccessDenied_NonExistingBook(){
        when(coaService.findBalanceBookById(anyLong())).thenReturn(null);
        boolean result = permissionService.checkPermission(book.getId(), appUser.getId());
        assertFalse("Incorrect result", result);
    }
}
