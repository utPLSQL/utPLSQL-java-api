package org.utplsql.api.db;

import oracle.jdbc.OracleConnection;

import java.util.LinkedHashMap;

public class DynamicParameterListBuilder {

    private LinkedHashMap<String, DynamicParameterList.DynamicParameter> params = new LinkedHashMap<>();
    private boolean addIfNullOrEmpty = true;

    private DynamicParameterListBuilder() {

    }

    public DynamicParameterListBuilder onlyAddIfNotEmpty() {
        addIfNullOrEmpty = false;
        return this;
    }

    public DynamicParameterListBuilder add( String identifier, String value ) {
        params.put(identifier, new DynamicParameterList.DynamicStringParameter(value));
        return this;
    }
    public DynamicParameterListBuilder add( String identifier, Integer value ) {
        params.put(identifier, new DynamicParameterList.DynamicIntegerParameter(value));
        return this;
    }
    public DynamicParameterListBuilder add(String identifier, Object[] value, String customTypeName, OracleConnection oraConnection ) {
        params.put(identifier, new DynamicParameterList.DynamicArrayParameter(value, customTypeName, oraConnection));
        return this;
    }

    public DynamicParameterList build() {
        return new DynamicParameterList(params);
    }


    public static DynamicParameterListBuilder create() {
        return new DynamicParameterListBuilder();
    }
}
