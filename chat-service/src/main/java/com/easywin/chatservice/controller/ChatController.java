package com.easywin.chatservice.controller;

import com.easywin.chatservice.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message getMessage(@Payload Message message){
        return message;
    }
}
