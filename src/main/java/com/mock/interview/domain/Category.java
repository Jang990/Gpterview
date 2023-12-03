package com.mock.interview.domain;

import lombok.Getter;

import java.util.List;

@Getter
public enum Category {
    IT("IT", List.of(Field.BACKEND, Field.FRONTEND, Field.NETWORK, Field.EMBEDDED));
    private final String name;
    private final List<Field> fields;

    Category(String name, List<Field> fields) {
        this.name = name;
        this.fields = fields;
    }

    @Getter
    public enum Field {
        BACKEND("백엔드"), FRONTEND("프론트엔드"), NETWORK("네트워크"), EMBEDDED("임베디드");
        private final String name;

        Field(String name) {
            this.name = name;
        }
    }

}