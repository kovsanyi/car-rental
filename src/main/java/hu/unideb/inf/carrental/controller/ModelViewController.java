package hu.unideb.inf.carrental.controller;

import hu.unideb.inf.carrental.commons.domain.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ModelViewController {
    @GetMapping(value = {"/", "/login"})
    public ModelAndView login(@RequestParam(value = "error", required = false) String error) {
        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", "Invalid username and password!");
        }
        model.setViewName("login");
        return model;
    }

    @GetMapping("/signup")
    public ModelAndView signUp() {
        ModelAndView model = new ModelAndView();
        model.addObject("user", new User());
        model.setViewName("signup");
        return model;
    }

    @GetMapping("/home")
    public ModelAndView home() {
        ModelAndView model = new ModelAndView();
        model.setViewName("home");
        return model;
    }

    @GetMapping("/home/company")
    public ModelAndView company() {
        ModelAndView model = new ModelAndView();
        model.setViewName("home/company");
        return model;
    }

    @GetMapping("/home/site")
    public ModelAndView site() {
        ModelAndView model = new ModelAndView();
        model.setViewName("home/site");
        return model;
    }

    @GetMapping("/home/car")
    public ModelAndView car() {
        ModelAndView model = new ModelAndView();
        model.setViewName("home/car");
        return model;
    }

    @GetMapping("/home/profile")
    public ModelAndView profile() {
        ModelAndView model = new ModelAndView();
        model.setViewName("home/profile");
        return model;
    }
}
