package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Represents a directory in the virtual file system.
 * A directory can contain files and other directories, forming a hierarchical structure.
 */
public class Directory extends File implements Serializable {
    private final List<File> files;
    private Directory parent;
    /**
     * Base size of a directory in bytes.
     */
    public final int fourty = 40;
    /**
     * Constructs a new directory with the specified name.
     *
     * @param name The name of the directory.
     */
    public Directory(String name) {
        super(name);
        this.files = new ArrayList<>();
    }
    /**
     * Adds a file or subdirectory to this directory.
     *
     * @param file The file or directory to add.
     * @throws IllegalArgumentException If a file with the same name already exists in this directory.
     */
    public void addFile(File file) {
        if (files.stream().anyMatch(existingFile -> existingFile.getName().equals(file.getName()))) {
            throw new IllegalArgumentException("A file with the name '" + file.getName() + "' already exists.");
        }
        files.add(file);
        if (file instanceof Directory) {
            ((Directory) file).setParent(this);
        }
    }
    /**
     * Sets the parent directory of this directory.
     *
     * @param parent The parent directory.
     */
    public void setParent(Directory parent) {
        this.parent = parent;
    }
    /**
     * Retrieves a file or directory by its name from this directory.
     *
     * @param name The name of the file or directory to retrieve.
     * @return The file or directory with the specified name, or null if it does not exist.
     */
    public File getFile(String name) {
        for (File file : files) {
            if (file.getName().equals(name)) {
                return file;
            }
        }
        return null;
    }
    /**
     * Removes a file or directory by its name from this directory.
     *
     * @param name The name of the file or directory to remove.
     */
    public void removeFile(String name) {
        files.removeIf(file -> file.getName().equals(name));
    }
    /**
     * Retrieves the list of files and directories contained in this directory.
     *
     * @return A list of files and directories in this directory.
     */
    public List<File> getFiles() {
        return files;
    }
    /**
     * Retrieves the parent directory of this directory.
     *
     * @return The parent directory, or null if this is the root directory.
     */
    public Directory getParent() {
        return parent;
    }
    /**
     * Calculates the total size of this directory, including its contents.
     * The size is the base size of the directory plus the sizes of all contained files and subdirectories.
     *
     * @return The total size of the directory in bytes.
     */
    @Override
    public int getSize() {
        int size = fourty;
        for (File file : files) {
            size += file.getSize();
        }
        return size;
    }
    /**
     * Retrieves the type of this file, which is "directory" for this class.
     *
     * @return The type of this file as a string.
     */
    @Override
    public String getType() {
        return "directory";
    }
}
