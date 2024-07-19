package com.nhnacademy.bookstore.global.elastic.document.book;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDocument {
	private long id;
	private String title;
	private String author;
	private String thumbnail;
	private String publisher;
	int price;
	int sellingPrice;
	private List<String> tagList;
	private List<String> categoryList;
	private List<String> keywordText;
	private List<String> keywordList;

}