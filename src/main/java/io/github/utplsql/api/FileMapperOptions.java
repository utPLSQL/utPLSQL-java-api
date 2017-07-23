package io.github.utplsql.api;

import java.util.ArrayList;
import java.util.List;

public class FileMapperOptions {

    private String objectOwner;
    private List<KeyValuePair> typeMappings;
    private String regexPattern;
    private int ownerSubExpression;
    private int typeSubExpression;
    private int nameSubExpression;

    public FileMapperOptions() {
        this.typeMappings = new ArrayList<>();
    }

    public String getObjectOwner() {
        return objectOwner;
    }

    public void setObjectOwner(String owner) {
        this.objectOwner = owner;
    }

    public List<KeyValuePair> getTypeMappings() {
        return typeMappings;
    }

    public void setTypeMappings(List<KeyValuePair> typeMappings) {
        this.typeMappings = typeMappings;
    }

    public String getRegexPattern() {
        return regexPattern;
    }

    public void setRegexPattern(String regexPattern) {
        this.regexPattern = regexPattern;
    }

    public int getOwnerSubExpression() {
        return ownerSubExpression;
    }

    public void setOwnerSubExpression(int ownerSubExpression) {
        this.ownerSubExpression = ownerSubExpression;
    }

    public int getTypeSubExpression() {
        return typeSubExpression;
    }

    public void setTypeSubExpression(int typeSubExpression) {
        this.typeSubExpression = typeSubExpression;
    }

    public int getNameSubExpression() {
        return nameSubExpression;
    }

    public void setNameSubExpression(int nameSubExpression) {
        this.nameSubExpression = nameSubExpression;
    }

}
