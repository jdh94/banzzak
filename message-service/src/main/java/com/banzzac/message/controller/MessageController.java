package com.banzzac.message.controller;

import com.banzzac.message.document.Message;
import com.banzzac.message.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Controller
@AllArgsConstructor
@RequestMapping(value = "/message")
public class MessageController {

	@Autowired
	private MessageService messageService;

	@GetMapping({"","/"})
	@ResponseBody
	public Mono<Message> messageTest(){
		// 테스트성공
		//return messageService.saveMessage();

		// 테스트 성공
		return messageService.messageInsertService();
	}

}
