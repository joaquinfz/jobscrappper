package com.project.joblist.JobList.Service;

import com.project.joblist.JobList.Dtos.JobOfferDTO;
import com.project.joblist.JobList.Entity.JobOffer;
import com.project.joblist.JobList.Repository.JobOfferRepository;
import com.project.joblist.JobList.Scrapper.LinkedinSearchScapper;
import com.project.joblist.JobList.Scrapper.Scrapper;
import com.project.joblist.JobList.Scrapper.ScrapperFactory;
import com.project.joblist.JobList.Scrapper.WeWorkRemotelyScrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobOfferService {

    private final LinkedinSearchScapper linkedinSearchScapper;
    private final WeWorkRemotelyScrapper weWorkRemotelyScrapper;

    private final JobOfferRepository jobOfferRepository;

    @Autowired
    public JobOfferService(LinkedinSearchScapper linkedinSearchScapper, WeWorkRemotelyScrapper weWorkRemotelyScrapper,
                           JobOfferRepository jobOfferRepository){
        this.linkedinSearchScapper = linkedinSearchScapper;
        this.weWorkRemotelyScrapper = weWorkRemotelyScrapper;
        this.jobOfferRepository = jobOfferRepository;
    }

    public List<JobOfferDTO> getJobOffers(List<String> webList, String search){
       ArrayList<JobOffer> jobOffers = new ArrayList<>();
        if(webList == null){
            jobOffers.addAll(weWorkRemotelyScrapper.scrap(search));
        }
        else{
            for(String web : webList){
                Scrapper scrapper = ScrapperFactory.create(web);
                jobOffers.addAll(scrapper.scrap(search));
            }
        }

        //SE GUARDAN LAS OFERTAS DE TRABAJO NUEVAS
        jobOffers.forEach(this::saveJobOffer);


        //SE TRAEN TODAS?? LAS OFERTAS DE TRABAJO EN DB
        return jobOfferRepository.findAll().stream().map(JobOfferDTO::new).toList();
    }

    public List<JobOfferDTO> getJobOffersBySkill(List<String> web,String search,List<String> skills){
        skills = skills.stream().map(x -> x.toUpperCase()).toList();
        List<JobOffer> offers = jobOfferRepository.getJobOffersBySkills(skills,skills.size());
        return offers.stream().map(JobOfferDTO::new).toList();
    }

    @Transactional
    public JobOffer saveJobOffer(JobOffer jobOffer) {
        jobOfferRepository.insertJobOffer(
                jobOffer.getCompany(),
                jobOffer.getContractType(),
                //jobOffer.getFechaPublicacion().toString(),
                jobOffer.getFullOffer(),
                jobOffer.getHeadquarters(),
                //jobOffer.getLocation(),
                jobOffer.getOfferLocation(),
                jobOffer.getOfferUrl(),
                jobOffer.getRol(),
                jobOffer.getSalary(),
                jobOffer.getScrappedFrom()
                //jobOffer.getTechnologies()
        );

        Long generatedId = jobOfferRepository.getLastInsertedId();
        jobOffer.setId(generatedId); // Manually assign the ID

        return jobOffer;
    }
}
