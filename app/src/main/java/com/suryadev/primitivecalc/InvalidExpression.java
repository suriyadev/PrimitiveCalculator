package com.suryadev.primitivecalc;

public class InvalidExpression extends Exception {

    @Override
    public String getMessage() {

        return "Invalid Expression";
    }
}
