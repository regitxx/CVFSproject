package hk.edu.polyu.comp.comp2021.cvfs.model;
import org.junit.Test;
import static org.junit.Assert.*;
public class CVFSTest {
    @Test
    public void testCVFSConstructor() {
        CVFS cvfs = new CVFS();
        assertNotNull(cvfs);
        assertNull(cvfs.getCurrentDirectory());
    }
    @Test
    public void testCreateDisk() {
        CVFS cvfs = new CVFS();
        cvfs.createDisk(1000);
        assertNotNull(cvfs.getCurrentDirectory());
        assertEquals("root", cvfs.getCurrentDirectory().getName());
    }
    @Test
    public void testCreateDocSuccess() {
        CVFS cvfs = new CVFS();
        cvfs.createDisk(1000);
        cvfs.createDoc("file1", "txt", "Hello World");
        assertNotNull(cvfs.getCurrentDirectory().getFile("file1"));
    }
    @Test(expected = IllegalArgumentException.class)
    public void testCreateDocExceedDiskSize() {
        CVFS cvfs = new CVFS();
        cvfs.createDisk(10);
        // This should throw an exception because the document size exceeds disk size
        cvfs.createDoc("file1", "txt", "TooLargeContent");
    }
    @Test(expected = IllegalArgumentException.class)
    public void testCreateDocInvalidName() {
        CVFS cvfs = new CVFS();
        cvfs.createDisk(1000);
        // Invalid name containing special characters
        cvfs.createDoc("file@", "txt", "Hello");
    }
    @Test
    public void testCreateDirSuccess() {
        CVFS cvfs = new CVFS();
        cvfs.createDisk(1000);
        cvfs.createDir("subdir");
        assertNotNull(cvfs.getCurrentDirectory().getFile("subdir"));
        assertTrue(cvfs.getCurrentDirectory().getFile("subdir") instanceof Directory);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testCreateDirInvalidName() {
        CVFS cvfs = new CVFS();
        cvfs.createDisk(1000);
        // Invalid directory name with special characters
        cvfs.createDir("sub!dir");
    }
    @Test
    public void testDeleteFile() {
        CVFS cvfs = new CVFS();
        cvfs.createDisk(1000);
        cvfs.createDoc("file1", "txt", "Content");
        assertNotNull(cvfs.getCurrentDirectory().getFile("file1"));
        cvfs.deleteFile("file1");
        assertNull(cvfs.getCurrentDirectory().getFile("file1"));
    }
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteNonExistentFile() {
        CVFS cvfs = new CVFS();
        cvfs.createDisk(1000);
        // Attempt to delete a file that does not exist
        cvfs.deleteFile("abobusinapadayut");
    }
    @Test
    public void testRenameFile() {
        CVFS cvfs = new CVFS();
        cvfs.createDisk(1000);
        cvfs.createDoc("file1", "txt", "Content");
        assertNotNull(cvfs.getCurrentDirectory().getFile("file1"));
        cvfs.renameFile("file1", "renamedFile");
        assertNull(cvfs.getCurrentDirectory().getFile("file1"));
        assertNotNull(cvfs.getCurrentDirectory().getFile("renamedFile"));
    }
    @Test
    public void testChangeDir() {
        CVFS cvfs = new CVFS();
        cvfs.createDisk(1000);
        cvfs.createDir("subdir");
        cvfs.changeDir("subdir");
        assertEquals("subdir", cvfs.getCurrentDirectory().getName());
    }
    @Test
    public void testChangeDirToParent() {
        CVFS cvfs = new CVFS();
        cvfs.createDisk(1000);
        cvfs.createDir("subdir");
        cvfs.changeDir("subdir");
        assertEquals("subdir", cvfs.getCurrentDirectory().getName());

        cvfs.changeDir("..");
        assertEquals("root", cvfs.getCurrentDirectory().getName());
    }
    @Test(expected = IllegalArgumentException.class)
    public void testChangeDirNonExistent() {
        CVFS cvfs = new CVFS();
        cvfs.createDisk(1000);
        // Attempt to change to a directory that does not exist
        cvfs.changeDir("abobusinapadayut");
    }
    @Test
    public void testListFiles() {
        CVFS cvfs = new CVFS();
        cvfs.createDisk(1000);
        cvfs.createDoc("file1", "txt", "Content");
        cvfs.createDir("subdir");
        cvfs.listFiles(); // This won't assert but ensures no exceptions occur
    }
    @Test
    public void testListFilesRecursively() {
        CVFS cvfs = new CVFS();
        cvfs.createDisk(1000);
        cvfs.createDoc("file1", "txt", "Content");
        cvfs.createDir("subdir");
        cvfs.changeDir("subdir");
        cvfs.createDoc("file2", "txt", "Content");
        cvfs.changeDir("..");
        cvfs.listFilesRecursively(); // This won't assert but ensures no exceptions occur
    }
    @Test
    public void testBinaryCriterionAndOperator() {
        Criterion left = new IsDocumentCriterion();
        Criterion right = new SimpleCriterion("c1", "type", "equals", "txt");
        BinaryCriterion binary = new BinaryCriterion(left, "&&", right);

        File file = new Document("doc1", "txt", "content");
        assertTrue(binary.evaluate(file));
    }

    @Test
    public void testBinaryCriterionOrOperator() {
        Criterion left = new IsDocumentCriterion();
        Criterion right = new SimpleCriterion("c1", "type", "equals", "html");
        BinaryCriterion binary = new BinaryCriterion(left, "||", right);

        File file = new Document("doc1", "txt", "content");
        assertTrue(binary.evaluate(file));
    }

    @Test
    public void testAddFileToDirectory() {
        Directory dir = new Directory("root");
        Document doc = new Document("doc1", "txt", "content");

        dir.addFile(doc);
        assertEquals(1, dir.getFiles().size());
        assertTrue(dir.getFiles().contains(doc));
    }

    @Test
    public void testDirectoryGetSize() {
        Directory dir = new Directory("root");
        Document doc = new Document("doc1", "txt", "content");

        dir.addFile(doc);
        assertEquals(94, dir.getSize()); // 40 for the directory + 40 + 2 * 7 for the document
    }

    @Test
    public void testRemoveFileFromDirectory() {
        Directory dir = new Directory("root");
        Document doc = new Document("doc1", "txt", "content");

        dir.addFile(doc);
        dir.removeFile("doc1");
        assertEquals(0, dir.getFiles().size());
        assertNull(dir.getFile("doc1"));
    }
    @Test
    public void testDocumentGetSize() {
        Document doc = new Document("doc1", "txt", "content");
        assertEquals(54, doc.getSize()); // 40 base size + 2 * 7 (content length)
    }

    @Test
    public void testDocumentGetType() {
        Document doc = new Document("doc1", "txt", "content");
        assertEquals("txt", doc.getType());
    }

    @Test
    public void testDocumentGetContent() {
        Document doc = new Document("doc1", "txt", "content");
        assertEquals("content", doc.getDocContent());
    }
    @Test
    public void testNegationCriterion() {
        Criterion baseCriterion = new IsDocumentCriterion();
        Criterion negation = new NegationCriterion(baseCriterion);

        File doc = new Document("doc1", "txt", "content");
        Directory dir = new Directory("root");

        assertFalse(negation.evaluate(doc));
        assertTrue(negation.evaluate(dir));
    }
    @Test
    public void testSimpleCriterionNameContains() {
        SimpleCriterion criterion = new SimpleCriterion("c1", "name", "contains", "doc");
        Document doc = new Document("doc1", "txt", "content");
        assertTrue(criterion.evaluate(doc));
        assertFalse(criterion.evaluate(new Document("file", "txt", "content")));
    }

    @Test
    public void testSimpleCriterionTypeEquals() {
        SimpleCriterion criterion = new SimpleCriterion("c1", "type", "equals", "txt");
        Document doc = new Document("doc1", "txt", "content");
        assertTrue(criterion.evaluate(doc));
        assertFalse(criterion.evaluate(new Document("doc2", "java", "content")));
    }

    @Test
    public void testSimpleCriterionSizeComparison() {
        Document doc = new Document("doc1", "txt", "content");

        assertTrue(new SimpleCriterion("c1", "size", ">", "50").evaluate(doc));
        assertFalse(new SimpleCriterion("c1", "size", "<", "50").evaluate(doc));
        assertTrue(new SimpleCriterion("c1", "size", "==", "54").evaluate(doc));
    }
    @Test
    public void testIsDocumentCriterion() {
        Criterion criterion = new IsDocumentCriterion();

        File doc = new Document("doc1", "txt", "content");
        File dir = new Directory("root");

        assertTrue(criterion.evaluate(doc));
        assertFalse(criterion.evaluate(dir));
    }
    @Test
    public void testSearchFilesByCriterion() {
        CVFS cvfs = new CVFS();
        cvfs.createDisk(1000);
        cvfs.createDoc("file1", "txt", "\"Content\"");
        cvfs.createDoc("file2", "java", "\"Content\"");
        cvfs.createSimpleCriterion("cd", "type", "equals", "\"txt\"");

        cvfs.searchFiles("cd"); // Ensure no exceptions are thrown and valid results
    }

    @Test
    public void testRecursiveSearchByCriterion() {
        CVFS cvfs = new CVFS();
        cvfs.createDisk(1000);
        cvfs.createDoc("file1", "txt", "\"Content\"");
        cvfs.createDir("subdir");
        cvfs.changeDir("subdir");
        cvfs.createDoc("file2", "txt", "\"Content\"");
        cvfs.changeDir("..");
        cvfs.createSimpleCriterion("cd", "type", "equals", "\"txt\"");

        cvfs.searchFilesRecursively("cd"); // Ensure no exceptions are thrown and valid results
    }

}