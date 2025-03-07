package com.project.joblist.JobList.Scrapper;

import com.project.joblist.JobList.Entity.JobOffer;

import java.util.List;

public interface Scrapper {

    List<JobOffer> scrap(String search);
}
