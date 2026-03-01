package com.dorm.manage.controller;

import com.dorm.manage.common.PageResult;
import com.dorm.manage.common.Result;
import com.dorm.manage.entity.Message;
import com.dorm.manage.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 留言接口
 *
 * @author dorm-manage
 */
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/add")
    public Result<Void> add(@RequestBody Message message, @RequestAttribute Long userId) {
        messageService.add(message, userId);
        return Result.ok();
    }

    @GetMapping("/my/list")
    public Result<List<Message>> myList(@RequestAttribute Long userId) {
        return Result.ok(messageService.myList(userId));
    }

    @PutMapping("/read/{id}")
    public Result<Void> markRead(@PathVariable Long id, @RequestAttribute Long userId) {
        messageService.markRead(id, userId);
        return Result.ok();
    }

    @GetMapping("/admin/page")
    public Result<PageResult<Message>> adminPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Integer status) {
        return Result.ok(messageService.adminPage(pageNum, pageSize, status));
    }

    @PutMapping("/admin/reply")
    public Result<Void> reply(@RequestBody Map<String, Object> body, @RequestAttribute Long userId) {
        Long id = Long.valueOf(body.get("id").toString());
        String replyContent = body.get("replyContent").toString();
        messageService.reply(id, replyContent, userId);
        return Result.ok();
    }
}
