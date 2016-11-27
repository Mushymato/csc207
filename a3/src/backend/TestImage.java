package backend;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
/** Requires file at D:\Desktop\2016Fall\CSC207\group_0514\a3\testImg\doge.jpg */
public class TestImage {

	Image testImage;
	
	@Before
	public void setUp() throws Exception {
		testImage = new Image("D:\\Desktop\\2016Fall\\CSC207\\group_0514\\a3\\testImg\\doge.jpg"); 
	}

	@After
	public void tearDown() throws Exception {
		testImage.revertToOriginal();
		testImage.close();
	}

	@Test
	public void testAddTag() {
		assertTrue("Tag addition unsuccessful.",testImage.addTag("cake"));
		assertTrue("Did not detect duplicates.",!testImage.addTag("cake"));
		String expectedName = "doge@cake.jpg";
		String actualName = testImage.getName();
		assertTrue("Name change error.",actualName.equals(expectedName));
	}
	
	@Test
	public void testDelTag() {
		testImage.addTag("cake");
		assertTrue("Tag removal unsuccessful.",testImage.delTag("cake"));
		assertTrue("Did not detect lack of tag.",!testImage.delTag("cake"));
		String expectedName = "doge.jpg";
		String actualName = testImage.getName();
		assertTrue("Name change error.",actualName.equals(expectedName));
	}
	
}
