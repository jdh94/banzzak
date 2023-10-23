//package com.banzzac.message.controller;
//
//import com.banzzac.message.document.TestEntity;
//import com.banzzac.message.repository.TestRepository;
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//@Controller
//@AllArgsConstructor
//@RequestMapping(value = "/test")
//public class TestController {
//
//	private final TestRepository testRepository;
//
//	@GetMapping({"","/"})
//	@ResponseBody
//	public String makeData(){
//		TestEntity test = new TestEntity();
//		test.setAge(30);
//		test.setName("donghu");
//		return testRepository.save(test).getName();
//	}
//
//}
