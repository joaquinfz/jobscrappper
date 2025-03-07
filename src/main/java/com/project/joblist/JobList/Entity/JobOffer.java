package com.project.joblist.JobList.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "job_offers")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobOffer implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    //ArrayList<String> technologies;

    //Location location;

    //Timestamp fechaPublicacion;

    @Column(name = "rol")
    String rol;
    @Column(name = "company")
    String company;
    @Column(name = "headquarters")
    String headquarters;
    @Column(name = "contractType")
    String contractType;
    @Column(name = "offerLocation")
    String offerLocation;
    @Column(name = "offerUrl")
    String offerUrl;
    @Column(name = "salary")
    String salary;
    @Column(name = "scrappedFrom")
    String scrappedFrom;
    @Column(name = "fullOffer")
    String fullOffer;
    @ManyToMany
    @JoinTable(
            name = "job_skills",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )private List<Skill> skills;



}
