package controller;

import controller.request.CreateChatRequest;
import controller.request.UpdateChatRequest;
import controller.response.ChatResponse;
import lombok.RequiredArgsConstructor;
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
import service.ChatService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody CreateChatRequest request) {
        final Long id = chatService.create(request);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ChatResponse>> findAllByUserId(@PathVariable Long userId) {
        final List<ChatResponse> all = chatService.findAllByUserId(userId);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id,
                                       @RequestBody UpdateChatRequest request) {
        chatService.update(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        chatService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
