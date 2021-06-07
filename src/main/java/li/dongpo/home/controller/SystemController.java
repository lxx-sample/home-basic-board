package li.dongpo.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author dongpo.li
 * @date 2021/6/7
 */
@Controller
@RequestMapping("/angular")
public class SystemController {

    @GetMapping("/")
    public String index() {
        return "redirect:/angular/index.html";
    }

}
