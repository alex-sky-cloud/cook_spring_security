package com.example.model;

import java.io.Serial;
import java.io.Serializable;

public record Foo(Long id, String name) implements Serializable {

    @Serial
    private static final long serialVersionUID = -5422285893276747592L;

}
