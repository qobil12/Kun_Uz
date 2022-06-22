package com.company.exps;

public class NoPermissionException extends RuntimeException {
    public NoPermissionException(String massage) {
        super(massage);
    }
}
