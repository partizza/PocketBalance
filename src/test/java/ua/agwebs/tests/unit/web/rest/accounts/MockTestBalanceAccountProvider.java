package ua.agwebs.tests.unit.web.rest.accounts;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ua.agwebs.root.entity.AppUser;
import ua.agwebs.root.entity.BSCategory;
import ua.agwebs.root.entity.BalanceAccount;
import ua.agwebs.root.entity.BalanceBook;
import ua.agwebs.root.service.CoaService;
import ua.agwebs.web.PageDTO;
import ua.agwebs.web.exceptions.PocketBalanceIllegalAccessException;
import ua.agwebs.web.rest.accounts.BalanceAccountDTO;
import ua.agwebs.web.rest.accounts.BalanceAccountProvider;
import ua.agwebs.web.rest.accounts.BalanceAccountService;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MockTestBalanceAccountProvider {

    @Mock
    private CoaService coaService;

    private BalanceAccountService balanceAccountService;

    private ModelMapper mapper;

    private BalanceAccount balanceAccount;

    private BalanceAccountDTO dto;

    @Before
    public void setUpMock() {
        AppUser appUser = new AppUser("b@u.cn");
        appUser.setId(777L);
        BalanceBook book = new BalanceBook("test", "Mockito test", appUser);
        book.setId(10L);
        balanceAccount = new BalanceAccount(BSCategory.PROFIT, 700L, book, "test account");

        mapper = new ModelMapper();

        dto = mapper.map(balanceAccount, BalanceAccountDTO.class);

        balanceAccountService = new BalanceAccountProvider(coaService, mapper);
    }

    @Test
    public void test_findById() {
        when(coaService.findBalanceBookById(dto.getBookId())).thenReturn(balanceAccount.getBook());
        when(coaService.findBalanceAccountById(balanceAccount.getBook().getId(), balanceAccount.getAccId())).thenReturn(balanceAccount);

        BalanceAccountDTO resultDto =
                balanceAccountService.findBalanceAccountById(balanceAccount.getBook().getId(), balanceAccount.getAccId(), balanceAccount.getBook().getAppUser().getId());

        assertEquals(dto, resultDto);

        when(coaService.findBalanceAccountById(balanceAccount.getBook().getId(), -700L)).thenReturn(null);

        resultDto =
                balanceAccountService.findBalanceAccountById(balanceAccount.getBook().getId(), -700L, balanceAccount.getBook().getAppUser().getId());

        assertNull("Incorrect result.", resultDto);
    }

    @Test(expected = PocketBalanceIllegalAccessException.class)
    public void test_findById_AccessDenied() {
        when(coaService.findBalanceBookById(dto.getBookId())).thenReturn(balanceAccount.getBook());
        balanceAccountService.findBalanceAccountById(balanceAccount.getBook().getId(), balanceAccount.getAccId(), balanceAccount.getBook().getAppUser().getId() + 1);
    }

    @Test
    public void test_createBalanceAccount() {
        when(coaService.findBalanceBookById(dto.getBookId())).thenReturn(balanceAccount.getBook());
        when(coaService.createBalanceAccount(any(BalanceAccount.class))).thenReturn(null);

        balanceAccountService.createBalanceAccount(dto, balanceAccount.getBook().getAppUser().getId());

        verify(coaService, times(2)).findBalanceBookById(dto.getBookId());
        verify(coaService, times(1)).createBalanceAccount(balanceAccount);
    }

    @Test(expected = PocketBalanceIllegalAccessException.class)
    public void test_createBalanceAccount_AccessDenied() {
        when(coaService.findBalanceBookById(dto.getBookId())).thenReturn(balanceAccount.getBook());
        balanceAccountService.createBalanceAccount(dto, balanceAccount.getBook().getAppUser().getId() + 1);
    }

    @Test
    public void test_updateBalanceAccount() {
        when(coaService.findBalanceBookById(dto.getBookId())).thenReturn(balanceAccount.getBook());
        when(coaService.updateBalanceAccount(balanceAccount)).thenReturn(null);

        balanceAccountService.updateBalanceAccount(dto, balanceAccount.getBook().getAppUser().getId());

        verify(coaService, times(2)).findBalanceBookById(dto.getBookId());
        verify(coaService, times(1)).updateBalanceAccount(balanceAccount);
    }

    @Test(expected = PocketBalanceIllegalAccessException.class)
    public void test_updateBalanceaccount_AccessDenied() {
        when(coaService.findBalanceBookById(dto.getBookId())).thenReturn(balanceAccount.getBook());
        balanceAccountService.updateBalanceAccount(dto, balanceAccount.getBook().getAppUser().getId() + 1);
    }

    @Test
    public void test_findBalanceAccountAllByBookId() {
        AppUser appUser = new AppUser("adc@rt.cv");
        appUser.setId(59L);

        BalanceBook book = new BalanceBook("book", "test", appUser);
        book.setId(77L);

        BalanceAccount account = new BalanceAccount(BSCategory.ASSET, 71L, book, "test account");
        book.addAccount(account);
        BalanceAccountDTO accountDTO = mapper.map(account, BalanceAccountDTO.class);

        when(coaService.findBalanceBookById(book.getId())).thenReturn(book);
        List<BalanceAccountDTO> resultDtoLst = balanceAccountService.findBalanceAccountAllByBookId(book.getId(), appUser.getId());

        assertTrue(resultDtoLst.contains(accountDTO));
    }

    @Test(expected = PocketBalanceIllegalAccessException.class)
    public void test_findBalanceAccountAllByBookId_AccessDenied() {
        AppUser appUser = new AppUser("adc@rt.cv");
        appUser.setId(59L);

        BalanceBook book = new BalanceBook("book", "test", appUser);
        book.setId(77L);

        when(coaService.findBalanceBookById(book.getId())).thenReturn(book);
        balanceAccountService.findBalanceAccountAllByBookId(book.getId(), appUser.getId() + 1);

    }
}
