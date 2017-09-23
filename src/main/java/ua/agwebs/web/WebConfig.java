package ua.agwebs.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import ua.agwebs.web.rest.entries.datatables.DataTableRequestResolver;

import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter{

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new DataTableRequestResolver());
    }

}
