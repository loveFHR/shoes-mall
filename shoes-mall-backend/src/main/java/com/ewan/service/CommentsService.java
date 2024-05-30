package com.ewan.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ewan.mapper.CommentsMapper;
import com.ewan.model.entity.Comments;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class CommentsService extends ServiceImpl<CommentsMapper, Comments> {
    public void findAllChildCommentId(List<Long> commentsIdList, Collection<Long> commentsParentIdList) {
        if (commentsParentIdList.size() == 0) return;
        List<Comments> commentsList = this.list(new QueryWrapper<Comments>().in(Comments.COL_ANSWER_ID, commentsParentIdList));
        commentsIdList.addAll(commentsList.stream().map(Comments::getId).toList());
        Set<Long> commentAnswerIdSet = commentsList.stream().map(Comments::getId).collect(Collectors.toSet());
        findAllChildCommentId(commentsIdList, commentAnswerIdSet);
    }
}
