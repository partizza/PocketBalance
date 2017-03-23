package ua.agwebs.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
public class SimpleController {

    @RequestMapping(value = {"/","home"})
    public String getHello() {
        return "home";
    }

}
