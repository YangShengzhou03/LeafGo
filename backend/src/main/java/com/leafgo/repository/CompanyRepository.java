package com.leafgo.repository;

import com.leafgo.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByUserId(Long userId);

    Page<Company> findByIndustry(String industry, Pageable pageable);

    Page<Company> findByNameContaining(String keyword, Pageable pageable);

    Page<Company> findByVerifiedTrue(Pageable pageable);
}
