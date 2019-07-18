package org.utplsql.api.db;

import java.util.LinkedHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DynamicParameterList {

    LinkedHashMap<String, Consumer<Integer>> params;

    DynamicParameterList(LinkedHashMap<String, Consumer<Integer>> params) {
        this.params = params;
    }

    public String getSql() {
        return params.keySet().stream()
                .map(e -> e + " = ?")
                .collect(Collectors.joining(", "));
    }

    public void applyFromIndex( int startIndex ) {
        int index = startIndex;
        for ( Consumer<Integer> function : params.values() ) {
            function.accept(index++);
        }
    }

}
