package com.project.joblist.JobList.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontendController {
    @RequestMapping("/")
    public String redirectToAngular() {
        return "forward:/index.html";  // Serve from /browser/
    }
}