package com.ddc.chat.controller;

import com.ddc.chat.controller.request.CreateChatRequest;
import com.ddc.chat.controller.request.UpdateChatRequest;
import com.ddc.chat.controller.response.ChatResponse;
import com.ddc.chat.util.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ddc.chat.service.ChatService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chats")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody CreateChatRequest request) {
        final Long id = chatService.create(request);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @GetMapping("/search/byName/{name}")
    public ResponseEntity<ChatResponse> findByName(@PathVariable String name) {
        final ChatResponse chatResponse = chatService.findByName(name);
        return new ResponseEntity<>(chatResponse, HttpStatus.OK); //todo: check users
    }

    @GetMapping("/search/byUserId/{userId}")
    public ResponseEntity<List<ChatResponse>> findAllByUserId(@PathVariable("userId") Long userId) {
        final List<ChatResponse> all = chatService.findAllByUserId(userId);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @GetMapping("/search/byUserId/private/{userId}")
    public ResponseEntity<List<ChatResponse>> findAllPrivateByUserId(@PathVariable("userId") Long userId) {
        final List<ChatResponse> all = chatService.findAllPrivateByUserId(userId);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @GetMapping("/search/byUserId/public/{userId}")
    public ResponseEntity<List<ChatResponse>> findAllPublicByUserId(@PathVariable("userId") Long userId) {
        final List<ChatResponse> all = chatService.findAllPublicByUserId(userId);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatResponse> findById(@PathVariable Long id) {
        final ChatResponse chat = chatService.findById(id);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Pagination<ChatResponse>> findAll(Pageable pageable) {
        final Page<ChatResponse> all = chatService.findAll(pageable);
        return new ResponseEntity<>(new Pagination<>(all), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id,
                                       @RequestBody UpdateChatRequest request) {
        chatService.update(request, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        chatService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
