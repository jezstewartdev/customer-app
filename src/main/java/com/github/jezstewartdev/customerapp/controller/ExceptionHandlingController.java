package com.github.jezstewartdev.customerapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ExceptionHandlingController {

	@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Requested object not found")
	@ExceptionHandler(ObjectNotFoundException.class)
	public void handleException(ObjectNotFoundException exception, HttpServletResponse response) {

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> notValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
		List<String> errors = new ArrayList<>();

		ex.getAllErrors().forEach(err -> errors.add(err.getDefaultMessage()));

		Map<String, List<String>> result = new HashMap<>();
		result.put("errors", errors);

		return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
	}

}
