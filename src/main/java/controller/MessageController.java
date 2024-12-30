package controller;

import controller.request.UpdateMessageRequest;
import controller.response.ChatMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.ChatMessageService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/message")
public class MessageController {

    private final ChatMessageService chatMessageService;

    @GetMapping("/{chatId}")
    public ResponseEntity<List<ChatMessageResponse>> findAllByUserId(@PathVariable Long chatId) {
        final List<ChatMessageResponse> all = chatMessageService.findAllByChatId(chatId);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @GetMapping("/{chatId}/{sender}")
    public ResponseEntity<List<ChatMessageResponse>> findByUserId(@PathVariable Long chatId,
                                                                  @PathVariable String sender) {
        final List<ChatMessageResponse> allByChatIdAndSender = chatMessageService.findAllByChatIdAndSender(chatId,
                                                                                                           sender);
        return new ResponseEntity<>(allByChatIdAndSender, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id,
                                             @RequestBody UpdateMessageRequest request) {
        chatMessageService.update(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        chatMessageService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
