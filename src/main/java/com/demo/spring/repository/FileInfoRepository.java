package com.demo.spring.repository;



import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.spring.model.FileInfo;

@Repository
public interface FileInfoRepository extends CrudRepository<FileInfo, Long> {

	

	

}
