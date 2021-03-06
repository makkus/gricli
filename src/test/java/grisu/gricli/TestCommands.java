package grisu.gricli;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import grisu.gricli.command.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.*;

public class TestCommands {
	
	GricliEnvironment env;
	GricliCommandFactory f;
	
    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

	@Before
	public void setUp(){
		f = GricliCommandFactory.getStandardFactory();
		env = new GricliEnvironment(f);
	}
	
	@Test
	public void testSetDirAsHome() throws Exception {
		SetCommand set = new SetCommand("dir", "~");
		set.execute(env);
		assertEquals(env.get("dir"),"~");
	}
	
	@Test(expected=GricliRuntimeException.class)
	public void testAddError() throws Exception{
		AddCommand c = new AddCommand("x","y");
		c.execute(env);
	}
	
	@Test
	public void testAttachWithTilda() throws Exception{
		String filename = "testAttachWithTilda";
		String path = System.getProperty("user.home") + System.getProperty("file.separator") + filename;
		AttachCommand attach = new AttachCommand(new String[] {"~" + System.getProperty("file.separator") + filename});
		boolean fileExists = false;
		File testfile = new File(path);
		try {		
			fileExists = testfile.exists();
			if (!fileExists){
				FileUtils.touch(new File(path));
				assertTrue(testfile.exists());
			} 
			attach.execute(env);
			assertEquals(env.getList("files").get(0), path);
			
		} catch (IOException e) {
			
		} finally {
			 if (!fileExists){
				testfile.delete();
			} 
		}
	}
	
	
	@Test
	public void testAttachAbsolutePath() throws Exception {
		String filename = "testAttachAbsolutePath";
		File f = folder.newFile(filename);
		AttachCommand attach = new AttachCommand(new String[] {f.getAbsolutePath()});
		attach.execute(env);
		assertEquals(env.getList("files").get(0),f.getAbsolutePath());
	}
	
	
	@Test
	public void testAttachTwoFiles() throws Exception {
		String filename1 = "testAttachTwoFiles1";
		String filename2 = "testAttachTwoFiles2";
		
		File f1 = folder.newFile(filename1);
		File f2 = folder.newFile(filename2);
		
		AttachCommand attach = new AttachCommand(new String[] {f1.getAbsolutePath(), f2.getAbsolutePath()});
		attach.execute(env);
		
		assertEquals(env.getList("files").get(0),f1.getAbsolutePath());
		assertEquals(env.getList("files").get(1),f2.getAbsolutePath());
	}
	
	@Test
	/**
	 * @author Sina Masoud-Ansari
	 *
	 * Made this as testAttachWithTilda was not working as expected
	 * (has been fixed now)
	 */
	public void testAttachWithTilda_2() throws Exception {
		String rand = ""+(int)(Math.random()*10000);
		String partname = File.pathSeparator + "testAttachFromHomeDir_"+rand;
		String filename = System.getProperty("user.home")+partname;
		String shortname = "~" + partname;
		File f = null;
		try {
			f = new File(filename);
			f.createNewFile();
			assertTrue(f.exists());
			AttachCommand attach = new AttachCommand(new String[] {shortname});
			attach.execute(env);
			assertEquals(env.getList("files").get(0), f.getAbsolutePath());
		} catch (GricliException e) {
			System.err.println(e.getMessage());
			fail();
		} catch (IOException e) {
			//e.printStackTrace();
		} finally {
			if (f != null && f.exists()){
				try {
					f.delete();
				} catch (SecurityException e) {
					System.err.println("Unable to delete temp file: "+
							f.getAbsolutePath());
				}
			}
		}
	}
	
	
	@Test
	public void testAttachNothingAfterSomething() throws Exception {
		String filename = "testAttachNothingAfterSomething";
		File f = folder.newFile(filename);
		
		AttachCommand attach1 = new AttachCommand(new String[] {f.getAbsolutePath()});
		AttachCommand attach2 = new AttachCommand(new String[] {});
		
		attach2.execute(attach1.execute(env));
		
		assertEquals(env.getList("files").size(),0);
	}
	
	@Test(expected=GricliRuntimeException.class)
	public void testAttachNonExistent() throws Exception{
		String filename = folder.getRoot().getCanonicalPath() + File.pathSeparator + "a";
		while (new File(filename).exists()){
			filename += "a";
		}
		AttachCommand attach = new AttachCommand(new String[] {filename});
		attach.execute(env);
	}

	@Test
	public void testRunWithTilda() throws Exception{
		String rand = ""+(int)(Math.random()*10000);
		String partname = File.pathSeparator + "testRunWithTilda_"+rand;
		String filename = System.getProperty("user.home")+partname;
		String shortname = "~" + partname;
		File f = null;
		try {
			f = new File(filename);
			f.createNewFile();
			assertTrue(f.exists());
			RunCommand run = new RunCommand(shortname);
			run.execute(env);
		} catch (IOException ex){
			
		} finally {
			if (f != null && f.exists()){
				try {
					f.delete();
				} catch (SecurityException e) {
					System.err.println("Unable to delete temp file: "+
							f.getAbsolutePath());
				}
			}
		}
	}	
	
	@Test
	public void testChdir() throws Exception {
		String dir = folder.getRoot().getCanonicalPath();
		
		ChdirCommand cd = new ChdirCommand(dir);
		cd.execute(env);
		
		File cFile = new File(System.getProperty("user.dir"));
		assertEquals(dir, cFile.getCanonicalPath());
	}
	
	@Test
	public void testCdToHomeDir() throws Exception {
		ChdirCommand cd = new ChdirCommand("~");
		cd.execute(env);
		
		File home = new File(System.getProperty("user.home"));
		File current = new File(System.getProperty("user.dir"));
		
		assertEquals(home.getCanonicalPath(),current.getCanonicalPath());
	}
	
	@Test
	public void testRunWithNoEndOfLine() throws Exception {
		File f = folder.newFile("testRun2.script");
		String scriptName = f.getCanonicalPath();
		FileUtils.writeByteArrayToFile(f, "set jobname hello".getBytes());
		
		RunCommand c = new RunCommand(scriptName);
		c.execute(env);
		
		assertEquals(env.get("jobname"),"hello");
	}
	
	@Test
	public void testRunWithEmptyLines() throws Exception {
		List<String> script = new LinkedList<String>();
		script.add("set description ''");
		script.add("");
		script.add("set jobname hello");
		
		File f = folder.newFile("testRun1.script");
		String scriptName = f.getCanonicalPath();
		FileUtils.writeLines(f,script);
		
		RunCommand c = new RunCommand(scriptName);
		c.execute(env);
		
		assertEquals(env.get("jobname"),"hello");
	}
	
}
