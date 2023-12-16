package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Desc:描述
 * @Author：yyf
 * @Filename：DiscussPostMapper
 * @Date：2023/12/6 21:19
 */
@Mapper
public interface DiscussPostMapper {

    //查询
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit, int orderMode);

    //分页，需要总条数
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);

    //查询帖子详情
    DiscussPost selectDiscussPostById(int id);

    //更新帖子是数量
    int updateCommentCount(int id, int commentCount);

    //更新类型置顶、加精
    int updateType(int id,int type);

    int updateStatus(int id, int status);

    int updateScore(int id, double score);
}
