package com.act.taxaudit.api.advice;

import com.act.taxaudit.domain.exception.DomainException;
import com.act.taxaudit.domain.exception.EngineAdapterException;
import com.act.taxaudit.domain.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ProblemDetail handleDomainException(DomainException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problem.setTitle("Domain Rule Violation");
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("https://api.itas.gov.et/errors/domain-violation"));
        return problem;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Resource Not Found");
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("https://api.itas.gov.et/errors/not-found"));
        return problem;
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ProblemDetail handleOptimisticLockingFailure(ObjectOptimisticLockingFailureException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("Optimistic Locking Conflict");
        problem.setDetail("The resource was modified by another transaction. Please retry.");
        problem.setType(URI.create("https://api.itas.gov.et/errors/conflict"));
        return problem;
    }

    @ExceptionHandler(EngineAdapterException.class)
    public ProblemDetail handleEngineAdapterException(EngineAdapterException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_GATEWAY);
        problem.setTitle("External Service Error");
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("https://api.itas.gov.et/errors/bad-gateway"));
        return problem;
    }
}