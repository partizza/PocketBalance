package ua.agwebs.web.rest.balance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ua.agwebs.root.entity.BSCategory;
import ua.agwebs.root.repo.BalanceLine;
import ua.agwebs.root.repo.ShortBalanceLine;
import ua.agwebs.root.service.EntryService;
import ua.agwebs.web.exceptions.PocketBalanceIllegalAccessException;
import ua.agwebs.web.rest.PermissionService;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

@Service
public class BookBalanceProvider implements BookBalanceService {

    private static Logger logger = LoggerFactory.getLogger(BookBalanceProvider.class);

    private static DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private EntryService entryService;
    private PermissionService permissionService;

    @Autowired
    public BookBalanceProvider(EntryService entryService, PermissionService permissionService) {
        this.entryService = entryService;
        this.permissionService = permissionService;
    }

    @Override
    public BalanceSheetDTO getShortBookBalance(long bookId, LocalDate reportDate, long userId) {
        logger.trace("Calculating short book balance: bookId = {}, reportDate = {}, userId = {}", bookId, reportDate, userId);

        Assert.notNull(reportDate);

        if (permissionService.checkPermission(bookId, userId)) {
            List<ShortBalanceLine> balanceLines = entryService.getShortBookBalance(bookId, reportDate);

            logger.trace("Pivot balance lines: {}", balanceLines);
            Set<String> currencySet = new TreeSet<>();
            Map<String, Map<String, Long>> grpBal = new HashMap<>();
            balanceLines.stream()
                    .forEach(e -> {
                        Map<String, Long> cm = grpBal.getOrDefault(e.getBsCategory().toString(), new HashMap<>());
                        Long amt = cm.getOrDefault(e.getCurrencyCode(), 0L);
                        amt += e.getOutstanding();
                        cm.put(e.getCurrencyCode(), amt);
                        grpBal.put(e.getBsCategory().toString(), cm);
                        currencySet.add(e.getCurrencyCode());
                    });

            logger.trace("Currency list: {}", currencySet);
            logger.trace("Balance articles: {}", grpBal);

            BalanceSheetDTO shortBal = new BalanceSheetDTO(bookId, reportDate);

            Arrays.stream(BSCategory.values()).forEach(e -> {
                String name = e.toString();
                String[] row = new String[currencySet.size() + 2];
                row[0] = String.valueOf(getBSCategoryNumber(e));
                row[1] = name;
                int index = 2;
                for (String c : currencySet) {
                    double amt = grpBal.getOrDefault(name, new HashMap<>()).getOrDefault(c, 0L) / 100.00;
                    row[index] = decimalFormat.format(amt);
                    index++;
                }

                shortBal.addData(row);
            });

            List<ColumnDefinition> columns = new ArrayList<>(Arrays.asList(
                    new ColumnDefinition("#", false),
                    new ColumnDefinition("Article", false)));
            currencySet.stream().forEach(e -> columns.add(new ColumnDefinition(e, true)));
            shortBal.setColumns(columns);

            logger.info("Short balance calculated: bookId = {}, reportDate = {}, userId = {}", bookId, reportDate, userId);
            logger.debug("Short balance: {}", shortBal);

            return shortBal;
        } else {
            throw new PocketBalanceIllegalAccessException("Creating entry - permission denied: bookId = " + bookId + ", userId = " + userId);
        }
    }

    @Override
    public BalanceSheetDTO getBookBalance(long bookId, LocalDate reportDate, long userId) {
        logger.trace("Calculate book balance: bookId = {}, reportDate = {}, userId = {}", bookId, reportDate, userId);

        Assert.notNull(reportDate);

        if (permissionService.checkPermission(bookId, userId)) {
            List<BalanceLine> balanceLines = entryService.getBookBalance(bookId, reportDate);

            logger.trace("Pivot balance lines: {}", balanceLines);

            Set<String> currencySet = new TreeSet<>();
            Map<Long, Map<String, Long>> grpBal = new HashMap<>();
            balanceLines.stream()
                    .forEach(e -> {
                        Map<String, Long> cm = grpBal.getOrDefault(e.getAccId(), new HashMap<>());
                        Long amt = cm.getOrDefault(e.getCurrencyCode(), 0L);
                        amt += e.getOutstanding();
                        cm.put(e.getCurrencyCode(), amt);
                        grpBal.put(e.getAccId(), cm);
                        currencySet.add(e.getCurrencyCode());
                    });

            Map<Long, BalanceLine> blMap = new TreeMap<>();
            balanceLines.stream().forEach(e -> blMap.put(e.getAccId(), e));

            logger.trace("Currency list: {}", currencySet);
            logger.trace("Balance articles: {}", grpBal);

            BalanceSheetDTO balanceDTO = new BalanceSheetDTO(bookId, reportDate);

            grpBal.keySet().stream().forEach(e -> {
                String[] row = new String[currencySet.size() + 3];
                row[0] = blMap.get(e).getBsCategory().toString();
                row[1] = blMap.get(e).getAccId().toString();
                row[2] = blMap.get(e).getAccount();

                int index = 3;
                for (String c : currencySet) {
                    double amt = grpBal.getOrDefault(e, new HashMap<>()).getOrDefault(c, 0L) / 100.00;
                    row[index] = decimalFormat.format(amt);
                    index++;
                }

                balanceDTO.addData(row);
            });


            List<ColumnDefinition> columns = new ArrayList<>(Arrays.asList(
                    new ColumnDefinition("Category", false),
                    new ColumnDefinition("AccountId", true, false),
                    new ColumnDefinition("Account", false)));
            currencySet.stream().forEach(e -> columns.add(new ColumnDefinition(e, true)));
            balanceDTO.setColumns(columns);

            logger.info("Balance calculated: bookId = {}, reportDate = {}, userId = {}", bookId, reportDate, userId);
            logger.debug("Balance sheet: {}", balanceDTO);

            return balanceDTO;
        } else {
            throw new PocketBalanceIllegalAccessException("Creating entry - permission denied: bookId = " + bookId + ", userId = " + userId);
        }

    }

    private int getBSCategoryNumber(BSCategory category) {
        switch (category) {
            case ASSET:
                return 1;
            case LIABILITY:
                return 2;
            case EQUITY:
                return 3;
            case PROFIT:
                return 4;
            default:
                return 5;
        }
    }
}

