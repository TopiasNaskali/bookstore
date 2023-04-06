package com.example.bookstore.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.bookstore.domain.Book;
import com.example.bookstore.domain.BookRepository;
import com.example.bookstore.domain.CategoryRepository;

@CrossOrigin
@Controller
public class BookstoreController {

	@GetMapping("/index")
	public String index() {
		return "welcome";
	}

	@RequestMapping(value = "/login")
	public String login() {
		return "login";
	}

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@RequestMapping(value = "/booklist")
	public String bookList(Model model) {
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		model.addAttribute("name", username);
		model.addAttribute("books", bookRepository.findAll());
		return "booklist";
	}

	@GetMapping("/books")
	public @ResponseBody List<Book> getBooks() {
		return (List<Book>) bookRepository.findAll();
	}

	@GetMapping("/books/{id}")
	public @ResponseBody Optional<Book> getOneBook(@PathVariable("id") Long bookId) {
		return bookRepository.findById(bookId);
	}

	@PostMapping("/books")
	public @ResponseBody Book addNewBook(@RequestBody Book book) {
		return bookRepository.save(book);
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addBook(Model model) {
		model.addAttribute("book", new Book());
		model.addAttribute("categories", categoryRepository.findAll());
		return "addbook";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Book book) {
		bookRepository.save(book);
		return "redirect:booklist";
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String deleteBook(@PathVariable("id") Long bookId, Model model) {
		bookRepository.deleteById(bookId);
		return "redirect:../booklist";
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editBook(@PathVariable("id") Long bookId, Model model) {
		model.addAttribute("book", bookRepository.findById(bookId));
		model.addAttribute("categories", categoryRepository.findAll());
		return "editbook";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Book book) {
		bookRepository.save(book);
		return "redirect:booklist";
	}

}