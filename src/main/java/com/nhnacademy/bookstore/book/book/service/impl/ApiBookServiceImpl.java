package com.nhnacademy.bookstore.book.book.service.impl;

import com.nhnacademy.bookstore.book.book.dto.response.ApiCreateBookResponse;
import com.nhnacademy.bookstore.book.book.dto.response.ImageMultipartFile;
import com.nhnacademy.bookstore.book.book.repository.ApiBookRepository;
import com.nhnacademy.bookstore.book.book.repository.BookRepository;
import com.nhnacademy.bookstore.book.book.service.ApiBookService;
import com.nhnacademy.bookstore.book.bookCartegory.repository.BookCategoryRepository;
import com.nhnacademy.bookstore.book.bookImage.repository.BookImageRepository;
import com.nhnacademy.bookstore.book.category.exception.CategoryNotFoundException;
import com.nhnacademy.bookstore.book.category.repository.CategoryRepository;
import com.nhnacademy.bookstore.book.image.exception.MultipartFileException;
import com.nhnacademy.bookstore.book.image.imageService.ImageService;
import com.nhnacademy.bookstore.entity.book.Book;
import com.nhnacademy.bookstore.entity.bookCategory.BookCategory;
import com.nhnacademy.bookstore.entity.bookImage.BookImage;
import com.nhnacademy.bookstore.entity.bookImage.enums.BookImageType;
import com.nhnacademy.bookstore.entity.category.Category;
import com.nhnacademy.bookstore.entity.totalImage.TotalImage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author 한민기
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApiBookServiceImpl implements ApiBookService {

    @Autowired
    private ApiBookRepository apiBookRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BookCategoryRepository bookCategoryRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private BookImageRepository bookImageRepository;

    /**
     * Api 로 받아온 책을 저장하는 코드
     *
     * @param isbnId 저장할 책의 isbn13
     */
    @Transactional
    @Override
    public void save(String isbnId) {
        ApiCreateBookResponse bookResponse = apiBookRepository.getBookResponse(isbnId);

        Book book = new Book(
                bookResponse.title().substring(bookResponse.title().indexOf("-") + 2),
                bookResponse.item().getFirst().description(),
                stringToZonedDateTime(bookResponse.pubDate()),
                bookResponse.item().getFirst().priceSales(),
                100,
                bookResponse.item().getFirst().priceSales(),
                0,
                true,
                realAuthorName(bookResponse.item().getFirst().author()),
                bookResponse.item().getFirst().isbn13(),
                bookResponse.item().getFirst().publisher(),
                null,
                null,
                null
        );
        book = bookRepository.save(book);

        List<String> categories = categoryNameStringToList(bookResponse.item().getFirst().categoryName());

        for (String categoryName : categories) {
            Optional<Category> category = categoryRepository.findByName(categoryName);

            if (category.isEmpty()) {
                throw new CategoryNotFoundException(categoryName);
            }
            BookCategory bookCategory = BookCategory.create(book, category.get());

            bookCategoryRepository.save(bookCategory);
        }

        String fileName = imageService.createImage(downloadImageAsMultipartFile(bookResponse.item().getFirst().cover()),
                "book");
        TotalImage totalImage = new TotalImage(fileName);
        BookImage bookImage = new BookImage(BookImageType.MAIN, book, totalImage);
        bookImageRepository.save(bookImage);

    }

    /**
     * @param author Api 에서 받아오는 작가 이름 => 지음 엮은이등이 포함되어있어서 이름만 받도록 수정
     * @return 작가의 이름
     */
    public String realAuthorName(String author) {
        if (author.contains("지음")) {
            return author.substring(0, author.indexOf("지음"));
        } else if (author.contains("엮은이")) {
            return author.substring(0, author.indexOf("엮은이"));
        }
        if (author.contains("옮김")) {
            return author.substring(0, author.indexOf("옮김"));
        }
        return author;
    }

    /**
     * imageUrl 에서 이미지 파일을 MultipartFile로 가져옴
     *
     * @param imageUrl api 에서 받은 cover 이미지의 사진
     * @return multipartFile 형식의 이미지  -> 이걸 나중에 image 등록
     */
    public MultipartFile downloadImageAsMultipartFile(String imageUrl) {
        // 1. URL에서 이미지 다운로드
        URL url = null;
        try {
            url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            // 2. 이미지를 바이트 배열로 변환
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (InputStream is = connection.getInputStream()) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
            }

            // 3. 바이트 배열을 MultipartFile로 변환
            byte[] imageBytes = baos.toByteArray();

            return new ImageMultipartFile(imageBytes);
        } catch (IOException e) {
            throw new MultipartFileException();
        }

    }

    /**
     * String -> ZoneDateTime 으로 변경
     *
     * @param dateStr 바꿀 date String
     * @return ZoneDateTime 의 날짜
     */
    public ZonedDateTime stringToZonedDateTime(String dateStr) {
        if (Objects.isNull(dateStr)) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
        LocalDate localDateStr = LocalDate.parse(dateStr, formatter);

        return localDateStr.atStartOfDay(ZoneId.systemDefault());
    }

    /**
     * 하나의 카테고리를 나눠서 넣기
     * ex) 국내도서>사회과학>비평/칼럼>정치비평/칼럼
     *
     * @param categoryName 하나로 길게 되어 있는 카테고리 이름
     * @return List 로 나눠진 카테고리
     */
    public List<String> categoryNameStringToList(String categoryName) {
        String[] categoryNameArray = categoryName.split(">");
        return new ArrayList<>(Arrays.asList(categoryNameArray));
    }

}
