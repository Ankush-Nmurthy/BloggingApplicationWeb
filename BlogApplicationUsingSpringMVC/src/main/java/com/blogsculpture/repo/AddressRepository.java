package com.blogsculpture.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.blogsculpture.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer>{

}
