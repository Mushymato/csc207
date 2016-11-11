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
	protected static String tagsPath;
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

	public boolean newImage(Image img) {
		for (int i = 0; i < images.size(); i++) {
			if (images.get(i).getPath() == img.getPath()) {
				return false;
			}
		}
		return images.add(img);
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
			str.append("\t");
			str.append(images.get(i).getName());
			str.append("\n");
		}
		return str.toString();
	}

	public static String toString(ArrayList<Image> images) {
		StringBuffer str = new StringBuffer("Images: \n");
		for (int i = 0; i < images.size(); i++) {
			str.append("\t");
			str.append(images.get(i).getName());
			str.append("\n");
		}
		return str.toString();
	}

	@Override
	public void close(){
		Tags.writeTags();
		for (int i = 0; i < images.size(); i++) {
			images.get(i).close();
		}
		images = null;
		imagesPath = null;
	}

	public static void main(String[] args) {
		PhotoRenamer pr = new PhotoRenamer("data/imgPaths");
		Tags.open();
		Scanner input = new Scanner(System.in);
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
			int key = input.nextInt();
			// TODO: finish this shit
			switch (key) {
			case 1:
				break;
			case 2:
				System.out.println(pr);
				break;
			case 3:
				String yn = "n";
				ArrayList<String> tags = new ArrayList<String>();
				do {
					System.out.print("Enter a tag: ");
					tags.add(input.nextLine());
					System.out.println("Keep adding tags? y/n");
					yn = input.nextLine();
				} while (yn.matches("y"));
				System.out.println(PhotoRenamer.toString(pr.listImageByTags(tags)));
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			case 8:
				break;
			case 0:
				break;
			}
		} while (!input.equals(0));
		input.close();
		pr.close();
	}
}
