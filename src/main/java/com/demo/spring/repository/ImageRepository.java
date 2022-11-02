package com.demo.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.spring.model.ImageModel;



public interface ImageRepository extends JpaRepository<ImageModel, Long> {

    Optional<ImageModel> findByName(String name);
    Optional<ImageModel> findById(Long id);
    Optional<ImageModel> findByIduser(Long id);

}