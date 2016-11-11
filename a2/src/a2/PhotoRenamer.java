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
import java.util.Scanner;

public class PhotoRenamer implements Closeable {

	private ArrayList<Image> images = new ArrayList<Image>();
	private File imagesPath;
	protected static String tagsPath = "data/tags";
	protected static String logDir = "data/";

	PhotoRenamer(String imagesPath) {
		String line = "";
		// Reader to load file
		BufferedReader br = null;
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

	public String newImage(String imgPath) {
		for (int i = 0; i < images.size(); i++) {
			if (images.get(i).getPath() == imgPath) {
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
		StringBuffer str = new StringBuffer("Images: \n");
		for (int i = 0; i < images.size(); i++) {
			str.append(i + ".");
			str.append("\t");
			str.append(images.get(i).getName());
			str.append("\n");
		}
		return str.toString();
	}

	public static String toString(ArrayList<Image> images) {
		StringBuffer str = new StringBuffer("Images: \n");
		for (int i = 0; i < images.size(); i++) {
			str.append(i + ".");
			str.append("\t");
			str.append(images.get(i).getName());
			str.append("\n");
		}
		return str.toString();
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
		PhotoRenamer pr = new PhotoRenamer("data/imgPaths");
		Tags.load();
		Scanner input = new Scanner(System.in);
		int key;
		String yn = "n";
		do {
			System.out.println("-----PhotoRenamer-----");
			System.out.println("1. Add new image");
			System.out.println("2. List images");
			System.out.println("3. List images by tag");
			System.out.println("4. Add tag to image");
			System.out.println("5. Delete tag from image");
			System.out.println("6. Undo last change");
			System.out.println("7. Revert change to time");
			System.out.println("8. Settings");
			System.out.println("0. Exit program");
			System.out.print("Enter your selection: ");
			key = input.nextInt();
			// TODO: finish this shit
			switch (key) {
			case 1:
				do {
					yn = "n";
					System.out.print("Enter image file path: ");
					String res = pr.newImage(input.nextLine());
					System.out.println(res);
					System.out.println("Add another image? y/n");
					yn = input.nextLine();
				} while (yn.matches("y"));
				break;
			case 2:
				System.out.println(pr);
				break;
			case 3:
				ArrayList<String> tags = new ArrayList<String>();
				do {
					yn = "n";
					System.out.print("Enter a tag: ");
					tags.add(input.nextLine());
					System.out.println("Keep adding tags? y/n");
					yn = input.nextLine();
				} while (yn.matches("y"));
				System.out.println(PhotoRenamer.toString(pr.listImageByTags(tags)));
				break;
			case 4:
				do {
					yn = "n";
					System.out.println(pr);
					System.out.print("Select image: ");
					int idx = input.nextInt();
					do{
						yn = "n";
						System.out.print("Enter a tag: ");
						String tag = input.nextLine();
						if(pr.images.get(idx).addTag(tag)){
							System.out.println("Tag added.");
						} else {
							System.out.println("Add tag unsuccessful.");
						}
						System.out.println("Keep adding tags to this image? y/n");
						yn = input.nextLine();
					} while (yn.matches("y"));
					System.out.println("Add tags to another image? y/n");
					yn = input.nextLine();
				} while (yn.matches("y"));
				break;
			case 5:
				do {
					yn = "n";
					System.out.println(pr);
					System.out.print("Select image: ");
					int idx = input.nextInt();
					Image chosen= pr.images.get(idx);
					do{
						yn = "n";
						System.out.println(chosen.tags);
						System.out.print("Choose a tag: ");
						String tag = input.nextLine();
						if(chosen.delTag(tag)){
							System.out.println("Tag deleted.");
						} else {
							System.out.println("No such tag.");
						}
						System.out.println("Keep deleting tags from this image? y/n");
						yn = input.nextLine();
					} while (yn.matches("y"));
					System.out.println("Delete tags from another image? y/n");
					yn = input.nextLine();
				} while (yn.matches("y"));
				break;
			case 6:
				do {
					yn = "n";
					System.out.println(pr);
					System.out.print("Select image: ");
					int idx = input.nextInt();
					Image chosen= pr.images.get(idx);
					System.out.println("Choose another image? y/n");
					yn = input.nextLine();
				} while (yn.matches("y"));
				break;
			case 7:
				break;
			case 8:
				break;
			case 0:
				break;
			}
		} while (key != 0);
		input.close();
		pr.close();
		System.out.println("Program terminated, goodbye.");
	}
}
