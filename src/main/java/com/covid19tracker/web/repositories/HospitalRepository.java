package com.covid19tracker.web.repositories;

import com.covid19tracker.web.entities.Hospital;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends CrudRepository<Hospital,String> {
}
