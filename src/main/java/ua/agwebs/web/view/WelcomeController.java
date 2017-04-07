package ua.agwebs.web.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"/"})
public class WelcomeController {

    @RequestMapping
    public String getLoginPage(){
        return "login";
    }
}
