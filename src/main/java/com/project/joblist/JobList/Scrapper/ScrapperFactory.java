package com.project.joblist.JobList.Scrapper;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class ScrapperFactory {


    public static Scrapper create(String web){
        if(web.equalsIgnoreCase("linkedin")){
            return new LinkedinSearchScapper();
        } else {
            throw new ResourceNotFoundException("No se encuentra la web: " + web);
        }
    }
}
