package com.example;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class NotFoundException extends RuntimeException {

	public NotFoundException(String empName) {
		super("could not find '" + empName + "'.");
	}
	
	public NotFoundException(Long id) {
		super("could not find " + id + ".");
	}
}
