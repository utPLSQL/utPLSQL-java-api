package io.github.utplsql.api;

import java.util.List;

public class FileMapperOptions {

    private List<String> filePaths;
    private String objectOwner;
    private List<KeyValuePair> typeMappings;
    private String regexPattern;
    private Integer ownerSubExpression;
    private Integer typeSubExpression;
    private Integer nameSubExpression;

    public FileMapperOptions(List<String> filePaths) {
        this.setFilePaths(filePaths);
    }

    public List<String> getFilePaths() {
        return filePaths;
    }

    public void setFilePaths(List<String> filePaths) {
        this.filePaths = filePaths;
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

    public Integer getOwnerSubExpression() {
        return ownerSubExpression;
    }

    public void setOwnerSubExpression(Integer ownerSubExpression) {
        this.ownerSubExpression = ownerSubExpression;
    }

    public Integer getTypeSubExpression() {
        return typeSubExpression;
    }

    public void setTypeSubExpression(Integer typeSubExpression) {
        this.typeSubExpression = typeSubExpression;
    }

    public Integer getNameSubExpression() {
        return nameSubExpression;
    }

    public void setNameSubExpression(Integer nameSubExpression) {
        this.nameSubExpression = nameSubExpression;
    }

}
