package com.myproject.springboot.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.myproject.springboot.jpa.entity.StudentEntity;

/**
 * Define the repository definition of how to interact with the datasource.
 *
 * This one uses Pagination.  Useful when retrieving lists that not everything if fetched
 *
 * THings to note:
 *
 * * JpaRepository - has both CRUD functionality and Paging capability
 * * JpaRepository<> generic takes (1) the Entity object, (2) the Primary ID data type (check the Entity @Id
 * annotation type)
 */
public interface StudentPagingRepository extends JpaRepository<StudentEntity, Long> {

}
