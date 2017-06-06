package ua.agwebs.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.agwebs.root.entity.BalanceBook;
import ua.agwebs.root.service.CoaService;

@Service
public class PermissionProvider implements PermissionService {

    private static Logger logger = LoggerFactory.getLogger(PermissionProvider.class);

    private CoaService coaService;

    @Autowired
    public PermissionProvider(CoaService coaService) {
        this.coaService = coaService;
    }

    @Override
    public boolean checkPermission(long bookId, long userId) {
        logger.debug("Check permission: bookId = {}, userId = {}", bookId, userId);
        BalanceBook book = coaService.findBalanceBookById(bookId);
        if (book != null && book.getAppUser().getId() == userId) {
            logger.debug("Access allowed: bookId = {}, userId = {}", bookId, userId);
            return true;
        } else {
            logger.debug("Access denied: bookId = {}, userId = {}", bookId, userId);
            return false;
        }
    }
}
