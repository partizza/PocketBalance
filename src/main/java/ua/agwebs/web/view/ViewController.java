package ua.agwebs.web.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

    @RequestMapping(value = {"/"})
    public String getLoginPage(){
        return "login";
    }

    @RequestMapping(value = {"/home"})
    public String getHome() {
        return "dashboard";
    }

    @RequestMapping(value = {"/accounting"})
    public String getAccountingPage() {
        return "accounting";
    }

    @RequestMapping(value = {"/balance"})
    public String getBalancePage() {
        return "balance";
    }

    @RequestMapping(value = "/setting/account")
    public String getSettingAccountView(){
        return "accounts";
    }

    @RequestMapping(value = "/setting/transaction")
    public String getSettingTransactionView(){
        return "transactions";
    }
}
