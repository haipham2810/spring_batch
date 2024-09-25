package com.example.spb_dlsource_into_db.repository;

import com.example.spb_dlsource_into_db.entity.DLCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DLCustomerRepository extends JpaRepository<DLCustomer, Long> {

}
