package org.utplsql.api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;

/**
 * Helper class for dealing with Resources
 *
 * @author pesse
 */
public class ResourceUtil {

    private ResourceUtil() {
    }

    /**
     * Copy directory from a jar file to the destination folder
     *
     * @param resourceAsPath  The resource to get children from
     * @param targetDirectory If set to true it will only return files, not directories
     */
    public static void copyResources(Path resourceAsPath, Path targetDirectory) {
        try {
            String resourceName = "/" + resourceAsPath;
            Files.createDirectories(targetDirectory);
            URL resourceUrl = ResourceUtil.class.getResource(resourceName);
            if (resourceUrl == null) {
                throw new IOException("Coverage HTML assets not found in classpath: " + resourceName
                        + ". The JAR was built without bundled coverage HTML reporter assets.");
            }
            URI uri = resourceUrl.toURI();
            Path myPath;
            if (uri.getScheme().equalsIgnoreCase("jar")) {
                try (FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                    myPath = fileSystem.getPath(resourceName);
                    copyRecursive(myPath, targetDirectory);
                }
            } else {
                myPath = Paths.get(uri);
                copyRecursive(myPath, targetDirectory);
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static void copyRecursive(Path from, Path targetDirectory) throws IOException {
        Files.walkFileTree(from, new SimpleFileVisitor<>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                super.preVisitDirectory(dir, attrs);
                Path currentTarget = targetDirectory.resolve(from.relativize(dir).toString());
                Files.createDirectories(currentTarget);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                super.visitFile(file, attrs);
                Files.copy(file, targetDirectory.resolve(from.relativize(file).toString()), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
