package com.algaworks.algafood.api.exceptionhandler;

import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		Throwable rootCause = ExceptionUtils.getRootCause(ex);
		
		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidFormatException((InvalidFormatException)rootCause, new HttpHeaders(), status, request);
		}
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		Problem problem = createProblemBuilder(status, problemType, ex.getMessage()).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException e,
			WebRequest request) {

		HttpStatus status = HttpStatus.NOT_FOUND;
		ProblemType problemType = ProblemType.ENTIDADE_NAO_ENCONTRADA;

		Problem problem = createProblemBuilder(status, problemType, "O corpo da requisição está inválido. Verifique erro de sintaxe.").build();

		return handleExceptionInternal(e, problem, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException e, WebRequest request) {

		HttpStatus status = HttpStatus.CONFLICT;
		ProblemType problemType = ProblemType.ENTIDADE_EM_USO;

		Problem problem = createProblemBuilder(status, problemType, e.getMessage()).build();

		return handleExceptionInternal(e, problem, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		if (!Objects.nonNull(body)) {
			body = Problem.builder().title(status.getReasonPhrase()).status(status.value()).build();
		} else if (body instanceof String) {
			body = Problem.builder().title((String) body).status(status.value()).build();
		}
		return super.handleExceptionInternal(e, body, headers, status, request);
	}
	
	private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail) {

		return Problem.builder().status(status.value()).type(problemType.getUri()).title(problemType.getTitle())
				.detail(detail);
	}
	
	private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		String path = ex.getPath().stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));

		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = String.format(
				"A propriedade '%s' recebeu o valor '%s', que é de um tipo inválido. Corrija e informe um valor compatível com o tipo '%s'.",
				path, ex.getValue(), ex.getTargetType().getSimpleName());

		Problem problem = createProblemBuilder(status, problemType, detail).build();
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
}
