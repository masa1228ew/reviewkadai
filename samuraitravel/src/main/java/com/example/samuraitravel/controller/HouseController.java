package com.example.samuraitravel.controller;

 import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReservationInputForm;
import com.example.samuraitravel.form.ReviewRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.ReviewService;
 
 @Controller
 @RequestMapping("/houses")
public class HouseController {
     private final HouseRepository houseRepository;        
     private final ReviewRepository reviewRepository;
     private final ReviewService reviewService;
     
     public HouseController(HouseRepository houseRepository,ReviewRepository reviewRepository,ReviewService reviewService) {
         this.houseRepository = houseRepository;            
         this.reviewRepository = reviewRepository;
         this.reviewService = reviewService;
     }     
   
     @GetMapping
     public String index(@RequestParam(name = "keyword", required = false) String keyword,
                         @RequestParam(name = "area", required = false) String area,
                         @RequestParam(name = "price", required = false) Integer price, 
                         @RequestParam(name = "order", required = false) String order,
                         @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
                         Model model) 
     {
         Page<House> housePage;
                 
         if (keyword != null && !keyword.isEmpty()) {
        	 if (order != null && order.equals("priceAsc")) {
                 housePage = houseRepository.findByNameLikeOrAddressLikeOrderByPriceAsc("%" + keyword + "%", "%" + keyword + "%", pageable);
             } else {
                 housePage = houseRepository.findByNameLikeOrAddressLikeOrderByCreatedAtDesc("%" + keyword + "%", "%" + keyword + "%", pageable);
             }            
         } else if (area != null && !area.isEmpty()) {
        	 if (order != null && order.equals("priceAsc")) {
                 housePage = houseRepository.findByAddressLikeOrderByPriceAsc("%" + area + "%", pageable);
             } else {
                 housePage = houseRepository.findByAddressLikeOrderByCreatedAtDesc("%" + area + "%", pageable);
             }     
         } else if (price != null) {
        	 if (order != null && order.equals("priceAsc")) {
                 housePage = houseRepository.findByPriceLessThanEqualOrderByPriceAsc(price, pageable);
             } else {
                 housePage = houseRepository.findByPriceLessThanEqualOrderByCreatedAtDesc(price, pageable);
             }            
         } else {
        	  if (order != null && order.equals("priceAsc")) {
                  housePage = houseRepository.findAllByOrderByPriceAsc(pageable);
              } else {
                  housePage = houseRepository.findAllByOrderByCreatedAtDesc(pageable);   
              }       
         }                
         
         model.addAttribute("housePage", housePage);
         model.addAttribute("keyword", keyword);
         model.addAttribute("area", area);
         model.addAttribute("price", price);                              
         model.addAttribute("order", order);
         return "houses/index";
     }
     @GetMapping("/{id}")
     public String show(@PathVariable(name = "id") Integer id, Model model, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
         House house = houseRepository.getReferenceById(id);
         boolean hasUserAlreadyReviewed = false;
         
         if(userDetailsImpl!=null) {
        	 User user = userDetailsImpl.getUser();
        	 hasUserAlreadyReviewed = reviewService.hasUserAlreadyReviewed(house,user);
         }
         List<Review> newReviews =reviewRepository.findTop6ByHouseOrderByCreatedAtDesc(house);
         long totalReviewCount = reviewRepository.countByHouse(house);  
         
         model.addAttribute("house", house);         
         model.addAttribute("reservationInputForm", new ReservationInputForm());
         model.addAttribute("hasUserAlreadyReviewed", hasUserAlreadyReviewed);
         model.addAttribute("newReviews",newReviews);
         model.addAttribute("totalReviewCount",totalReviewCount);
         
         
         return "houses/show";
     }    
     
    
     
     @GetMapping("/{id}/review/register")
     public String register(@PathVariable(name = "id") Integer id, Model model) {
         House house = houseRepository.getReferenceById(id);
         model.addAttribute("house", house); 
    	 model.addAttribute("reviewRegisterForm", new ReviewRegisterForm());
    	 return "review/register";
    	
     }
     
     @GetMapping("/{id}/review/index")
     public String index(@PathVariable(name = "id") Integer id,Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
//    	 Page<Review> reviewPage = reviewRepository.findAll(pageable);
    	 
    	 
//    	 House house = houseRepository.getReferenceById(id);
//    	 List<Review> reviewPage = reviewRepository.findByHouse(house);
    	  
//    	 model.addAttribute("reviewPage",reviewPage);
//    	 model.addAttribute(house);
    	
    	 House house = houseRepository.getReferenceById(id);
         boolean hasUserAlreadyReviewed = false;
         
         if(userDetailsImpl!=null) {
        	 User user = userDetailsImpl.getUser();
        	 hasUserAlreadyReviewed = reviewService.hasUserAlreadyReviewed(house,user);
         }
//         List<Review> newReviews =reviewRepository.findTop6ByHouseOrderByCreatedAtDesc(house);
         List<Review> reviewList = reviewRepository.findAllByHouseOrderByCreatedAtDesc(house);
         Page<Review> reviewPage = reviewRepository.findAllByHouseOrderByCreatedAtDesc(house,pageable);
         long totalReviewCount = reviewRepository.countByHouse(house);  
         
         
         model.addAttribute("house", house);         
         model.addAttribute("reservationInputForm", new ReservationInputForm());
         model.addAttribute("hasUserAlreadyReviewed", hasUserAlreadyReviewed);
         model.addAttribute("reviewList",reviewList);
         model.addAttribute("reviewPage",reviewPage);
         model.addAttribute("totalReviewCount",totalReviewCount);
    	 
    	 return "/review/index";
    	 
     }
    
     
//     @PostMapping("/{id}/review/create")
//     public String create(@PathVariable(name = "id") Integer id,Model model,@ModelAttribute @Validated ReviewRegisterForm reviewRegisterForm,BindingResult bindingResult,RedirectAttributes redirectAttributes ) {
//   	  if(bindingResult.hasErrors()) {
//   		  return "review/register";
//   	  }
//   	 House house = houseRepository.getReferenceById(id);
   	 
//   	  model.addAttribute("house", house); 
//   	  reviewService.create(reviewRegisterForm);
//   	  redirectAttributes.addAttribute("successMessage","レビューを投稿しました。");
   	  
//   	  return "redirect:/houses/show";
//     }
     
//     @PostMapping("{id}/create")
// 	public String create(@PathVariable(name = "id") Integer id, @ModelAttribute @Validated ReviewRegisterForm reviewRegisterForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
// 		if(bindingResult.hasErrors()) {
 			
// 			return "review/reviewRegister";
// 		}
 		
// 		reviewService.create(reviewRegisterForm, userDetailsImpl);
// 		redirectAttributes.addFlashAttribute("successMessage", "レビューを登録しました。");
 		
// 		return "redirect:/houses/show";
// 	}

//     @GetMapping("/houses/{houseId}/review/{reviewId}/edit")
//     public String reviewedit(@PathVariable("houseId") Integer houseId, 
//                              @PathVariable("reviewId") Integer reviewId, 
//                              Model model) {
//         House house = houseRepository.getReferenceById(houseId);
//         Review review = reviewRepository.getReferenceById(reviewId);
//    	 ReviewEditForm reviewEditForm = new ReviewEditForm(review.getScore(),review.getContent());
//    	 model.addAttribute("house", house);
//    	 model.addAttribute("reviewEditForm",reviewEditForm);
    	 
    	 
//    	 return "houses/review/edit";
//   }
     
//     @PostMapping("/houses/{houseId}/review/{reviewid}/delete")
//     public String delete(@PathVariable(name="houseId") Integer houseId,@PathVariable(name="reviewId") Integer reviewId,RedirectAttributes redirectAttributes) {
//    	 House house = houseRepository.getReferenceById(houseId);
//    	 reviewRepository.deleteById(reviewId);
    	 
//    	  redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");
    	  
//    	  return "redirect:/houses/show";
//     }
     
//     @PostMapping("houses/{houseId}/review/{reviewId}/delete")
//     public String delete(@PathVariable(name="id") Integer id,RedirectAttributes redirectAttributes) {
    	 
//    	 reviewRepository.deleteById(id);
    	 
//    	  redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");
    	  
//    	  return "redirect:/houses/show";
//     }
}