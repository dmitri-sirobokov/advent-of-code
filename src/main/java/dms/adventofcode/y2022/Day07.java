package dms.adventofcode.y2022;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Day 7: No Space Left On Device.
 * Find the folder to delete and free up the space on the device.
 */
public class Day07 {

    private Day07() { }

    public static long part1(List<String> input) {
        var fs = FileSystem.parse(input);
        fs.cd("/");
        List<Directory> allDirectories = getAllDirectories(fs.currentDirectory);
        return allDirectories.stream().filter(d -> d.getSize() <= 100000).mapToLong(Directory::getSize).sum();
    }

    public static long part2(List<String> input) {
        var fs = FileSystem.parse(input);
        fs.cd("/");
        List<Directory> allDirectories = getAllDirectories(fs.currentDirectory);
        var sortedDirectories = allDirectories.stream()
                .sorted(Comparator.comparingLong(Directory::getSize))
                .toList();
        var freeSpace = fs.totalSize - fs.currentDirectory.getSize();
        var needsToFree = 30000000 - freeSpace;
        for (Directory dir : sortedDirectories) {
            if (dir.getSize() >= needsToFree) {
                return dir.getSize();
            }
        }
        return -1;
    }

    private static List<Directory> getAllDirectories(Directory dir) {
        var result = new ArrayList<Directory>();
        result.add(dir);
        dir.directories.forEach(d -> result.addAll(getAllDirectories(d)));
        return result;
    }

    private static class FileSystem {
        private final int totalSize;
        private Directory currentDirectory;
        public static FileSystem parse(List<String> lines) {
            var result = new FileSystem(70000000);
            var lineIndex = 0;
            while (lineIndex < lines.size()) {
                var line = lines.get(lineIndex);
                var lineParts = line.split(" ");
                if ("$".equalsIgnoreCase(lineParts[0])) {
                    if ("cd".equalsIgnoreCase(lineParts[1])) {
                        result.cd(lineParts[2]);
                        lineIndex++;
                    } else if ("ls".equalsIgnoreCase(lineParts[1])) {
                        lineIndex++;
                        while (lineIndex < lines.size()) {
                            line = lines.get(lineIndex);
                            lineParts = line.split(" ");
                            if ("$".equalsIgnoreCase(lineParts[0])) {
                                break;
                            }
                            if ("dir".equalsIgnoreCase(lineParts[0])) {
                                var dir = new Directory(result.currentDirectory, lineParts[1]);
                                result.currentDirectory.directories.add(dir);
                            } else {
                                var file = new File(result.currentDirectory, lineParts[1], Integer.parseInt(lineParts[0]));
                                result.currentDirectory.files.add(file);
                            }
                            lineIndex++;
                        }
                    } else {
                        throw new RuntimeException("Unrecognized command: " + line);
                    }
                } else {
                    throw new RuntimeException("Expected command $ in the next line");
                }

            }
            return result;
        }

        public FileSystem(int totalSize) {
            this.totalSize = totalSize;
            currentDirectory = new Directory(null, "/");
        }

        public void cd(String dir) {
            if ("/".equals(dir)) {
                while (this.currentDirectory.parent != null) {
                    this.currentDirectory = this.currentDirectory.parent;
                }
            } else if ("..".equals(dir)) {
                this.currentDirectory = this.currentDirectory.parent;
            } else {
                var foundDir = this.currentDirectory.directories.stream()
                        .filter(d -> StringUtils.equals(d.name, dir)).findFirst().orElse(null);
                if (foundDir == null) throw new RuntimeException("Directory not found: " + dir);
                this.currentDirectory = foundDir;
            }

        }
    }

    private static abstract class Node {
        public final String name;
        public final Directory parent;

        protected abstract long getSize();
        public Node(Directory parent, String name) {
            this.parent = parent;
            this.name = name;
        }
    }

    private static class File extends Node {
        private final long size;

        public File(Directory parent, String name, int size) {
            super(parent, name);
            this.size = size;
        }

        @Override
        public long getSize() {
            return size;
        }
    }

    private static class Directory extends Node {
        private final List<File> files = new ArrayList<>();
        private final List<Directory> directories = new ArrayList<>();

        public Directory(Directory parent, String name) {
            super(parent, name);
        }
        @Override
        protected long getSize() {
            return directories.stream().mapToLong(Directory::getSize).sum() + files.stream().mapToLong(File::getSize).sum();
        }
    }
}
