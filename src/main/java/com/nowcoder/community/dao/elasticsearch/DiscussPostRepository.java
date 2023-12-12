package com.nowcoder.community.dao.elasticsearch;

import com.nowcoder.community.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Desc:描述
 * @Author：yyf
 * @Filename：DiscussPostRepository
 * @Date：2023/12/12 15:26
 */
@Repository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> {
    //ElasticsearchRepository<DiscussPost, Integer> 实体类，主键

}
