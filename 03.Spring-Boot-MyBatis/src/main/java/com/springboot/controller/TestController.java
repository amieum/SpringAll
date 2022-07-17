package com.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.springboot.bean.Student;
import com.springboot.service.StudentService;

@RestController
public class TestController {

	@Autowired
	private StudentService studentService;
	
	@GetMapping( value = "/querystudent/{sno}")
	public Student queryStudentBySno(@PathVariable("sno") String sno) {
		return this.studentService.queryStudentBySno(sno);
	}
}
