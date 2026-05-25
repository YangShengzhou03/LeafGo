package com.leafgo.repository;

import com.leafgo.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Page<Application> findByUserId(Long userId, Pageable pageable);

    Page<Application> findByUserIdAndStatus(Long userId, Application.ApplicationStatus status, Pageable pageable);

    Page<Application> findByJobId(Long jobId, Pageable pageable);

    Page<Application> findByStatus(Application.ApplicationStatus status, Pageable pageable);
}
