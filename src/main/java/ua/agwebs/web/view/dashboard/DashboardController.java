package ua.agwebs.web.view.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"/home"})
public class DashboardController {

    @RequestMapping
    public String getHome() {
        return "dashboard";
    }
}
