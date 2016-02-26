package diffind;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @RequestMapping("/")
    public String home(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "home";
    }

    @RequestMapping("/s")
    public String search(@RequestParam(value="q", required=true) String query, Model model) {
        model.addAttribute("query", query);
        return "search";
    }

}
