package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.io.Serializable;
/**
 * Represents a general file in the virtual file system.
 * This is an abstract class to be extended by specific file types such as Document or Directory.
 */
public abstract class File implements Serializable {
    private String name;
    /**
     * Constructs a new file with the specified name.
     *
     * @param name The name of the file.
     */
    public File(String name) {
        this.name = name;
    }
    /**
     * Retrieves the name of this file.
     *
     * @return The name of the file.
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the name of this file.
     *
     * @param name The new name of the file.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Retrieves the type of this file.
     * This is an abstract method to be implemented by subclasses.
     *
     * @return The type of the file as a string.
     */
    public abstract String getType();
    /**
     * Calculates the size of this file in bytes.
     * This is an abstract method to be implemented by subclasses.
     *
     * @return The size of the file in bytes.
     */
    public abstract int getSize();

}
