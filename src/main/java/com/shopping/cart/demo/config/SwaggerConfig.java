package com.shopping.cart.demo.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SwaggerConfig {
    @RequestMapping("/swagger-ui")
    public String redirectToUi() {
        return "redirect:/webjars/swagger-ui/index.html?url=/api/swagger";
    }
}
