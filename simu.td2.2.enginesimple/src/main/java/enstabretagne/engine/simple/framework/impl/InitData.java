package enstabretagne.engine.simple.framework.impl;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

public abstract class InitData {

    private String name;

    @JsonbCreator
    public InitData(@JsonbProperty(value = "name") String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }

}
