package com.ddc.chat.controller;

import com.ddc.chat.controller.request.UpdateMessageRequest;
import com.ddc.chat.controller.response.MessageResponse;
import com.ddc.chat.service.MessageService;
import com.ddc.chat.util.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/messages")
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/{chatId}")
    public ResponseEntity<Pagination<MessageResponse>> findAllByChatId(@PathVariable Long chatId,
                                                                 Pageable pageable) {
        final Page<MessageResponse> all = messageService.findAllByChatId(chatId, pageable);
        return new ResponseEntity<>(new Pagination<>(all), HttpStatus.OK);
    }

    @GetMapping("/{chatId}/{sender}")
    public ResponseEntity<Pagination<MessageResponse>> findByUserId(@PathVariable Long chatId,
                                                              @PathVariable String sender,
                                                              Pageable pageable) {
        final Page<MessageResponse> all = messageService.findAllByChatIdAndSender(chatId, sender,
                                                                                                   pageable);
        return new ResponseEntity<>(new Pagination<>(all), HttpStatus.OK);
    }

    // todo: how to send date correctly?
    @GetMapping("/{chatId}/byDate/{date}")
    public ResponseEntity<Pagination<MessageResponse>> findByDate(@PathVariable Long chatId,
                                                            @PathVariable String date,
                                                            Pageable pageable) {
        final Page<MessageResponse> all = messageService.findAllByChatIdAndDate(chatId, date, pageable);
        return new ResponseEntity<>(new Pagination<>(all), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id,
                                             @RequestBody UpdateMessageRequest request) {
        messageService.update(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        messageService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
