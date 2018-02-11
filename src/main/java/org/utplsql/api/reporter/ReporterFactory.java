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


    public static class ReporterInfo {
        public ReporterInfo( Supplier<? extends Reporter> factoryMethod, String description) {
            this.factoryMethod = factoryMethod;
            this.description = description;
        }
        public Supplier<? extends Reporter> factoryMethod;
        public String description;
    }

    private Map<String, ReporterInfo> reportFactoryMethodMap = new HashMap<>();

    private static ReporterFactory instance;
    
    private ReporterFactory() {
        registerDefaultReporters();
    }

    /** Registers the default reporters, provided with utPLSQL core
     */
    private void registerDefaultReporters() {
        Arrays.stream(DefaultReporters.values())
                .forEach(r -> registerReporterFactoryMethod(r.name(), r.getFactoryMethod(), r.getDescription()));
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
     * @param description the description of the reporter
     * @return Object with information about the registered reporter
     */
    public synchronized ReporterInfo registerReporterFactoryMethod( String reporterName, Supplier<? extends Reporter> factoryMethod, String description) {
        return reportFactoryMethodMap.put(reporterName, new ReporterInfo(factoryMethod, description));
    }

    /** Unregisters a specified reporter name.
     *
     * @param reporterName the reporter's name to unregister
     * @return information about the reporter which was previously registered or null
     */
    public synchronized ReporterInfo unregisterReporterFactoryMethod( String reporterName ) {
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

        if ( !reportFactoryMethodMap.containsKey(reporterName))
            throw new RuntimeException("Reporter " + reporterName + " not implemented.");

        ReporterInfo ri = reportFactoryMethodMap.get(reporterName);
        if ( ri == null )
            throw new RuntimeException("ReporterInfo for " + reporterName + " was null");

        Supplier<? extends Reporter> supplier = ri.factoryMethod;
        if ( supplier == null )
            throw new RuntimeException("No factory method for " + reporterName);
        else
            return supplier.get();
    }

    /** Returns a set of all registered reporter's names
     *
     * @return Set of reporter names
     */
    public Map<String, String> getRegisteredReporterInfo() {
        Map<String, String> descMap = new HashMap<>(reportFactoryMethodMap.size());

        for (Map.Entry<String, ReporterInfo> entry : reportFactoryMethodMap.entrySet()) {
            descMap.put(entry.getKey(), entry.getValue().description);
        }
        return descMap;
    }

}
