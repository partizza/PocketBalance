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
import ua.agwebs.web.rest.PermissionService;

import java.util.ArrayList;
import java.util.List;

@Service
public class BalanceAccountProvider implements BalanceAccountService {

    private static final Logger logger = LoggerFactory.getLogger(BalanceAccountProvider.class);

    private CoaService coaService;
    private ModelMapper mapper;
    private PermissionService permissionService;

    @Autowired
    public BalanceAccountProvider(CoaService coaService, ModelMapper mapper, PermissionService permissionService) {
        this.coaService = coaService;
        this.mapper = mapper;
        this.permissionService = permissionService;
    }

    @Override
    public BalanceAccountDTO findBalanceAccountById(long bookId, long accountId, long userId) {
        logger.debug("Find a balance account: userId= {}, bookId = {}, accountId = {}", userId, bookId, accountId);
        if (permissionService.checkPermission(bookId, userId)) {
            BalanceAccount balanceAccount = coaService.findBalanceAccountById(bookId, accountId);
            BalanceAccountDTO dto = balanceAccount == null ? null : mapper.map(balanceAccount, BalanceAccountDTO.class);
            return dto;
        } else {
            throw new PocketBalanceIllegalAccessException();
        }
    }

    @Override
    public void createBalanceAccount(BalanceAccountDTO dto, long userId) {
        logger.debug("Create balance account: userId = {}, balanceAccountDTO = {}", userId, dto);
        if (permissionService.checkPermission(dto.getBookId(), userId)) {
            BalanceAccount account = new BalanceAccount();
            account.setAccId(dto.getAccId());
            account.setName(dto.getName());
            account.setDesc(dto.getDesc());
            account.setBsCategory(BSCategory.valueOf(dto.getBsCategory()));
            BalanceBook book = coaService.findBalanceBookById(dto.getBookId());
            account.setBook(book);
            coaService.createBalanceAccount(account);
        } else {
            throw new PocketBalanceIllegalAccessException();
        }
    }

    @Override
    public void updateBalanceAccount(BalanceAccountDTO dto, long userId) {
        logger.debug("Update balance account: userId = {}, balanceAccountDTO = {}", userId, dto);
        if (permissionService.checkPermission(dto.getBookId(), userId)) {
            BalanceAccount account = new BalanceAccount();
            account.setAccId(dto.getAccId());
            account.setName(dto.getName());
            account.setDesc(dto.getDesc());
            account.setBsCategory(BSCategory.valueOf(dto.getBsCategory()));
            BalanceBook book = coaService.findBalanceBookById(dto.getBookId());
            account.setBook(book);
            coaService.updateBalanceAccount(account);
        } else {
            throw new PocketBalanceIllegalAccessException();
        }
    }

    @Override
    public List<BalanceAccountDTO> findBalanceAccountAllByBookId(long bookId, long userId) {
        logger.debug("Find all balance account by book: userId = {}, bookId = {}", userId, bookId);
        if (permissionService.checkPermission(bookId, userId)) {
            BalanceBook book = coaService.findBalanceBookById(bookId);
            List<BalanceAccountDTO> dtoList = new ArrayList<>();
            for (BalanceAccount e : book.getAccounts()) {
                dtoList.add(mapper.map(e, BalanceAccountDTO.class));
            }
            return dtoList;
        } else {
            throw new PocketBalanceIllegalAccessException();
        }
    }

}
