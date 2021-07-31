package pers.zhz.dao;

import org.apache.ibatis.annotations.Param;
import pers.zhz.pojo.Books;

import java.util.List;

public interface BookMapper {
    //增加一个Book
    int addBook(Books book);

    //根据id删除一个Book
    int deleteBookById(@Param("bookID") int bookID);

    //更新Book
    int updateBook(Books books);

    //根据id查询,返回一个Book
    Books queryBookById(@Param("bookID") int bookID);

    //查询全部Book,返回list集合
    List<Books> queryAllBook();

    // 根据书籍名字查询书籍
    List<Books> queryBookByName(@Param("bookName")String bookName);
}
