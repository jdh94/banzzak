package com.banzzac.message.controller;

import com.banzzac.message.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

//인증이 안된 사용자들이 출입할 수 있는 경로를 /auth/** 허용
//그냥 주소가 /이면 intex.jsp 허용
//static이하에 있는 /js/**, /css/**, /image/**

@RestController
@RequestMapping(value = "/boardmng")
public class MessageController {
	@Autowired
	private ChatMessageService chatMessageService;

	//	@AuthenticationPrincipal PrincipalDetail principal
	@GetMapping({"","/"})
	public ModelAndView BoardPage() throws Exception {
		return new ModelAndView("/test.jsp").addObject("test", "test");
	}

}
