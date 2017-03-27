package ua.agwebs.web.rest.Accounts;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.agwebs.root.entity.BalanceAccount;
import ua.agwebs.root.service.CoaService;

@Service
public class BalanceAccountProvider implements BalanceAccountService{

    private CoaService coaService;
    private ModelMapper mapper;

    @Autowired
    public BalanceAccountProvider(CoaService coaService, ModelMapper mapper){
        this.coaService = coaService;
        this.mapper = mapper;
    }

    @Override
    public BalanceAccountDTO findById(long bookId, long accountId) {
        BalanceAccount balanceAccount = coaService.findBalanceAccountById(bookId, accountId);
        BalanceAccountDTO dto = mapper.map(balanceAccount, BalanceAccountDTO.class);
        return dto;
    }
}
