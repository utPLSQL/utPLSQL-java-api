package io.github.utplsql.api;

import java.util.ArrayList;
import java.util.List;

public class FileMapperOptions {

    private String owner;
    private List<KeyValuePair> typeMappings;
    private String regexPattern;
    private int ownerSubExpression;
    private int typeSubExpression;
    private int nameSubExpression;

    public FileMapperOptions(List<String> filePaths) {
        this.typeMappings = new ArrayList<>();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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
