package com.student.controller;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.student.StudentProperties;
import com.student.core.Student;
import com.student.service.StudentService;

@RequestMapping("/student")
@RestController
public class StudentController {

	@Inject
	private StudentProperties studentProperties;

	@GetMapping("msg")
	public String getMessage(@RequestHeader("user-agent") String userAgent) {
		return studentProperties.getGreeting() + userAgent;
	}

	@Inject
	private StudentService studentService;

	@GetMapping()
	public Collection<Student> getAll() {
		return studentService.getAllStudents();
	}

	@GetMapping("/{id}")
	public Student getStudent(@PathVariable("id") long id) {
		return studentService.get(id);
	}

	@GetMapping(path = "/single", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public Student getSingleStudent(@RequestParam("id") Optional<Long> optional) {
		return studentService.get(optional.orElse(1l));
	}

	@GetMapping("/search/{department}")
	public Collection<Student> getStudentsPerDepartment(@PathVariable("department") String department,
			@RequestParam("name") Optional<String> optional) {
		return studentService.getAllStudentsInDepartment(department, optional.orElse(""));
	}

	@PostMapping
	public ResponseEntity<String> addStudent(@RequestBody Student student) {
		studentService.add(student);
		if (student.getId() > 0) {
			URI uri = URI.create("college/student/" + student.getId());
			return ResponseEntity.accepted().location(uri).build();
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
}
