package com.github.jezstewartdev.customerapp.repository;

import com.github.jezstewartdev.customerapp.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

//Spring data avoids need to write standard repository code yourself
public interface CustomerRepository extends JpaRepository<Customer, Integer>

{
}
