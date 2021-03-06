package com.example.api.controller;

import com.example.api.domain.Book;
import com.example.api.dto.BookDTO;
import com.example.api.dto.ValidationFirst;
import com.example.api.exception.InvalidRequestException;
import com.example.api.exception.NotFoundException;
import com.example.api.service.BookService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class BookController {

  @Autowired
  private BookService bookService;

  /**
   * 獲取書單列表
   */
  @GetMapping("/books")
  public ResponseEntity<?> listAllBooks() {
    List<Book> books = bookService.findAllBooks();
    if (CollectionUtils.isEmpty(books)) {
      throw new NotFoundException("Books Not Found");
    }
    return new ResponseEntity<>(books, HttpStatus.OK);
  }

  /**
   * 獲取一個書單
   */
  @GetMapping("/books/{id}")
  public ResponseEntity<?> getBook(@PathVariable long id) {
    Book book = bookService.getBookById(id);
    return new ResponseEntity<>(book, HttpStatus.OK);
  }

  /**
   * 新增一個書單
   */
  @PostMapping("/books")
  public ResponseEntity<?> saveBook(@Validated({ValidationFirst.class}) @RequestBody BookDTO bookDTO,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new InvalidRequestException("Invalid parameter", bindingResult);
    }
    Book book1 = bookService.saveBook(bookDTO.convertToBook());
    return new ResponseEntity<>(book1, HttpStatus.CREATED);
  }

  /**
   * 更新一個書單
   */
  @PutMapping("/books/{id}")
  public ResponseEntity<?> updateBook(@PathVariable long id, @Valid @RequestBody BookDTO bookDTO) {
//		if (bindingResult.hasErrors()) {
//			throw new InvalidRequestException("Invalid parameter", bindingResult);
//		}
    Book book = bookService.updateBook(id, bookDTO);
    return new ResponseEntity<>(book, HttpStatus.OK);
  }

  /**
   * 刪除一個書單
   */
  @DeleteMapping("/books/{id}")
  public ResponseEntity<?> deleteBook(@PathVariable long id) {
    bookService.deleteBookById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  /**
   * 刪除所有書單
   */
  @DeleteMapping("/books")
  public ResponseEntity<?> deleteAllBooks() {
    bookService.deleteAllBooks();
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
