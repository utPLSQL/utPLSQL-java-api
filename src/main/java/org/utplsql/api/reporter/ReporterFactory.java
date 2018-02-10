package org.utplsql.api.reporter;

import org.utplsql.api.CustomTypes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/** This singleton-class manages the instantiation of reporters.
 * One can register a supplier method for a specific name which will then be callable via createReporter(name)
 *
 * @author pesse
 */
public final class ReporterFactory {

    private Map<String, Supplier<? extends Reporter>> reportFactoryMethodMap = new HashMap<>();

    private static ReporterFactory instance;
    
    private ReporterFactory() {
        registerDefaultReporters();
    }

    /** Registers the default reporters, provided with utPLSQL core
     */
    private void registerDefaultReporters() {
        Arrays.stream(DefaultReporters.values())
                .forEach(r -> registerReporterFactoryMethod(r.name(), r.getFactoryMethod()));
    }

    /** Returns the global instance of the ReporterFactory
     *
     * @return ReporterFactory
     */
    public static ReporterFactory getInstance() {
        if ( instance == null ) 
            instance = new ReporterFactory();
        return instance;
    }

    /** Registers a creation method for a specified reporter name. Overrides eventually existing creation method
     *
     * @param reporterName the reporter's name to register
     * @param factoryMethod the method which will return the reporter
     * @return the method stored for the registered name
     */
    public synchronized Supplier<? extends Reporter> registerReporterFactoryMethod( String reporterName, Supplier<? extends Reporter> factoryMethod) {
        return reportFactoryMethodMap.put(reporterName, factoryMethod);
    }

    /** Unregisters a specified reporter name.
     *
     * @param reporterName the reporter's name to unregister
     * @return the method which was previously registered or null
     */
    public synchronized Supplier<? extends Reporter> unregisterReporterFactoryMethod( String reporterName ) {
        return reportFactoryMethodMap.remove(reporterName);
    }

    /** Returns a new reporter of the given name (or should do so). In reality it just calls the registered method
     * for the given reporterName and returns its value (which should be a subclass of Reporter).
     * Usually you should expect a new instance of a reporter, but who knows what evil forces register themselves nowadays...
     *
     * @param reporterName the reporter's name to create a new instance of
     * @return A reporter
     */
    public Reporter createReporter(String reporterName) {
        Supplier<? extends Reporter> supplier = reportFactoryMethodMap.get(reporterName);
        if ( supplier == null )
            throw new RuntimeException("Reporter " + reporterName + " not implemented.");
        else
            return supplier.get();
    }

    /** Returns a set of all registered reporter's names
     *
     * @return Set of reporter names
     */
    public Set<String> getRegisteredReporterNames() {
        return reportFactoryMethodMap.keySet();
    }
}
