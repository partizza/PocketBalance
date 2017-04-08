package ua.agwebs.tests.unit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.agwebs.root.RootConfig;
import ua.agwebs.root.entity.AppUser;
import ua.agwebs.root.entity.BSCategory;
import ua.agwebs.root.entity.BalanceAccount;
import ua.agwebs.root.entity.BalanceBook;
import ua.agwebs.web.rest.accounts.BalanceAccountDTO;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class})
public class TestDTO {

    @Autowired
    private ModelMapper mapper;

    @Test
    public void test_ModelMapper_BalanceAccountDTO() {
        AppUser appUser = new AppUser("bf@u.com", "An", "Xe");
        appUser.setId(999L);
        BalanceBook book = new BalanceBook("book", "some book for testing", appUser);
        book.setId(12L);
        BalanceAccount account = new BalanceAccount(BSCategory.PROFIT, 1002L, book, "Cash", "Cash in walet");

        BalanceAccountDTO accountDTO = mapper.map(account, BalanceAccountDTO.class);

        assertEquals("Incorrect dto object.", account.getAccId(), accountDTO.getAccId());
        assertEquals("Incorrect dto object.", account.getName(), accountDTO.getName());
        assertEquals("Incorrect dto object.", account.getDesc(), accountDTO.getDesc());
        assertEquals("Incorrect dto object.", account.isEnable(), accountDTO.getEnable());
        assertEquals("Incorrect dto object.", account.getBook().getId(), accountDTO.getBookId());
        assertEquals("Incorrect dto object.", account.getBsCategory().name(), accountDTO.getBsCategory());
    }

}
