package a2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class PhotoRenamer implements Closeable {

	private ArrayList<Image> images = new ArrayList<Image>();
	private File imagesPath;
	protected static String dataDirPath = "data/";

	PhotoRenamer(String imagesPath) {
		String line = "";
		// Reader to load file
		BufferedReader br = null;
		File dataDir = new File(PhotoRenamer.dataDirPath);
		if (!dataDir.exists()) {
			dataDir.mkdirs();
		}
		if (!dataDir.isDirectory()) {
			dataDir.delete();
			dataDir.mkdirs();
		}
		try {
			// Attempt to read from file
			this.imagesPath = new File(imagesPath);
			if (!this.imagesPath.exists()) {
				this.imagesPath.createNewFile();
			} else {
				br = new BufferedReader(new FileReader(imagesPath));
				while ((line = br.readLine()) != null) {
					try {
						Image newImg = new Image(line);
						images.add(newImg);
					} catch (FileNotFoundException e) {
						continue;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Make sure to close the reader
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public ArrayList<Image> listImage() {
		return new ArrayList<Image>(images);
	}

	public ArrayList<Image> listImageByTags(String... tags) {
		ArrayList<Image> taggedImages = new ArrayList<Image>();
		for (int i = 0; i < images.size(); i++) {
			Image curr = images.get(i);
			boolean hasAllTags = true;

			for (String tag : tags) {
				if (!curr.tags.contains(tag)) {
					hasAllTags = false;
					break;
				}
			}

			if (hasAllTags) {
				taggedImages.add(curr);
			}

		}
		return taggedImages;
	}

	public ArrayList<Image> listImageByTags(ArrayList<String> tags) {
		ArrayList<Image> taggedImages = new ArrayList<Image>();
		for (int i = 0; i < images.size(); i++) {
			Image curr = images.get(i);
			boolean hasAllTags = true;

			for (String tag : tags) {
				if (!curr.tags.contains(tag)) {
					hasAllTags = false;
					break;
				}
			}

			if (hasAllTags) {
				taggedImages.add(curr);
			}

		}
		return taggedImages;
	}

	public String addImage(String imgPath) {
		for (int i = 0; i < images.size(); i++) {
			if (images.get(i).getPath().equals(imgPath)) {
				return "Image has already been added.";
			}
		}
		try {
			Image newImage = new Image(imgPath);
			images.add(newImage);
			return "Image successfully added.";
		} catch (FileNotFoundException e) {
			return "Image file not found.";
		}
	}

	public String delImage(Image img, boolean clearData) {
		if (images.remove(img)) {
			if (clearData) {
				img.revertName(0);
				img.log.delete();
				return img.imageName() + " removed, data cleared.";
			} else {
				return img.imageName() + " removed.";
			}
		} else {
			return img.imageName() + "not successfully removed.";
		}
	}

	public void writeImagePaths() {
		BufferedWriter bw = null;
		try {
			FileWriter fw = new FileWriter(imagesPath);
			bw = new BufferedWriter(fw);
			for (int i = 0; i < images.size(); i++) {
				bw.write(images.get(i).getPath());
				bw.write("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String toString() {
		return PhotoRenamer.toString(images);
	}

	public static String toString(ArrayList<Image> images) {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < images.size(); i++) {
			str.append("[" + i + "]");
			str.append(" ");
			str.append(images.get(i).getName());
			str.append("\n");
		}
		return str.toString();
	}

	public void clearData() {
		File dataDir = new File(PhotoRenamer.dataDirPath);
		for (int i = 0; i < images.size(); i++) {
			images.get(i).log.delete();
			images.get(i).revertToOriginal();
		}
		if (dataDir.exists()) {
			this.clearDir(dataDir);
			dataDir.delete();
		}
	}

	private void clearDir(File del) {
		if (del.isDirectory()) {
			File[] sub = del.listFiles();
			for (int i = 0; i < sub.length; i++) {
				this.clearDir(sub[i]);
			}
		} else {
			del.delete();
		}
	}

	@Override
	public void close() {
		Tags.writeTags();
		this.writeImagePaths();
		for (int i = 0; i < images.size(); i++) {
			images.get(i).close();
		}
		images = null;
		imagesPath = null;
	}

	public static void main(String[] args) {
		PhotoRenamer pr = new PhotoRenamer("data/imgPaths.data");
		Tags.load();
		Scanner input = new Scanner(System.in);
		int key;
		String yn = "n";
		String tmp;
		boolean success = false;
		do {
			System.out.println("-----PhotoRenamer-----");
			System.out.println("1. Add new image");
			System.out.println("2. Manage images");
			System.out.println("3. View images by tag");
			System.out.println("4. View tags");
			System.out.println("5. Settings");
			System.out.println("0. Exit program");
			System.out.print("Enter your selection: ");
			tmp = input.next();
			try {
				key = Integer.parseInt(tmp);
			} catch (NumberFormatException e) {
				key = -1;
			}
			switch (key) {
			case 1: // Add new image
				do {
					yn = "n";
					System.out.print("Enter image file path: ");
					String path = input.next();
					String res = pr.addImage(path);
					System.out.println(res);
					pr.writeImagePaths();
					System.out.print("Add another image? y/n ");
					yn = input.next();
				} while (yn.matches("y"));
				break;
			case 2: // Manage images
				do {
					if (pr.images.size() == 0) {
						System.out.println("You haven't added any images yet.");
						break;
					}
					yn = "n";
					System.out.println("Images: ");
					System.out.println(pr);
					System.out.print("Select image: ");
					tmp = input.next();
					try {
						key = Integer.parseInt(tmp);
					} catch (NumberFormatException e) {
						key = -1;
					}
					Image chosen;
					try {
						chosen = pr.images.get(key);
					} catch (IndexOutOfBoundsException e) {
						System.out.println("Image selection canceled.");
						break;
					}
					System.out.println("--------Options-------");
					System.out.println("1. Add Tag");
					System.out.println("2. Remove Tag");
					System.out.println("3. Revert to last change");
					System.out.println("4. Revert to specified change");
					System.out.println("5. Remove image");
					System.out.print("Enter your selection: ");
					tmp = input.next();
					try {
						key = Integer.parseInt(tmp);
					} catch (NumberFormatException e) {
						key = -1;
					}
					switch (key) {
					case 1: // Add tag
						do {
							yn = "n";
							System.out.println("Current tags:");
							System.out.println(chosen.tags);
							System.out.print("Enter a tag: ");
							String tag = input.next();
							if (chosen.addTag(tag)) {
								System.out.println("Tag added.");
							} else {
								System.out.println("Tag addition unsuccessful.");
							}
							System.out.print("Keep adding tags to this image? y/n ");
							yn = input.next();
						} while (yn.matches("y"));
						break;
					case 2: // Del tag
						do {
							if (chosen.tags.size() == 0) {
								System.out.println(chosen.getName() + " has no tags.");
								break;
							}
							yn = "n";
							System.out.println("Current tags:");
							System.out.println(chosen.tags);
							System.out.print("Enter a tag: ");
							String tag = input.next();
							if (chosen.delTag(tag)) {
								System.out.println("Tag deleted.");
							} else {
								System.out.println("No such tag.");
							}
							System.out.print("Keep deleting tags from this image? y/n ");
							yn = input.next();
						} while (yn.matches("y"));
						break;
					case 3: // revert 2
					case 4: // revert n
						success = false;
						if (key == 3) {
							success = chosen.revertName(2);
						} else if (key == 4) {
							System.out.print("Enter number of steps to undo: ");
							tmp = input.next();
							try {
								key = Integer.parseInt(tmp);
							} catch (NumberFormatException e) {
								key = -1;
							}
							success = chosen.revertName(key);
						}
						if (success) {
							System.out.println("Successfully reverted image name to " + chosen.getName());
						} else {
							System.out.println("Revert unsucessful.");
						}
						break;
					case 5: // remove image
						System.out.print("Delete data and revert the image name? y/n ");
						yn = input.next();
						String res = pr.delImage(chosen, yn == "y");
						System.out.println(res);
						break;
					default:
						break;
					}
					System.out.print("Choose another image? y/n ");
					yn = input.next();
				} while (yn.matches("y"));

				break;
			case 3: // Get images by tags
				System.out.println("Current Tags:");
				System.out.println(Tags.getTags());

				ArrayList<String> tagChoices = new ArrayList<String>();
				do {
					yn = "n";
					System.out.print("Enter a tag: ");
					tagChoices.add(input.next());
					System.out.println("Keep choosing tags? y/n");
					yn = input.next();
				} while (yn.matches("y"));
				System.out.println("Images tagged as: " + tagChoices.toString());
				System.out.println(PhotoRenamer.toString(pr.listImageByTags(tagChoices)));
				break;
			case 4:
				Map<String, Integer> tagsByUsage = Tags.getTagUsage();
				if(!tagsByUsage.isEmpty()){
					int i = 1;
					for (Map.Entry<String, Integer> tag : tagsByUsage.entrySet()) {
						System.out.printf("%d %s, used %d times.", i, tag.getKey(), tag.getValue());
					}
				} else {
					System.out.println("There are no tags yet.");
				}
				break;
			case 5: // Settings
				System.out.println("-------Settings-------");
				System.out.println("1. Delete all tags and restore file names");
				System.out.println("2. Delete all data");
				System.out.println("0. Cancel");
				System.out.print("Enter your selection:");
				int choice = input.nextInt();
				switch (choice) {
				case 1:
					System.out.print("Are you sure you want to remove tags from all images? y/n");
					yn = input.next();
					if (yn.matches("y")) {
						for (Image img : pr.listImage()) {
							img.revertToOriginal();
						}
						System.out.println("Tags cleared.");
					}
					break;
				case 2:
					System.out.print("Are you sure you want to clear all data? y/n");
					yn = input.next();
					if (yn.matches("y")) {
						pr.clearData();
					}
					break;
				case 0:
				default:
					break;
				}
				break;
			case 0: // Exit program
			default:
				break;
			}
		} while (key != 0);
		input.close();
		pr.close();
		System.out.println("Program terminated, goodbye.");
	}
}
