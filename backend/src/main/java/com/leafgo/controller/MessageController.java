package com.leafgo.controller;

import com.leafgo.entity.Conversation;
import com.leafgo.entity.Message;
import com.leafgo.repository.ConversationRepository;
import com.leafgo.repository.MessageRepository;
import com.leafgo.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;

    @GetMapping("/conversations")
    public ResponseEntity<ApiResponse<List<Conversation>>> getConversations() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("请先登录"));
        }
        List<Conversation> conversations = conversationRepository.findByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(conversations));
    }

    @GetMapping("/conversations/{conversationId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMessages(
            @PathVariable Long conversationId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), size, Sort.by(Sort.Direction.ASC, "createdAt"));
        Page<Message> messagePage = messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId, pageable);
        Map<String, Object> result = new HashMap<>();
        result.put("content", messagePage.getContent());
        result.put("totalElements", messagePage.getTotalElements());
        result.put("totalPages", messagePage.getTotalPages());
        result.put("size", messagePage.getSize());
        result.put("number", messagePage.getNumber() + 1);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Message>> sendMessage(@RequestBody Message message) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("请先登录"));
        }
        message.setSenderId(userId);
        message.setCreatedAt(LocalDateTime.now());
        message.setIsRead(false);

        Conversation conversation = conversationRepository.findByUsers(userId, message.getReceiverId())
                .orElseGet(() -> {
                    Conversation newConv = new Conversation();
                    newConv.setUser1Id(userId);
                    newConv.setUser2Id(message.getReceiverId());
                    return conversationRepository.save(newConv);
                });

        message.setConversationId(conversation.getId());
        Message saved = messageRepository.save(message);

        conversation.setLastMessage(message.getContent());
        conversation.setLastMessageTime(LocalDateTime.now());
        conversationRepository.save(conversation);

        return ResponseEntity.ok(ApiResponse.success("发送成功", saved));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable Long id) {
        if (!messageRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Message msg = messageRepository.findById(id).get();
        msg.setIsRead(true);
        messageRepository.save(msg);
        return ResponseEntity.ok(ApiResponse.success("已读", null));
    }
}
