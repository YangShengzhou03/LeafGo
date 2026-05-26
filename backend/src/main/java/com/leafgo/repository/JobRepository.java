package com.leafgo.repository;

import com.leafgo.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    Page<Job> findByCompanyId(Long companyId, Pageable pageable);

    Page<Job> findByCompanyIdAndStatus(Long companyId, Job.JobStatus status, Pageable pageable);

    Page<Job> findByStatus(Job.JobStatus status, Pageable pageable);

    @Query(value = "SELECT j FROM Job j LEFT JOIN FETCH j.company WHERE " +
           "(:keyword IS NULL OR j.title LIKE CONCAT('%', :keyword, '%') OR j.description LIKE CONCAT('%', :keyword, '%')) AND " +
           "(:city IS NULL OR j.city LIKE CONCAT('%', :city, '%')) AND " +
           "(:jobType IS NULL OR j.jobType = :jobType) AND " +
           "(:salary IS NULL OR j.salary LIKE CONCAT('%', :salary, '%')) AND " +
           "(:experience IS NULL OR j.experience = :experience) AND " +
           "(:education IS NULL OR j.education = :education) AND " +
           "j.status = 'ACTIVE'",
           countQuery = "SELECT COUNT(j) FROM Job j WHERE " +
           "(:keyword IS NULL OR j.title LIKE CONCAT('%', :keyword, '%') OR j.description LIKE CONCAT('%', :keyword, '%')) AND " +
           "(:city IS NULL OR j.city LIKE CONCAT('%', :city, '%')) AND " +
           "(:jobType IS NULL OR j.jobType = :jobType) AND " +
           "(:salary IS NULL OR j.salary LIKE CONCAT('%', :salary, '%')) AND " +
           "(:experience IS NULL OR j.experience = :experience) AND " +
           "(:education IS NULL OR j.education = :education) AND " +
           "j.status = 'ACTIVE'")
    Page<Job> searchJobs(@Param("keyword") String keyword,
                         @Param("city") String city,
                         @Param("jobType") String jobType,
                         @Param("salary") String salary,
                         @Param("experience") String experience,
                         @Param("education") String education,
                         Pageable pageable);

    @Query("SELECT COUNT(j) FROM Job j WHERE j.companyId = :companyId AND j.status = 'ACTIVE'")
    Long countActiveJobsByCompanyId(@Param("companyId") Long companyId);
}
