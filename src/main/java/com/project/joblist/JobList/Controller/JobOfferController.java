package com.project.joblist.JobList.Controller;

import com.project.joblist.JobList.Dtos.JobOfferDTO;
import com.project.joblist.JobList.Entity.JobOffer;
import com.project.joblist.JobList.Service.JobOfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/offers")
@Tag(name = "User Management", description = "Operations related to users")
public class JobOfferController {

    private final JobOfferService jobOfferService;
    @Autowired
    public JobOfferController(JobOfferService jobOfferService){
        this.jobOfferService = jobOfferService;
    }


    @GetMapping
    @Operation(summary = "Get all job offers", description = "Fetch the list of all job offers")
    public List<JobOfferDTO> getJobOffers(@RequestParam(required = false) List<String> web, @RequestParam String search){
        //web se refiere a desde cual web hacer el scrap
        return jobOfferService.getJobOffers(web,search);
    }

    @GetMapping("/skills/{skills}")
    @Operation(summary = "Get all job offers that demand a certain skill", description = "Fetch the list of all job offers")
    public List<JobOfferDTO> getJobOffersBySkill(@RequestParam(required = false) List<String> web,
                                                 @RequestParam String search,
                                                 @PathVariable List<String> skills){
        //web se refiere a desde cual web hacer el scrap
        return jobOfferService.getJobOffersBySkill(web,search,skills);
    }

}
