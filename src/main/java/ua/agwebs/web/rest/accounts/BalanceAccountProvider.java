package ua.agwebs.web.rest.accounts;


import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ua.agwebs.root.entity.BSCategory;
import ua.agwebs.root.entity.BalanceAccount;
import ua.agwebs.root.entity.BalanceBook;
import ua.agwebs.root.service.AppUserManager;
import ua.agwebs.root.service.CoaService;
import ua.agwebs.web.PageDTO;
import ua.agwebs.web.exceptions.PocketBalanceIllegalAccessException;

import java.util.ArrayList;
import java.util.List;

@Service
public class BalanceAccountProvider implements BalanceAccountService {

    private static final Logger logger = LoggerFactory.getLogger(BalanceAccountProvider.class);

    private CoaService coaService;
    private ModelMapper mapper;

    @Autowired
    public BalanceAccountProvider(CoaService coaService, ModelMapper mapper) {
        this.coaService = coaService;
        this.mapper = mapper;
    }

    @Override
    public BalanceAccountDTO findBalanceAccountById(long bookId, long accountId) {
        BalanceAccount balanceAccount = coaService.findBalanceAccountById(bookId, accountId);
        BalanceAccountDTO dto = balanceAccount == null ? null : mapper.map(balanceAccount, BalanceAccountDTO.class);
        return dto;
    }

    @Override
    public void createBalanceAccount(BalanceAccountDTO dto) {
        BalanceAccount account = new BalanceAccount();
        account.setAccId(dto.getAccId());
        account.setName(dto.getName());
        account.setDesc(dto.getDesc());
        account.setBsCategory(BSCategory.valueOf(dto.getBsCategory()));
        BalanceBook book = coaService.findBalanceBookById(dto.getBookId());
        account.setBook(book);
        coaService.createBalanceAccount(account);
    }

    @Override
    public PageDTO<BalanceAccountDTO> findBalanceAccountAll(Pageable pageable) {
        Assert.notNull(pageable);
        Page<BalanceAccount> page = coaService.findAllBalanceAccount(pageable);
        PageDTO<BalanceAccountDTO> pageDTO = new PageDTO<>(page.getNumber(), page.getTotalPages());
        for (BalanceAccount e : page.getContent()) {
            BalanceAccountDTO dto = mapper.map(e, BalanceAccountDTO.class);
            pageDTO.addContent(dto);
        }
        return pageDTO;
    }

    @Override
    public List<BalanceAccountDTO> findBalanceAccountAllByBookId(long bookId, long userId) {
        logger.debug("Check permission: bookId = {}, userId = {}", bookId, userId);
        BalanceBook book = coaService.findBalanceBookById(bookId);
        if (book.getAppUser().getId() == userId) {
            logger.debug("Access allowed: bookId = {}, userId = {}", bookId, userId);
            List<BalanceAccountDTO> dtoList = new ArrayList<>();
            for (BalanceAccount e : book.getAccounts()) {
                dtoList.add(mapper.map(e, BalanceAccountDTO.class));
            }
            return dtoList;
        } else {
            logger.debug("Access denied: bookId = {}, userId = {}", bookId, userId);
            throw new PocketBalanceIllegalAccessException();
        }
    }
}
