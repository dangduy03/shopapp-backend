package com.example.shopapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.shopapp.responses.excel.ImportExcelResponse;

import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class FileImportExcelException{
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	  public ResponseEntity<ImportExcelResponse> handleMaxSizeException(MaxUploadSizeExceededException exc) {
	    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
	    		.body(new ImportExcelResponse("File too large!"));
	  }

}
