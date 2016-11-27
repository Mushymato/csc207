package backend;

import static org.junit.Assert.*;

import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestTags {

	@Before
	public void setUp() throws Exception {
		PRWrapper.dataDirPath = "data/";
		Tags.load();
	}

	@After
	public void tearDown() throws Exception {
		while (Tags.getTagUsage().get("Tag") != null) {
			Tags.removeTag("Tag");
		}
		Tags.writeTags();
	}

	@Test
	/** Check that 0 usage tags are removed properly and tag usage map is sorted.*/
	public void testGetTagsUsage() {
		Map<String, Integer> tagUse = Tags.getTagUsage();
		Map.Entry<String, Integer> prev = null;
		for (Map.Entry<String, Integer> t : tagUse.entrySet()) {
			assertTrue("0 use tag not disposed", t.getValue() != 0);
			if(prev != null){
				assertTrue("Tag usage map not sorted", prev.getValue() >= t.getValue());
			}
			prev = t;
		}
	}

	@Test
	/** Check that new tags are added and usage count is incremented*/
	public void testNewTag() {
		Tags.newTag("Tag");
		assertTrue(Tags.getTagUsage().get("Tag") == 1);
		assertTrue(Tags.getTags().contains("Tag"));
		Tags.newTag("Tag");
		assertTrue(Tags.getTagUsage().get("Tag") == 2);
	}

	@Test
	/** Check that tags are removed and usage count is decremented and removed once usage becomes 0*/
	public void testRemoveTag() {
		Tags.newTag("Tag");
		Tags.newTag("Tag");
		Tags.removeTag("Tag");
		assertTrue(Tags.getTagUsage().get("Tag") == 1);
		Tags.removeTag("Tag");
		assertTrue(!Tags.getTags().contains("Tag"));
	}
}
