package com.gft.retailManagerApp.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gft.retailManagerApp.service.ShopService;

/**
 * It allows custom error message to be displayed to the clients and to add more
 * information when an error occurs for debugging purposes
 *
 * @author Vanessa Cantalapiedra
 * @version 1.0
 * @since 20-04-2017
 */

@ControllerAdvice
public class GlobalAdvice {

	private static final Logger logger = LoggerFactory.getLogger(ShopService.class);

	private static final String ERROR_BEGIN = "[###ERROR: ";
	private static final String ERROR_END = "###]";

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public String handleException(IllegalArgumentException ex) {
		return ERROR_BEGIN + ex.getLocalizedMessage() + ERROR_END;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public String handleException(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();
		String message = "Missing required fields:";
		for (FieldError error : fieldErrors) {
			message += " " + error.getField();
		}
		return ERROR_BEGIN + message + ERROR_END;
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	public String handleException(HttpMediaTypeNotSupportedException ex) {
		StringBuilder errorMessage = new StringBuilder(ERROR_BEGIN + ex.getLocalizedMessage());
		errorMessage.append(System.lineSeparator());
		errorMessage.append("Unsupported content type: ");
		errorMessage.append(ex.getContentType());
		errorMessage.append(System.lineSeparator());
		errorMessage.append("Supported content types: ");
		errorMessage.append(MediaType.toString(ex.getSupportedMediaTypes()));
		errorMessage.append(ERROR_END);
		return errorMessage.toString();
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public String handleException(HttpMessageNotReadableException ex) {

		Throwable mostSpecificCause = ex.getMostSpecificCause();
		String errorMessage;
		if (mostSpecificCause != null) {
			String exceptionName = mostSpecificCause.getClass().getName();
			String message = mostSpecificCause.getMessage();
			errorMessage = exceptionName + " -> " + message;
		} else {
			errorMessage = ex.getLocalizedMessage();
		}

		return ERROR_BEGIN + errorMessage + ERROR_END;
	}

	@ExceptionHandler(Throwable.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleException(Throwable ex) {
		logger.error("###############GENERIC ERROR#############", ex);
	}
}
