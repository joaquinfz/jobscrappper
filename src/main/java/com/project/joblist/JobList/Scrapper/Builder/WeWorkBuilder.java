package com.project.joblist.JobList.Scrapper.Builder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsoup.nodes.Document;

@Setter
@Getter
@NoArgsConstructor
public class WeWorkBuilder {

    String rol;
    String company;
    String headquarters;
    String contractType;
    String offerLocation;
    String offerUrl;
    String salary;
    String skills;
    String offerFullUrl;
    Document offerDoc;
}
