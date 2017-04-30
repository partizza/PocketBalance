package ua.agwebs.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ua.agwebs.root.entity.*;
import ua.agwebs.root.events.BookCreatedEvent;
import ua.agwebs.root.service.BusinessTransactionService;
import ua.agwebs.root.service.CoaService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toMap;

@Component
public class BookDefaultContentInit {

    private static final Logger logger = LoggerFactory.getLogger(BookDefaultContentInit.class);

    private CoaService coaService;

    private BusinessTransactionService businessTransactionService;

    @Autowired
    public BookDefaultContentInit(CoaService coaService, BusinessTransactionService businessTransactionService){
        this.coaService = coaService;
        this.businessTransactionService = businessTransactionService;
    }

    @EventListener
    public void init(BookCreatedEvent event){
        logger.debug("Initialize default chart of accounts: book = {}", event.getBook());
        List<BalanceAccount> accountlist = coaService.createBalanceAccount(this.getInitBalanceAccounts(event.getBook()));

        Map<Long, BalanceAccount> accountMap = new HashMap<>();
        accountMap = accountlist.stream().collect(toMap(BalanceAccount::getAccId, Function.identity()));


        logger.debug("Initialize default business transactions settings: book = {}", event.getBook());
        Transaction transaction = new Transaction("Salary receipt", event.getBook());
        transaction = businessTransactionService.createTransaction(transaction);
        TransactionDetail transactionDetail = new TransactionDetail(transaction, accountMap.get(110L), EntrySide.D);
        businessTransactionService.setTransactionDetail(transactionDetail);
        transactionDetail = new TransactionDetail(transaction, accountMap.get(310L), EntrySide.C);
        businessTransactionService.setTransactionDetail(transactionDetail);
    }

    private List<BalanceAccount> getInitBalanceAccounts(BalanceBook book) {
        List<BalanceAccount> initAccounts = new ArrayList<>();
        initAccounts.add(new BalanceAccount(BSCategory.ASSET, 110L, book, "Cash on hand"));
        initAccounts.add(new BalanceAccount(BSCategory.ASSET, 120L, book, "Current accounts"));
        initAccounts.add(new BalanceAccount(BSCategory.ASSET, 130L, book, "Saving accounts"));
        initAccounts.add(new BalanceAccount(BSCategory.ASSET, 140L, book, "Deposits"));
        initAccounts.add(new BalanceAccount(BSCategory.ASSET, 150L, book, "Loans to others"));
        initAccounts.add(new BalanceAccount(BSCategory.ASSET, 190L, book, "Other cash"));

        initAccounts.add(new BalanceAccount(BSCategory.LIABILITY, 210L, book, "Credit cards"));
        initAccounts.add(new BalanceAccount(BSCategory.LIABILITY, 220L, book, "Loans from others"));
        initAccounts.add(new BalanceAccount(BSCategory.LIABILITY, 290L, book, "Other debts"));

        initAccounts.add(new BalanceAccount(BSCategory.PROFIT, 310L, book, "Salary"));
        initAccounts.add(new BalanceAccount(BSCategory.PROFIT, 320L, book, "Interest incomes"));
        initAccounts.add(new BalanceAccount(BSCategory.PROFIT, 390L, book, "Other incomes"));

        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 411L, book, "Rent/mortgage expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 412L, book, "Phone expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 413L, book, "Electricity expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 414L, book, "Gas expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 415L, book, "Water and sewer expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 416L, book, "Internet expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 417L, book, "Housing supplies expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 419L, book, "Other expenses"));

        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 421L, book, "Bus fare expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 422L, book, "Taxi fare expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 423L, book, "Fuel expenses"));

        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 430L, book, "Insurance expenses"));

        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 441L, book, "Food expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 442L, book, "Dining out expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 443L, book, "Medical expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 444L, book, "Clothing expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 445L, book, "Gym expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 446L, book, "Organization due expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 447L, book, "Events/parties expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 448L, book, "Gifts expenses"));

        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 451L, book, "Loans expenses"));

        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 461L, book, "Pets expenses"));

        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 471L, book, "Children expenses"));

        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 490L, book, "Other expenses"));

        initAccounts.add(new BalanceAccount(BSCategory.EQUITY, 500L, book, "Start-up capital"));

        return initAccounts;
    }
}
