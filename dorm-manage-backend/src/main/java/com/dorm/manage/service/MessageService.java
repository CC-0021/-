package com.dorm.manage.service;

import com.dorm.manage.common.PageResult;
import com.dorm.manage.entity.Message;
import com.dorm.manage.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 留言服务
 *
 * @author dorm-manage
 */
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageMapper messageMapper;

    public void add(Message message, Long studentId) {
        message.setStudentId(studentId);
        message.setStatus(0);
        messageMapper.insert(message);
    }

    public List<Message> myList(Long studentId) {
        return messageMapper.selectByStudentId(studentId);
    }

    public void markRead(Long id, Long studentId) {
        Message m = messageMapper.selectById(id);
        if (m != null && m.getStudentId().equals(studentId)) {
            m.setIsRead(1);
            messageMapper.updateById(m);
        }
    }

    public PageResult<Message> adminPage(int pageNum, int pageSize, Integer status) {
        long offset = (long) (pageNum - 1) * pageSize;
        List<Message> list = messageMapper.selectPage(status, offset, pageSize);
        long total = messageMapper.countPage(status);
        return new PageResult<>(total, list);
    }

    public void reply(Long id, String replyContent, Long replyUserId) {
        Message m = messageMapper.selectById(id);
        if (m == null) {
            return;
        }
        m.setStatus(1);
        m.setReplyContent(replyContent);
        m.setReplyUserId(replyUserId);
        m.setReplyTime(LocalDateTime.now());
        messageMapper.updateById(m);
    }
}
