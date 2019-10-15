package org.tronder.words.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundError extends RuntimeException {

    private static final long serialVersionUID = 1L;

}
