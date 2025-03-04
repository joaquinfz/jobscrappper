package com.project.joblist.JobList.Dtos;

import com.project.joblist.JobList.Entity.JobOffer;
import com.project.joblist.JobList.Entity.Skill;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JobOfferDTO {
    private Long id;
    private String rol;
    private String company;
    private String headquarters;
    private String contractType;
    private String offerLocation;
    private String offerUrl;
    private String salary;
    private String scrappedFrom;
    private String fullOffer;
    private List<String> skills;

    public JobOfferDTO(JobOffer jobOffer) {
        this.id = jobOffer.getId();
        this.rol = jobOffer.getRol();
        this.company = jobOffer.getCompany();
        this.headquarters = jobOffer.getHeadquarters();
        this.contractType = jobOffer.getContractType();
        this.offerLocation = jobOffer.getOfferLocation();
        this.offerUrl = jobOffer.getOfferUrl();
        this.salary = jobOffer.getSalary();
        this.scrappedFrom = jobOffer.getScrappedFrom();
        this.fullOffer = jobOffer.getFullOffer();
        this.skills = jobOffer.getSkills().stream()
                .map(Skill::getName)
                .toList();
    }
}
