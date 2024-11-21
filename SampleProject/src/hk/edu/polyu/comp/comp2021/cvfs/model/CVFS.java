package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
/**
 * The CVFS class represents a virtual file system.
 * It provides functionality for managing files and directories,
 * handling criteria, and saving/loading the virtual disk.
 */
public class CVFS implements Serializable {
    private int diskSize;
    private int currentDiskUsage;
    private Directory rootDirectory;
    private Directory currentDirectory;
    private final Map<String, Criterion> criteriaMap;
    /**
     * Constructs a new CVFS instance.
     * Initializes the virtual file system with no disk and no criteria.
     */
    public CVFS(){
        this.diskSize = 0;
        this.currentDiskUsage = 0;
        Directory zero = null;
        this.rootDirectory = zero;
        this.currentDirectory = zero;
        this.criteriaMap = new HashMap<>();
    }
    /**
     * Saves the virtual disk and its contents to the specified file.
     *
     * @param path The path to the file where the virtual disk will be saved.
     */
    public void saveDisk(String path) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(this);
            System.out.println("Virtual disk and criteria saved successfully to " + path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save the virtual disk: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @return the current directory
     */
    public Directory getCurrentDirectory() {
        return currentDirectory;
    }

    /**
     * Loads a virtual disk from the specified file.
     *
     * @param path The path to the file containing the saved virtual disk.
     * @return The loaded CVFS instance.
     */
    public static CVFS loadDisk(String path) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            CVFS loadedDisk = (CVFS) ois.readObject();
            System.out.println("Virtual disk and criteria loaded successfully from " + path);
            return loadedDisk;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load the virtual disk: " + e.getMessage(), e);
        }
    }

    /**
     * Adds a new search criterion.
     *
     * @param criName  The name of the criterion.
     * @param criterion The criterion to add.
     */
    public void addCriterion(String criName, Criterion criterion) {
        criteriaMap.put(criName, criterion);
    }

    /**
     * Retrieves a search criterion by its name.
     *
     * @param criName The name of the criterion to retrieve.
     * @return The {@link Criterion} object associated with the name, or null if it doesn't exist.
     */
    public Criterion getCriterion(String criName) {
        return criteriaMap.get(criName);
    }
    /**
     * Creates a new virtual disk with the specified size.
     *
     * @param diskSize The size of the disk in bytes.
     */
    public void createDisk(int diskSize){
        this.rootDirectory = new Directory("root");
        this.currentDirectory = rootDirectory;
        this.diskSize = diskSize;
        System.out.println("Disk created with size " + diskSize);
    }
    /**
     * Creates a new document in the current directory.
     *
     * @param name    The name of the document.
     * @param type    The type of the document (e.g., txt, java, html, css).
     * @param content The content of the document.
     */
    public void createDoc(String name, String type, String content){
        Document doc = new Document(name, type, content);
        if (doc.getSize()+currentDiskUsage>diskSize){
            throw new IllegalArgumentException("Document size exceeds disk size");
        }
        if (!type.matches("txt|java|html|css")){
            throw new IllegalArgumentException("Document type not supported");
        }
        if (name.length()>10){
            throw new IllegalArgumentException("Document name exceeds 10 characters");
        }
        if (!name.matches("[a-zA-Z0-9]+")){
            throw new IllegalArgumentException("only digits and English letters are allowed\n" +
                    "in file names");
        }
        currentDiskUsage+=doc.getSize();
        currentDirectory.addFile(doc);
        System.out.println("Document created successfully");
    }
    /**
     * Creates a new directory in the current directory.
     *
     * @param dirName The name of the new directory.
     */
    public void createDir(String dirName){
        Directory dir = new Directory(dirName);
        if (dir.getSize()+currentDiskUsage>diskSize){
            throw new IllegalArgumentException("Directory size exceeds disk size");
        }
        if (dirName.length()>10){
            throw new IllegalArgumentException("Directory name exceeds 10 characters");
        }
        if (!dirName.matches("[a-zA-Z0-9]+")){
            throw new IllegalArgumentException("only digits and English letters are allowed\n" +
                    "in file names");
        }
        currentDirectory.addFile(dir);
        currentDiskUsage+=dir.getSize();
        System.out.println("Directory created successfully");
    }
    /**
     * Deletes a file or directory from the current directory.
     *
     * @param name The name of the file or directory to delete.
     */
    public void deleteFile(String name) {
        File file = currentDirectory.getFile(name);
        if (file == null) {
            throw new IllegalArgumentException("File not found.");
        }
        currentDirectory.removeFile(name);
        currentDiskUsage -= file.getSize();
        System.out.println("File deleted successfully.");
    }
    /**
     * Renames a file or directory in the current directory.
     *
     * @param oldName The current name of the file or directory.
     * @param newName The new name of the file or directory.
     */
    public void renameFile(String oldName, String newName) {
        File file = currentDirectory.getFile(oldName);
        if (file == null) {
            throw new IllegalArgumentException("File not found.");
        }
        if (!newName.matches("[a-zA-Z0-9]+")){
            throw new IllegalArgumentException("only digits and English letters are allowed\n" +
                    "in file names");
        }
        file.setName(newName);
        System.out.println("File renamed successfully.");
    }
    /**
     * Changes the current working directory.
     *
     * @param name The name of the directory to change to, or ".." to move to the parent directory.
     */
    public void changeDir(String name){
        if (name.equals("..")){
            if (currentDirectory == rootDirectory){
                throw new IllegalArgumentException("Already at root directory");
            } else {
                currentDirectory = currentDirectory.getParent();
            }
        } else {
            File dir = currentDirectory.getFile(name);
            if (!(dir instanceof Directory) ) {
                throw new IllegalArgumentException("Directory not found.");
            } else {
                currentDirectory = (Directory) dir;
            }
        }
        System.out.println("Directory changed successfully.");
    }
    /**
     * Lists all files and directories in the current directory.
     */
    public void listFiles() {
        List<File> files = currentDirectory.getFiles();
        int number = 0, size = 0;
        if (files == null) {
            throw new IllegalArgumentException("No files found in current directory");
        }
        for (File file : files) {
            String details = file.getName() + " (" + file.getType() + ") - " + file.getSize() + " bytes";
            System.out.println(details);
            number++;
            size+=file.getSize();
        }
        System.out.println("Number of files: "+number+", Total size: "+size);
    }
    /**
     * Recursively lists all files and directories starting from the current directory.
     */
    public void listFilesRecursively() {
        System.out.println("Files in " + currentDirectory.getName() + " (recursive):");
        int[] total = {0}; // Total number of files
        int[] totalSize = {0}; // Total size of all files
        listRecursiveHelper(currentDirectory, 0, total, totalSize);
        System.out.println("Total files: " + total[0] + ", Total size: " + totalSize[0] + " bytes.");
    }

    private void listRecursiveHelper(Directory directory, int level, int[] total, int[] totalSize) {
        for (File file : directory.getFiles()) {
            String indent = new String(new char[level]).replace("\0", "  "); // Indentation
            String details = indent + file.getName() + " (" + file.getType() + ") - " + file.getSize() + " bytes";
            System.out.println(details);
            if (level == 0){
                totalSize[0] += file.getSize();
            }
            total[0]++;
            if (file instanceof Directory) {
                listRecursiveHelper((Directory) file, level + 1, total, totalSize);
            }
        }
    }
    /**
     * Creates a new simple search criterion.
     *
     * @param criName  The name of the criterion (must contain exactly two English letters).
     * @param attrName The attribute to evaluate ("name", "type", or "size").
     * @param op       The operator to use for evaluation:
     *                 - "contains" for "name".
     *                 - "equals" for "type".
     *                 - ">", "<", ">=", "<=", "==", "!=" for "size".
     * @param val      The value to compare the attribute against.
     *                 - A string in double quotes for "name" or "type".
     *                 - An integer for "size".
     * @throws IllegalArgumentException If the criterion name is invalid,
     *                                  the attribute name is unsupported,
     *                                  the operator is invalid for the attribute,
     *                                  or the value format is incorrect.
     */
    public void createSimpleCriterion(String criName, String attrName, String op, String val) {
        // Validate criName
        if (!criName.matches("[a-zA-Z]{2}")) {
            throw new IllegalArgumentException("Criterion name must contain exactly two English letters.");
        }

        // Validate attrName and corresponding conditions
        if (attrName.equals("name")) {
            if (!op.equals("contains")) {
                throw new IllegalArgumentException("For attribute 'name', operator must be 'contains'.");
            }
            if (!val.matches("\"[^\"]*\"")) {
                throw new IllegalArgumentException("Value for 'name' must be a string in double quotes.");
            }
            val = val.substring(1, val.length() - 1); // Remove double quotes
        } else if (attrName.equals("type")) {
            if (!op.equals("equals")) {
                throw new IllegalArgumentException("For attribute 'type', operator must be 'equals'.");
            }
            if (!val.matches("\"[^\"]*\"")) {
                throw new IllegalArgumentException("Value for 'type' must be a string in double quotes.");
            }
            val = val.substring(1, val.length() - 1); // Remove double quotes
        } else if (attrName.equals("size")) {
            if (!op.matches(">|<|>=|<=|==|!=")) {
                throw new IllegalArgumentException("For attribute 'size', operator must be one of >, <, >=, <=, ==, !=.");
            }
            try {
                Integer.parseInt(val); // Ensure val is an integer
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Value for 'size' must be an integer.");
            }
        } else {
            throw new IllegalArgumentException("Attribute name must be one of: name, type, or size.");
        }

        // Create and store the simple criterion
        SimpleCriterion criterion = new SimpleCriterion(criName, attrName, op, val);
        criteriaMap.put(criName, criterion);
        System.out.println("Simple criterion '" + criName + "' created successfully.");
    }

    /**
     * Defines the "IsDocument" criterion.
     * This criterion evaluates to true if and only if the file is a document.
     */
    public void defineIsDocumentCriterion() {
        criteriaMap.put("IsDocument", new IsDocumentCriterion());
        System.out.println("Criterion 'IsDocument' defined successfully.");
    }

    /**
     * Creates a new negation criterion.
     *
     * @param criName1 The name of the new negation criterion.
     * @param criName2 The name of the existing criterion to negate.
     * @throws IllegalArgumentException If the referenced criterion does not exist.
     */
    public void createNegationCriterion(String criName1, String criName2) {
        Criterion inner = criteriaMap.get(criName2);
        if (inner == null) {
            throw new IllegalArgumentException("Criterion '" + criName2 + "' does not exist.");
        }
        criteriaMap.put(criName1, new NegationCriterion(inner));
        System.out.println("Negation criterion '" + criName1 + "' created successfully.");
    }
    /**
     * Creates a new binary criterion that combines two existing criteria.
     *
     * @param criName1 The name of the new binary criterion.
     * @param criName2 The name of the first existing criterion.
     * @param logicOp  The logical operator ("&&" or "||") to combine the criteria.
     * @param criName3 The name of the second existing criterion.
     * @throws IllegalArgumentException If one or both referenced criteria do not exist
     *                                  or if the logical operator is invalid.
     */
    public void createBinaryCriterion(String criName1, String criName2, String logicOp, String criName3) {
        Criterion left = criteriaMap.get(criName2);
        Criterion right = criteriaMap.get(criName3);
        if (left == null || right == null) {
            throw new IllegalArgumentException("One or both criteria do not exist.");
        }
        if (!logicOp.equals("&&") && !logicOp.equals("||")) {
            throw new IllegalArgumentException("Logic operator must be '&&' or '||'.");
        }
        criteriaMap.put(criName1, new BinaryCriterion(left, logicOp, right));
        System.out.println("Binary criterion '" + criName1 + "' created successfully.");
    }

    /**
     * Prints all defined search criteria.
     */
    public void printAllCriteria() {
        if (criteriaMap.isEmpty()) {
            System.out.println("No criteria defined.");
            return;
        }
        System.out.println("Defined criteria:");
        for (Map.Entry<String, Criterion> entry : criteriaMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
    /**
     * Searches for files in the working directory that satisfy a given criterion.
     *
     * @param criName The name of the criterion to apply.
     * @throws IllegalArgumentException If the criterion does not exist.
     */
    public void searchFiles(String criName) {
        Criterion criterion = criteriaMap.get(criName);
        if (criterion == null) {
            throw new IllegalArgumentException("Criterion '" + criName + "' does not exist.");
        }

        List<File> files = currentDirectory.getFiles();
        int totalFiles = 0;
        int totalSize = 0;

        System.out.println("Files in the working directory that satisfy '" + criName + "':");
        for (File file : files) {
            if (criterion.evaluate(file)) {
                System.out.println(file.getName() + " (" + file.getType() + ") - " + file.getSize() + " bytes");
                totalFiles++;
                totalSize += file.getSize();
            }
        }
        System.out.println("Total files: " + totalFiles + ", Total size: " + totalSize + " bytes.");
    }

    /**
     * Recursively searches for files in the working directory that satisfy a given criterion.
     *
     * @param criName The name of the criterion to apply.
     * @throws IllegalArgumentException If the criterion does not exist.
     */
    public void searchFilesRecursively(String criName) {
        Criterion criterion = criteriaMap.get(criName);
        if (criterion == null) {
            throw new IllegalArgumentException("Criterion '" + criName + "' does not exist.");
        }

        int[] stats = {0, 0}; // [totalFiles, totalSize]
        System.out.println("Files in the working directory (recursive) that satisfy '" + criName + "':");
        searchRecursiveHelper(currentDirectory, criterion, stats);
        System.out.println("Total files: " + stats[0] + ", Total size: " + stats[1] + " bytes.");
    }

    private void searchRecursiveHelper(Directory directory, Criterion criterion, int[] stats) {
        for (File file : directory.getFiles()) {
            if (criterion.evaluate(file)) {
                System.out.println(file.getName() + " (" + file.getType() + ") - " + file.getSize() + " bytes");
                stats[0]++;
                stats[1] += file.getSize();
            }
            if (file instanceof Directory) {
                searchRecursiveHelper((Directory) file, criterion, stats);
            }
        }
    }
}
