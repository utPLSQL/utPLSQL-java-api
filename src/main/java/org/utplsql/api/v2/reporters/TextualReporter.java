package org.utplsql.api.v2.reporters;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Pavel Kaplya on 01.05.2019.
 * Reporter producing its result as plain text
 */
public interface TextualReporter {

    /**
     * Print the lines as soon as they are produced and write to a PrintStream.
     *
     * @param ps the PrintStream to be used, e.g: System.out
     */
    void printAvailable(PrintStream ps);

    /**
     * Print the lines as soon as they are produced and write to a list of PrintStreams.
     *
     * @param printStreams the PrintStream list to be used, e.g: System.out, new PrintStream(new FileOutputStream)
     */
    void printAvailable(List<PrintStream> printStreams);

    /**
     * Print the lines as soon as they are produced and call the callback passing the new line.
     *
     * @param onLineFetched the callback to be called
     */
    void fetchAvailable(Consumer<String> onLineFetched);

    /**
     * Get all lines from output buffer and return it as a list of strings.
     *
     * @return the lines
     */
    List<String> fetchAll();

}
