package ua.agwebs.web.view.Accounts;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/setting/account")
public class AccountsController {

    @RequestMapping
    public String getSettingAccountView(){
        return "accounts";
    }
}