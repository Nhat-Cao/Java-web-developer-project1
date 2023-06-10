package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface FileMapper {

    @Results({
            @Result(property = "fileData", column = "filedata", jdbcType = JdbcType.BLOB, javaType = byte[].class)
    })
    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> getFiles(Integer userId);


    @Results({
            @Result(property = "fileData", column = "filedata", jdbcType = JdbcType.BLOB, javaType = byte[].class)
    })
    @Select("SELECT * FROM FILES WHERE fileid = #{fileId}")
    File getFile(Integer fileId);

    @Results({
            @Result(property = "fileData", column = "filedata", jdbcType = JdbcType.BLOB, javaType = byte[].class)
    })
    @Select("SELECT * FROM FILES WHERE filename = #{fileName} and userid = #{userId}")
    File getFileByNameAndUserId(String fileName, Integer userId);

    @Insert("INSERT INTO FILES (fileName, contentType, fileSize, userid, filedata) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData, jdbcType=BLOB})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    Integer insert(File file);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    Integer delete(Integer fileId);
}
