package a2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Tester {

	private Image testI;
	private File testF;

	Tester() {
		Tags.load();
		testF = new File("testI");
		if (!testF.exists()) {
			try {
				testF.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			testI = new Image(testF.getAbsolutePath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void testAddTag() {
		String tag = "cake";
		testI.addTag(tag);
		assert testI.getName().contains(Tags.PREFIX + tag) : "Inproperly renamed.";
		assert Tags.getTags().contains(tag) : tag + " not added to Tags.";
	}
	
	public void testDelTag(){
		String tag = null;
		for (String t : testI.tags) {
			tag = t;
			break;
		}
		if(tag == null){
			tag = "cake";
			testI.addTag(tag);
		}
		testI.delTag(tag);
		assert !testI.getName().contains(Tags.PREFIX + tag) : "Inproperly renamed.";
		assert Tags.getTags().contains(tag) : tag + " removed from Tags";
	}
	
	public void takedown(){
		testF.delete();
		testI.close();
	}
	
	public static void main(String[] args) {
		Tags.load();
		System.out.println(Tags.getTags());
		System.out.println(Tags.getTagUsage());
	}
}
