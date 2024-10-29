package com.example.samuraitravel.service;
 
 import org.springframework.stereotype.Service;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewRegisterForm;
import com.example.samuraitravel.repository.ReviewRepository;

import jakarta.transaction.Transactional;
 
 @Service
 public class ReviewService {
     private final ReviewRepository reviewRepository;        
     
     public ReviewService(ReviewRepository reviewRepository) {        
         this.reviewRepository = reviewRepository;        
     }     
     
     @Transactional
     public void create(ReviewRegisterForm reviewRegisterForm) {
    	 Review review = new Review();
    	
    	 review.setScore(reviewRegisterForm.getScore());
    	 review.setContent(reviewRegisterForm.getContent());
    	 
    	 reviewRepository.save(review);
     }
     
     public boolean hasUserAlreadyReviewed(House house, User user) {
         return reviewRepository.findByHouseAndUser(house, user) != null;
     }
 }