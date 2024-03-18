package com.project.library.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
   private Long  bookId;
   private  String title;
   private String author;
   private String isbn;
   private int printYear;
   private String imageUrl;
   private int quantityAvailable;
   private boolean isActive;

   private Long publisherId;


}
