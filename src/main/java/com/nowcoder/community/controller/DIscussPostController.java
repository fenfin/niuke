package com.nowcoder.community.controller;

import com.nowcoder.community.event.EventProducer;
import com.nowcoder.community.entity.*;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @Desc:描述
 * @Author：yyf
 * @Filename：DIscussPostController
 * @Date：2023/12/9 15:34
 */
@Controller
@RequestMapping("/discuss")
public class DIscussPostController implements CommunityConstant{

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content){
        User user = hostHolder.getUser();
        if(user == null){
            return CommunityUtil.getJSONString(403,"你还没有登录");
        }
        DiscussPost post =  new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);

        //触发发帖事件
        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(user.getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(post.getId());
        eventProducer.fireEvent(event);
        return CommunityUtil.getJSONString(0,"发布成功");
    }

    @RequestMapping(path = "/detail/{discussPostId}",method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int  discussPostId, Model model, Page page){
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", post);
        //作者
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);

        //点赞
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeCount",likeCount);


        int likeStatus = hostHolder.getUser() == null?0:likeService.findEntityLikeStatus(hostHolder.getUser().getId(),ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeStatus",likeStatus);

        //评论分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/"+ discussPostId);
        page.setRows(post.getCommentCount());

        /**
         * 评论：给帖子的评论
         * 回复：给评论的评论
         * 评论列表
         */
        List<Comment> commentList = commentService.findCommentsByEntity(ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if (commentList != null){
            for (Comment comment : commentList) {
                //评论VO
                Map<String, Object> commentVo = new HashMap<>();
                //评论
                commentVo.put("comment",comment);
                //作者
                commentVo.put("user",userService.findUserById(comment.getUserId()));

                //点赞
                likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeCount",likeCount);

                likeStatus = hostHolder.getUser() == null?0:likeService.findEntityLikeStatus(hostHolder.getUser().getId(),ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeStatus",likeStatus);

                //回复列表
                List<Comment> replyList = commentService.findCommentsByEntity(
                        CommunityConstant.ENTITY_TYPE_COMMENT, comment.getEntityId(), 0, Integer.MAX_VALUE);

                //回复VO列表
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if(replyList!=null){
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVO = new HashMap<>();
                        replyVO.put("reply", reply);
                        replyVO.put("user",userService.findUserById(reply.getUserId()));

                        //回复目标
                        User target = reply.getTargetId() == 0?null:userService.findUserById(reply.getTargetId());
                        replyVO.put("target", target);

                        //点赞
                        likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId());
                        replyVO.put("likeCount",likeCount);

                        likeStatus = hostHolder.getUser() == null?0:likeService.findEntityLikeStatus(hostHolder.getUser().getId(),ENTITY_TYPE_COMMENT, reply.getId());
                        replyVO.put("likeStatus",likeStatus);


                        replyVoList.add(replyVO);
                    }
                }
                commentVo.put("replys",replyVoList);

                //回复数量
                int replyCount = commentService.findCommentCount(CommunityConstant.ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount",replyCount);

                commentVoList.add(commentVo);
            }
        }

        model.addAttribute("comments", commentVoList);
        return "/site/discuss-detail";
    }
}
