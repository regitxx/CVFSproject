package hk.edu.polyu.comp.comp2021.cvfs;

import hk.edu.polyu.comp.comp2021.cvfs.model.CVFS;
import java.util.Scanner;
/**
 * The main application class for the CVFS system.
 * Provides a command-line interface for managing the virtual file system.
 */
public class Application {
    /**
     * The entry point of the application.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args){
        CVFS cvfs = new CVFS();
        Scanner scanner = new Scanner(System.in);
        // Initialize and utilize the system
        while(true){
            if (cvfs.getCurrentDirectory()==null){
                System.out.print("$:root > ");
            } else {
                System.out.print("$:"+cvfs.getCurrentDirectory().getName()+" > ");
            }
            String input = scanner.nextLine().trim();
            String[] parts = input.split("\\s", 2);
            String p = (parts.length>1?parts[1]:"");
            String[] dp;
            try {
                switch(parts[0]){
                    case "newDisk":
                        cvfs.createDisk(Integer.parseInt(p));
                        break;
                    case "newDoc":
                        dp = p.split("\\s", 3);
                        if (dp.length!=3){
                            throw new IllegalArgumentException("Usage: newDoc <docName> <docType> <docContent>");
                        }
                        cvfs.createDoc(dp[0], dp[1], dp[2]);
                        break;
                    case "newDir":
                        if (parts.length != 2) {
                            throw new IllegalArgumentException("Usage: newDir <dirName>");
                        }
                        cvfs.createDir(p);
                        break;
                    case "delete":
                        cvfs.deleteFile(p);
                        break;
                    case "rename":
                        dp = p.split("\\s", 2);
                        if (dp.length!=2){
                            throw new IllegalArgumentException("Usage: rename <oldFileName> <newFileName>");
                        }
                        cvfs.renameFile(parts[1], parts[2]);
                        break;
                    case "changeDir":
                        cvfs.changeDir(p);
                        break;
                    case "list":
                        cvfs.listFiles();
                        break;
                    case "rList":
                        cvfs.listFilesRecursively();
                        break;
                    case "newSimpleCri":
                        String[] simpleParts = p.split("\\s", 4);
                        if (simpleParts.length != 4) {
                            throw new IllegalArgumentException("Usage: newSimpleCri <criName> <attrName> <op> <val>");
                        }
                        cvfs.createSimpleCriterion(simpleParts[0], simpleParts[1], simpleParts[2], simpleParts[3]);
                        break;
                    case "IsDocument":
                        cvfs.defineIsDocumentCriterion();
                        break;

                    case "newNegation":
                        String[] negParts = p.split("\\s", 2);
                        if (negParts.length != 2) {
                            throw new IllegalArgumentException("Usage: newNegation <criName1> <criName2>");
                        }
                        cvfs.createNegationCriterion(negParts[0], negParts[1]);
                        break;
                    case "newBinaryCri":
                        String[] binParts = p.split("\\s", 4);
                        if (binParts.length != 4) {
                            throw new IllegalArgumentException("Usage: newBinaryCri <criName1> <criName2> <logicOp> <criName3>");
                        }
                        cvfs.createBinaryCriterion(binParts[0], binParts[1], binParts[2], binParts[3]);
                        break;
                    case "printAllCriteria":
                        cvfs.printAllCriteria();
                        break;
                    case "quit":
                        System.out.println("Exiting CVFS. Goodbye!");
                        System.exit(0);
                        break;
                    case "search":
                        if (parts.length != 2) {
                            throw new IllegalArgumentException("Usage: search <criName>");
                        }
                        cvfs.searchFiles(parts[1]);
                        break;

                    case "rSearch":
                        if (parts.length != 2) {
                            throw new IllegalArgumentException("Usage: rSearch <criName>");
                        }
                        cvfs.searchFilesRecursively(parts[1]);
                        break;
                    case "save":
                        if (parts.length != 2) {
                            throw new IllegalArgumentException("Usage: save <path>");
                        }
                        cvfs.saveDisk(parts[1]);
                        break;

                    case "load":
                        if (parts.length != 2) {
                            throw new IllegalArgumentException("Usage: load <path>");
                        }
                        cvfs = CVFS.loadDisk(parts[1]);
                        break;
                    default:
                        System.out.println("Unknown command.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
