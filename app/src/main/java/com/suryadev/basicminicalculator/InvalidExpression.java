package com.suryadev.basicminicalculator;

public class InvalidExpression extends Exception {

    @Override
    public String getMessage() {

        return "Invalid Expression";
    }
}
