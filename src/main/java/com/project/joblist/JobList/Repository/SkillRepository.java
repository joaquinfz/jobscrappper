package com.project.joblist.JobList.Repository;


import com.project.joblist.JobList.Entity.JobOffer;
import com.project.joblist.JobList.Entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill,Long> {

    @Query(value = "SELECT * FROM skills s WHERE s.name = :name" +
            " LIMIT 1", nativeQuery = true)
    Optional<Skill> findSameSkill(@Param("name") String name);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO skills (name, info, ocurrences) VALUES (:name, :info, :ocurrences)", nativeQuery = true)
    void insertSkill(@Param("name") String name,
                        @Param("info") String info,
                        @Param("ocurrences") Integer ocurrences);
    @Query(value = "SELECT * \n" +
            "FROM skills\n" +
            "ORDER BY id DESC\n" +
            "LIMIT 1;", nativeQuery = true)
    Optional<Skill> getLastInsertedSkill();

    @Modifying
    @Transactional
    @Query(value = "UPDATE skills SET ocurrences = ocurrences + 1 WHERE name = :#{#skill.name}", nativeQuery = true)
    void incrementOccurrences(@Param("skill") Skill skill);
}
