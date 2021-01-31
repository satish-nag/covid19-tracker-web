package com.covid19tracker.web.repositories;

import com.covid19tracker.web.entities.Users;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<Users,String > {
    boolean existsUsersByUsernameAndAndHospital_HospitalCode(String username,String hospitalCode);
}
