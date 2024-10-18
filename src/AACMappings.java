import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KeyNotFoundException;
import edu.grinnell.csc207.util.NullKeyException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.NoSuchElementException;

/**
 * Creates a set of mappings of an AAC that has two levels,
 * one for categories and then within each category, it has
 * images that have associated text to be spoken. This class
 * provides the methods for interacting with the categories
 * and updating the set of images that would be shown and handling
 * an interactions.
 * 
 * @author Catie Baker & Lily Blanchard
 *
 */
public class AACMappings implements AACPage {

	/** The array that stores the category image/ name pairs. */
	AACCategory names = new AACCategory("names");

	/** The array that stores the image loc/ AAs. */
	AssociativeArray<String, AACCategory> categories;

	/** What category we're currently in. "" if at top level. */
	String here;
	
	/**
	 * Creates a set of mappings for the AAC based on the provided
	 * file. The file is read in to create categories and fill each
	 * of the categories with initial items. The file is formatted as
	 * the text location of the category followed by the text name of the
	 * category and then one line per item in the category that starts with
	 * > and then has the file name and text of that image
	 * 
	 * for instance:
	 * img/food/plate.png food
	 * >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon
	 * img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing
	 * and food has french fries and watermelon and clothing has a 
	 * collared shirt
	 * @param filename the name of the file that stores the mapping information
	 */
	public AACMappings(String filename) {
		this.categories = new AssociativeArray<String, AACCategory>();
		this.here = "";
		try {
			FileReader file = new FileReader(filename);
			BufferedReader eyes = new BufferedReader(file);
			String line;
			AACCategory catHolder = null;

			while (eyes.read() > -1) {
				line = eyes.readLine();
				if (!line.startsWith(">")) {
					String[] linePieces = line.split(" ", 1);
					if (linePieces.length != 2) {
						System.err.println("Invalid file format.");
						break;
					} // if
					catHolder = new AACCategory(linePieces[1]);
					try {
						categories.set(linePieces[0], catHolder);
						names.addItem(linePieces[0], linePieces[1]);
					} catch (NullKeyException e) {
					} // try/catch
				} else {
					String[] linePieces = line.split(" ", 1);
					if (linePieces.length != 2) {
						System.err.println("Invalid file format.");
						break;
					} // if
					if (catHolder != null) {
						this.categories.get(here).addItem(linePieces[0], linePieces[1]);
					} else {
						System.err.println("Invalid file format.");
					} // if
				} // if
			} // while

			file.close();
			eyes.close();
		} catch (Exception FileNotFoundException) {
			System.err.println("File not found.");
		} // try/catch
	} // AACMappings
	
	/**
	 * Given the image location selected, it determines the action to be
	 * taken. This can be updating the information that should be displayed
	 * or returning text to be spoken. If the image provided is a category, 
	 * it updates the AAC's current category to be the category associated 
	 * with that image and returns the empty string. If the AAC is currently
	 * in a category and the image provided is in that category, it returns
	 * the text to be spoken.
	 * @param imageLoc the location where the image is stored
	 * @return if there is text to be spoken, it returns that information, otherwise
	 * it returns the empty string
	 * @throws NoSuchElementException if the image provided is not in the current 
	 * category
	 */
	public String select(String imageLoc) {
		if (this.names.hasImage(imageLoc)) {
			this.here = imageLoc;
			return "";
		} else if (!this.here.equals("")) {
			try {
				return categories.get(this.here).select(imageLoc);
			} catch (KeyNotFoundException e) {
				throw new NoSuchElementException();
			} // try/catch
		} else {
			throw new NoSuchElementException();
		} // if
	} // select(String)
	
	/**
	 * Provides an array of all the images in the current category
	 * @return the array of images in the current category; if there are no images,
	 * it should return an empty array
	 */
	public String[] getImageLocs() {
		if (this.here.equals("")) {
			return categories.getKeys();
		} else {
			try {
				return categories.get(this.here).imgTxt.getKeys();
			} catch (KeyNotFoundException e) {
			} // try/catch
		}
		return null;
	} // getImageLocs()
	
	/**
	 * Resets the current category of the AAC back to the default
	 * category
	 */
	public void reset() {
		this.here = "";
	} // reset()
	
	
	/**
	 * Writes the ACC mappings stored to a file. The file is formatted as
	 * the text location of the category followed by the text name of the
	 * category and then one line per item in the category that starts with
	 * > and then has the file name and text of that image
	 * 
	 * for instance:
	 * img/food/plate.png food
	 * >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon
	 * img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing
	 * and food has french fries and watermelon and clothing has a 
	 * collared shirt
	 * 
	 * @param filename the name of the file to write the
	 * AAC mapping to
	 */
	public void writeToFile(String filename) {
		// STUB
	} // writeToFile(String)
	
	/**
	 * Adds the mapping to the current category (or the default category if
	 * that is the current category)
	 * @param imageLoc the location of the image
	 * @param text the text associated with the image
	 */
	public void addItem(String imageLoc, String text) {
		this.names.addItem(imageLoc, text);
	} // addItem()


	/**
	 * Gets the name of the current category
	 * @return returns the current category or the empty string if 
	 * on the default category
	 */
	public String getCategory() {
		if (this.here.equals("")) {
			return "";
		} else {
			return names.select(this.here);
		} // if
	} // getCategory()


	/**
	 * Determines if the provided image is in the set of images that
	 * can be displayed and false otherwise
	 * @param imageLoc the location of the category
	 * @return true if it is in the set of images that
	 * can be displayed, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		String[] locs = categories.getKeys();
		for (String loc : locs) {
			if (imageLoc.equals(loc)) {
				return true;
			} // if
		} // for
		return false;
	} // hasImage()
} // class AACMappings()
