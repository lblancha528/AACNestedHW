import java.util.NoSuchElementException;
import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KeyNotFoundException;
import edu.grinnell.csc207.util.NullKeyException;

/**
 * Represents the mappings for a single category of items that should
 * be displayed
 * 
 * @author Catie Baker & Lily Blanchard
 *
 */
public class AACCategory implements AACPage {

	/** The AssociativeArray where the imageLocation/text pairs are stored. */
	AssociativeArray<String, String> imgTxt;

	/** The name of the category. */
	String catName;

	/**
	 * Creates a new empty category with the given name
	 * @param name the name of the category
	 */
	public AACCategory(String name) {
		this.imgTxt = new AssociativeArray<String, String>();
		this.catName = name;
	} // AACCategory(String)
	
	/**
	 * Adds the image location, text pairing to the category
	 * @param imageLoc the location of the image
	 * @param text the text that image should speak
	 */
	public void addItem(String imageLoc, String text) {
		try {
			this.imgTxt.set(imageLoc, text);
		} catch (Exception NullKeyException) {
			System.err.println("Fail to set pair.");
		} // try/catch
		return;
	} // addItem(String, String)

	/**
	 * Returns an array of all the images in the category
	 * @return the array of image locations; if there are no images,
	 * it should return an empty array
	 */
	public String[] getImageLocs() {
		return this.imgTxt.getKeys();
	} // getImageLocs()

	/**
	 * Returns the name of the category
	 * @return the name of the category
	 */
	public String getCategory() {
		return this.catName;
	} // getCategory()

	/**
	 * Returns the text associated with the given image in this category
	 * @param imageLoc the location of the image
	 * @return the text associated with the image
	 * @throws NoSuchElementException if the image provided is not in the current
	 * 		   category
	 */
	public String select(String imageLoc) throws NoSuchElementException {
		try {
			return this.imgTxt.get(imageLoc);
		} catch (KeyNotFoundException e) {
			throw new NoSuchElementException();
		} // try/catch
	} // select(String)

	/**
	 * Determines if the provided images is stored in the category
	 * @param imageLoc the location of the category
	 * @return true if it is in the category, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		try {
			return this.imgTxt.hasKey(imageLoc);
		} catch (Exception NullKeyException) {
			System.err.println("Cannot find null key.");
			return false;
		} // try/catch
	} // hasImage(String)
} // class AACCategory
