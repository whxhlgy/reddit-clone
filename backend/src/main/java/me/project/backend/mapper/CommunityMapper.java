package me.project.backend.mapper;

import me.project.backend.domain.Community;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CommunityMapper {

    @Select("SELECT * FROM community WHERE id = #{id}")
    Community findById(@Param("id") String id);

}
