package com.project.joblist.JobList.Repository;

import com.project.joblist.JobList.Entity.JobOffer;
import com.project.joblist.JobList.Entity.Location;
import com.project.joblist.JobList.Entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer,Long> {

    @Query(value = "SELECT * FROM job_offers j WHERE j.rol = :rol AND j.company = :company AND " +
            "j.scrapped_from = :scrappedFrom AND j.offer_location = :offerLocation" +
            " LIMIT 1", nativeQuery = true)
    Optional<JobOffer> findSameJobOffer(@Param("rol") String rol,
                                        @Param("company") String company,
                                        @Param("offerLocation") String offerLocation,
                                        @Param("scrappedFrom") String scrappedFrom);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO job_offers (company, contract_type, full_offer, headquarters, offer_location, offer_url, rol, salary, scrapped_from) VALUES (:company, :contractType, :fullOffer, :headquarters, :offerLocation, :offerUrl, :rol, :salary, :scrappedFrom)", nativeQuery = true)
    void insertJobOffer(@Param("company") String company,
                        @Param("contractType") String contractType,
                        @Param("fullOffer") String fullOffer,
                        @Param("headquarters") String headquarters,
                        @Param("offerLocation") String offerLocation,
                        @Param("offerUrl") String offerUrl,
                        @Param("rol") String rol,
                        @Param("salary") String salary,
                        @Param("scrappedFrom") String scrappedFrom);

    @Query(value = "SELECT last_insert_rowid()", nativeQuery = true)
    Long getLastInsertedId();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO job_skills (job_id, skill_id) VALUES (:offerId, :skillId)", nativeQuery = true)
    void insertJobSkill(@Param("offerId") Long jobId, @Param("skillId") Long skillId);

    @Transactional
    @Query(value = "SELECT * FROM job_offers WHERE id IN " +
            "(SELECT job_id FROM job_skills WHERE skill_id IN " +
            "(SELECT id FROM skills WHERE name = :skillName))", nativeQuery = true)
    List<JobOffer> getJobOffersBySkillName(@Param("skillName")List<String> skillName);

}
