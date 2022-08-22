package com.demo.spring.service;



import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.demo.spring.model.ImageModel;

import com.demo.spring.repository.ImageRepository;

@Service
@Transactional
public class ImageService {
	
	@Autowired
	ImageRepository imageRepository;
	 
	public ImageModel save(ImageModel img) {
		
	
		return imageRepository.save(img);
	}
}
