package ua.agwebs.tests.unit.web.rest.accounts;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ua.agwebs.root.entity.AppUser;
import ua.agwebs.root.entity.BSCategory;
import ua.agwebs.root.entity.BalanceAccount;
import ua.agwebs.root.entity.BalanceBook;
import ua.agwebs.root.service.CoaService;
import ua.agwebs.web.PageDTO;
import ua.agwebs.web.rest.accounts.BalanceAccountDTO;
import ua.agwebs.web.rest.accounts.BalanceAccountProvider;
import ua.agwebs.web.rest.accounts.BalanceAccountService;

import java.util.Arrays;

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
        BalanceBook book = new BalanceBook("test", "Mockito test", appUser);
        book.setId(10L);
        balanceAccount = new BalanceAccount(BSCategory.PROFIT, 700L, book, "test account");

        mapper = new ModelMapper();

        dto = mapper.map(balanceAccount, BalanceAccountDTO.class);

        balanceAccountService = new BalanceAccountProvider(coaService, mapper);
    }

    @Test
    public void test_findById() {
        when(coaService.findBalanceAccountById(balanceAccount.getAccId(), balanceAccount.getBook().getId())).thenReturn(balanceAccount);
        BalanceAccountDTO resultDto = balanceAccountService.findById(balanceAccount.getAccId(), balanceAccount.getBook().getId());

        assertEquals(dto, resultDto);

        when(coaService.findBalanceAccountById(-10L, -700L)).thenReturn(null);
        resultDto = balanceAccountService.findById(-10L, -700L);

        assertNull("Incorrect result.", resultDto);
    }

    @Test
    public void test_createBalanceAccount() {
        when(coaService.findBalanceBookById(dto.getBookId())).thenReturn(balanceAccount.getBook());
        when(coaService.createBalanceAccount(any(BalanceAccount.class))).thenReturn(null);

        balanceAccountService.createBalanceAccount(dto);

        verify(coaService, times(1)).findBalanceBookById(dto.getBookId());
        verify(coaService, times(1)).createBalanceAccount(balanceAccount);
    }

    @Test
    public void test_findAll() {
        Pageable pageable = Mockito.mock(Pageable.class);
        when(coaService.findAllBalanceAccount(pageable)).thenReturn(new PageImpl<BalanceAccount>(Arrays.asList(balanceAccount)));

        PageDTO<BalanceAccountDTO> pageDTO = balanceAccountService.findAll(pageable);

        assertTrue(pageDTO.getContent().contains(dto));
    }
}
