package com.travelog.board.dao;

import com.travelog.board.dto.BoardDto;
import org.apache.ibatis.annotations.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Mapper
public interface BoardDao {

    // Select ArticleList
    @Select(value = "select * from board order by id")
    @Results(id="list", value={
            @Result(property = "id", column = "id"),
            @Result(property = "writer", column = "writer"),
            @Result(property = "title", column = "title"),
            @Result(property = "content", column = "content"),
            @Result(property = "view_cnt", column = "view_cnt"),
            @Result(property = "reg_data", column = "reg_data"),
    })
    List<BoardDto> listArticle() throws SQLException;

    // Create Article
    @Insert(value = "insert into board (writer, title, content) values (#{writer},#{title},#{content})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int writeArticle(BoardDto boardDto) throws SQLException;

    // Select Article
    @Select(value = "select * from board where id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "writer", column = "writer"),
            @Result(property = "title", column = "title"),
            @Result(property = "content", column = "content"),
            @Result(property = "view_cnt", column = "view_cnt"),
            @Result(property = "reg_data", column = "reg_data"),
    })
    BoardDto getArticle(int articleNo) throws SQLException;

    // Increase view_cnt
    @Update(value = "update board set view_cnt = view_cnt+1 where id = #{id}")
    void updateHit(int articleNo) throws SQLException;
    // Update Article
    @Update(value = "update board set title = #{title}, content = #{content} where id = #{id}")
    void modifyArticle(BoardDto boardDto) throws SQLException;
    // Delete Article
    @Delete(value = "delete from board where id = #{id}")
    void deleteArticle(int articleNo) throws SQLException;
}
