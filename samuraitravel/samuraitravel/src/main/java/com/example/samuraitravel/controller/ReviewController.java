package com.example.samuraitravel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewRegisterForm;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.service.ReviewService;

@Controller
@RequestMapping("/review")
public class ReviewController{
	private final ReviewRepository reviewRepository;
	private final ReviewService reviewService;
	
	public ReviewController(ReviewRepository reviewRepository,ReviewService reviewService) {
		this.reviewRepository = reviewRepository;
		this.reviewService = reviewService;
	}
	
//	 @GetMapping("/{houseId}/review/{reviewId}/edit")
//     public String reviewedit(@PathVariable(name="id") Integer id,Model model){
//    	 Review review= reviewRepository.getReferenceById(id);
//    	 ReviewEditForm reviewEditForm = new ReviewEditForm(review.getScore(),review.getContent());
    	 
//    	 model.addAttribute("reviewEditForm",reviewEditForm);
    	 
//    	 return "/houses/review/edit";
//   }
	
//	 @GetMapping("/{id}/houses/review/register")
//     public String register(Model model) {
//    	 model.addAttribute("reviewRegisterForm", new ReviewRegisterForm());
//    	 return "houses/review/register";
//     }
     
     @PostMapping("/{id}/houses/review/create")
     public String create(@ModelAttribute @Validated ReviewRegisterForm reviewRegisterForm,BindingResult bindingResult,RedirectAttributes redirectAttributes ) {
   	  if(bindingResult.hasErrors()) {
   		  return "/{id}/houses/review/register";
   	  }
   	  reviewService.create(reviewRegisterForm);
   	  redirectAttributes.addAttribute("successMessage","レビューを投稿しました。");
   	  
   	  return "redirect:/houses/show";
     }
//	@GetMapping
//	public String show(Model model) {
//		List<Review> newReview = reviewRepository.findTop10ByOrderByCreatedAtDesc(house);
//		model.addAttribute("newReview",newReview);
//		return "house/show";
//	}
	
     @GetMapping("/{reviewId}/edit")
     public String reviewedit( 
             @PathVariable(name="reviewId") Integer reviewId, 
             Model model) {
//House house = houseRepository.getReferenceById(houseId);
//House house = houseRepository.getReferenceById(houseId);
Review review = reviewRepository.getReferenceById(reviewId);
ReviewEditForm reviewEditForm = new ReviewEditForm(review.getScore(),review.getContent());
//model.addAttribute("houseId", house);
model.addAttribute("reviewId",review);
model.addAttribute("reviewEditForm",reviewEditForm);


return "review/edit";
}
}