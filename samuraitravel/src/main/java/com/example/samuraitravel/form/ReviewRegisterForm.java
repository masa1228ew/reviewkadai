package com.example.samuraitravel.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRegisterForm {
// @NotNull
// private Integer HouseId;
 @NotNull
 private Integer score;
 
 @NotBlank
 private String content;
}
