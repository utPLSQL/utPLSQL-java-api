package org.utplsql.api;

import com.sun.nio.zipfs.ZipPath;
import org.utplsql.api.reporter.CoverageHTMLReporter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Helper class for dealing with Resources
 *
 * @author pesse
 */
public class ResourceUtil {

    private ResourceUtil() {}

    /**
     * Returns the Path to a resource so it is walkable no matter if it's inside a jar or on the file system
     *
     * @param resourceName The name of the resource
     * @return Path to the resource, either in JAR or on file system
     * @throws IOException
     * @throws URISyntaxException
     */
    public static Path getPathToResource(String resourceName) throws IOException, URISyntaxException {
        URI uri = CoverageHTMLReporter.class.getResource(resourceName).toURI();
        Path myPath;
        if (uri.getScheme().equalsIgnoreCase("jar")) {
            FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
            myPath = fileSystem.getPath(resourceName);
        } else {
            myPath = Paths.get(uri);
        }

        return myPath;
    }

    /**
     * Returns the relative paths of all children of the given resource. Relative path begins from the first atom of the given path.
     *
     * @param resourceAsPath The resource to get children from
     * @param filesOnly      If set to true it will only return files, not directories
     * @return List of relative Paths to the children
     * @throws IOException
     * @throws URISyntaxException
     */
    public static List<Path> getListOfChildren(Path resourceAsPath, boolean filesOnly) throws IOException, URISyntaxException {

        Path resourcePath = getPathToResource("/" + resourceAsPath.toString());
        int relativeStartIndex = resourcePath.getNameCount() - resourceAsPath.getNameCount();

        final List<Path> result = new ArrayList<>();

        if (resourcePath instanceof ZipPath) {
            try (ZipFile zf = new ZipFile(resourcePath.getFileSystem().toString())) {

                for (Enumeration list = zf.entries(); list.hasMoreElements(); ) {
                    ZipEntry entry = (ZipEntry) list.nextElement();
                    // Get entry-path with root element so we can compare it
                    Path entryPath = resourcePath.getRoot().resolve(resourcePath.getFileSystem().getPath(entry.toString()));

                    if (entryPath.startsWith(resourcePath) && (!filesOnly || !entry.isDirectory()))
                        result.add(entryPath.subpath(relativeStartIndex, entryPath.getNameCount()));
                }
            }
            resourcePath.getFileSystem().close();
        } else {
            Files.walk(resourcePath)
                    .filter(p -> !filesOnly || p.toFile().isFile())
                    .forEach(p -> result.add(p.subpath(relativeStartIndex, p.getNameCount())));

        }

        return result;
    }
}