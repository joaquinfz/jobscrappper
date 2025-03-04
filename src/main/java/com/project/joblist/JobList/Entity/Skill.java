package com.project.joblist.JobList.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "skills")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Skill {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name")
    String name;

    @Column(name = "info")
    String info;

    @Column(name = "ocurrences")
    Integer ocurrences;

    @ManyToMany(mappedBy = "skills")
    private List<JobOffer> jobOffers;

}
