package org.utplsql.api.db;

import oracle.jdbc.OracleConnection;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

/** Lets you build a list of parameters for a CallableStatement
 * <br>
 * Create it with the Builder (DynamicParameterList.builder())
 *
 * @author pesse
 */
public class DynamicParameterList {

    private LinkedHashMap<String, DynamicParameter> params;

    interface DynamicParameter {
        void setParam( CallableStatement statement, int index ) throws SQLException;
    }

    private DynamicParameterList(LinkedHashMap<String, DynamicParameter> params) {
        this.params = params;
    }

    /** Returns the SQL of this ParameterList as comma-separated list of the parameter identifiers:<br>
     *
     * e.g. "a_parameter1 => ?, a_parameter2 => ?"
     *
     * @return comma-separated list of parameter identifiers
     */
    public String getSql() {
        return params.keySet().stream()
                .map(e -> e + " => ?")
                .collect(Collectors.joining(", "));
    }

    /** Sets the contained parameters in the order they were added to the given statement by index, starting with the given one
     *
     * @param statement The statement to set the parameters to
     * @param startIndex The index to start with
     * @throws SQLException SQLException of the underlying statement.setX methods
     */
    public void setParamsStartWithIndex(CallableStatement statement, int startIndex ) throws SQLException {
        int index = startIndex;
        for ( DynamicParameter param : params.values() ) {
            param.setParam(statement, index++);
        }
    }

    /** Returns a builder to create a DynamicParameterList
     *
     * @return Builder
     */
    public static DynamicParameterListBuilder builder() {
        return new DynamicParameterListBuilder();
    }

    /** Builder-class for DynamicParameterList
     * <br>
     * Usage:
     * <pre>
     *  DynamicParameterList.builder()
     *      .add("parameter1", "StringParameter")
     *      .add("parameter2", 123)
     *      .build();
     * </pre>
     *
     * @author pesse
     */
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

    /* Implementations of DynamicStringParameter */
    private static class DynamicStringParameter implements DynamicParameter {
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

    private static class DynamicIntegerParameter implements DynamicParameter {
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

    private static class DynamicArrayParameter implements DynamicParameter {
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

}
