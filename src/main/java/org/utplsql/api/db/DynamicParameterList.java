package org.utplsql.api.db;

import oracle.jdbc.OracleConnection;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class DynamicParameterList {

    private LinkedHashMap<String, DynamicParameter> params;

    interface DynamicParameter {
        void setParam( CallableStatement statement, int index ) throws SQLException;
    }

    static class DynamicStringParameter implements DynamicParameter {
        private final String value;

        DynamicStringParameter( String value ) {
            this.value = value;
        }

        @Override
        public void setParam(CallableStatement statement, int index) throws SQLException {
            if ( value == null ) {
                statement.setNull(index, Types.VARCHAR);
            } else {
                statement.setString(index, value);
            }
        }
    }
    static class DynamicIntegerParameter implements DynamicParameter {
        private final Integer value;

        DynamicIntegerParameter( Integer value ) {
            this.value = value;
        }

        @Override
        public void setParam(CallableStatement statement, int index) throws SQLException {
            if ( value == null ) {
                statement.setNull(index, Types.INTEGER);
            } else {
                statement.setInt(index, value);
            }
        }
    }
    static class DynamicArrayParameter implements DynamicParameter {
        private final Object[] value;
        private final String customTypeName;
        private final OracleConnection oraConnection;

        DynamicArrayParameter( Object[] value, String customTypeName, OracleConnection oraConnection ) {
            this.value = value;
            this.customTypeName = customTypeName;
            this.oraConnection = oraConnection;
        }

        @Override
        public void setParam(CallableStatement statement, int index) throws SQLException {
            if ( value == null ) {
                statement.setNull(index, Types.ARRAY, customTypeName);
            } else {
                statement.setArray(
                        index, oraConnection.createOracleArray(customTypeName, value)
                );
            }
        }
    }

    DynamicParameterList(LinkedHashMap<String, DynamicParameter> params) {
        this.params = params;
    }

    public String getSql() {
        return params.keySet().stream()
                .map(e -> e + " => ?")
                .collect(Collectors.joining(", "));
    }

    public void setParamsStartWithIndex(CallableStatement statement, int startIndex ) throws SQLException {
        int index = startIndex;
        for ( DynamicParameter param : params.values() ) {
            param.setParam(statement, index++);
        }
    }

    public static DynamicParameterListBuilder builder() {
        return new DynamicParameterListBuilder();
    }

    public static class DynamicParameterListBuilder {

        private LinkedHashMap<String, DynamicParameterList.DynamicParameter> params = new LinkedHashMap<>();
        private boolean addIfNullOrEmpty = true;

        private DynamicParameterListBuilder() {

        }

        public DynamicParameterListBuilder onlyAddIfNotEmpty() {
            addIfNullOrEmpty = false;
            return this;
        }

        public DynamicParameterListBuilder add(String identifier, String value ) {
            if ( addIfNullOrEmpty || (value != null && !value.isEmpty()) ) {
                params.put(identifier, new DynamicParameterList.DynamicStringParameter(value));
            }
            return this;
        }
        public DynamicParameterListBuilder add(String identifier, Integer value ) {
            if ( addIfNullOrEmpty || (value != null)) {
                params.put(identifier, new DynamicParameterList.DynamicIntegerParameter(value));
            }
            return this;
        }
        public DynamicParameterListBuilder add(String identifier, Object[] value, String customTypeName, OracleConnection oraConnection ) {
            if ( addIfNullOrEmpty || (value != null && value.length > 0 )) {
                params.put(identifier, new DynamicParameterList.DynamicArrayParameter(value, customTypeName, oraConnection));
            }
            return this;
        }

        public DynamicParameterList build() {
            return new DynamicParameterList(params);
        }
    }

}
