package a2;

import java.io.File;
import java.util.HashSet;

/** Image will not be serialized*/
@SuppressWarnings("serial")
public class Image extends File{
	HashSet<String> tags;
	
	public Image(String pathname, HashSet<String> tags) {
		super(pathname);
		this.tags = tags;
	}

	
}
