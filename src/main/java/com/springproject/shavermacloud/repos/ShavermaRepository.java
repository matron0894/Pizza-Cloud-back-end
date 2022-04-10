package com.springproject.shavermacloud.repos;

import com.springproject.shavermacloud.domain.Shaverma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShavermaRepository extends JpaRepository<Shaverma, Long> {

}
