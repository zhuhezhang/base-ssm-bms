package pers.zhz.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.zhz.pojo.Books;
import pers.zhz.service.BookService;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    @Qualifier("BookServiceImpl")//自动装配把autowire = “byName”
    private BookService bookService;

    // 查询全部书籍
    @RequestMapping("/allBook")
    public String allBook(Model model) {
        List<Books> list = bookService.queryAllBook();
        model.addAttribute("list", list);
        return "allBook";
    }

    // 跳转到添加书籍
    @RequestMapping("/toAddBook")
    public String toAddPaper() {
        return "addBook";
    }

    // 添加书籍后跳转回查询全部书籍界面
    @RequestMapping("/addBook")
    public String addBook(Books books) {
        System.out.println("the new book: " + books);
        bookService.addBook(books);
        return "redirect:/book/allBook";
    }

    // 跳转到更新书籍界面
    @RequestMapping("/toUpdateBook")
    public String toUpdateBook(Model model, int id) {
        Books books = bookService.queryBookById(id);
        model.addAttribute("book", books);
        return "updateBook";
    }

    // 更新书籍后跳转回查询全部书籍界面
    @RequestMapping("/updateBook")
    public String updateBook(Model model, Books books) {
        System.out.println("book before update: " + books);
        bookService.updateBook(books);
        books = bookService.queryBookById(books.getBookID());
        System.out.println("book after update: " + books);
        return "redirect:/book/allBook";
    }

    // 删除书籍
    @RequestMapping("/del/{bookId}")
    public String deleteBook(@PathVariable("bookId") int bookId) {
        bookService.deleteBookById(bookId);
        System.out.println("the bookId that have deleted: " + bookId);
        return "redirect:/book/allBook";
    }

    // 根据书名查询书籍
    @RequestMapping("/queryBook")
    public String queryBookByName(String bookName, Model model) {
        System.out.println("query book by bookName :" + bookName);
        List<Books> list = new ArrayList<>();
        list.addAll(bookService.queryBookByName(bookName));
        if (list.isEmpty()) {
            model.addAttribute("error", "not found");
        }
        model.addAttribute("list", list);
        return "allBook";
    }

}
