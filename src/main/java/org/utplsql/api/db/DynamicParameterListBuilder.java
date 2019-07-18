package org.utplsql.api.db;

import java.util.LinkedHashMap;
import java.util.function.Consumer;

public class DynamicParameterListBuilder {

    private LinkedHashMap<String, Consumer<Integer>> params = new LinkedHashMap<>();

    private DynamicParameterListBuilder() {

    }

    public DynamicParameterListBuilder addParameter(String identifier, Consumer<Integer> function) {

        params.put(identifier, function);

        return this;
    }

    public DynamicParameterList build() {
        return new DynamicParameterList(params);
    }


    public static DynamicParameterListBuilder create() {
        return new DynamicParameterListBuilder();
    }
}
