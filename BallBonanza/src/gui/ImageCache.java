package gui;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 * ImageCache class
 * Loads buffered images and stores them for Frogger use.
 * @author Anders Nyberg, Erik Sundholm, Peter Spegel
 * @version %G%
 */
public class ImageCache {

	/*Constants*/
	private static int TRANS_RED = 0;
	private static int TRANS_GREEN = 255;
	private static int TRANS_BLUE = 0;
	
	/* Table of images. */
	private HashMap<String, BufferedImage> images;
	/* Table of animated sprites. */
	private HashMap<String, AnimatedSprite> sprites;

	/**
	 * ImageCache constructor
	 * Creates an empty image cache.
	 */
	public ImageCache() {
		images = new HashMap<String, BufferedImage> ();
		sprites = new HashMap<String, AnimatedSprite> ();
	}

	/**
	 * loadImage
	 * Loads an image from file and creates a BufferedImage.
	 * @param imageURL Image URL
	 * @return Image
	 */
	public BufferedImage loadImage(URL imageURL) {
		try {
			/* Read image from file and return it. */
			return ImageIO.read(imageURL);
		} catch (IOException e) {
			System.err.println("BallBonanza: Could not find image " + imageURL.toString());
			return null;
		} catch (IllegalArgumentException e) {
			System.err.println("BallBonanza: Could not read image " + imageURL.toString());
			return null;
		}
	}

	/**
	 * getImage
	 * Gets an image from file based on its filepath and angle of direction.
	 * @param imageURL Image filepath
	 * @return Image
	 */
	public BufferedImage getImage(String imageURL) {
		/* Get the image (load it if neccesary). */
		BufferedImage image = (BufferedImage) images.get(imageURL);
		if (image == null) {
			try {
				image = loadImage(new URI(imageURL).toURL());
			} catch (MalformedURLException e) {
				System.err.println("BallBonanza: Malformed URL");
				e.printStackTrace();
				image = null;
			} catch (URISyntaxException e) {
				System.err.println("BallBonanza: Unallowed URI syntax");
				e.printStackTrace();
				image = null;
			}
			
			if(image != null) {
				images.put(imageURL, image);
			}
		}
		return image;
	}
	
	public BufferedImage getTransparentImage(String imageURL) {
		/*Get the image (load it if nescessary)*/
		BufferedImage image = (BufferedImage) images.get(imageURL + "Transparent");
		if(image == null) {
			image = getImage(imageURL);
			/*If the image has been successfully loaded make a transparent version of the image*/
			if(image != null) {
				image = Transparency.makeColorTransparent(image, new Color(TRANS_RED, TRANS_GREEN, TRANS_BLUE));
				images.put(imageURL + "Transparent", image);
			}
		}
		return image;
	}
	
	public AnimatedSprite getAnimatedSprite(String imageURL, int rows, int columns) {
		/*Get the animated sprite (load it if nescessary)*/
		AnimatedSprite sprite = sprites.get(imageURL);
		if(sprite == null) {
			BufferedImage image = getTransparentImage(imageURL);
			if(image == null) {
				System.err.println("BallBonanza: Could not load sprite sheet\n");
				return null;
			}
			
			BufferedImage[] images = new BufferedImage[rows * columns]; 
			int count = 0;
			for(int i = 0; i < rows; i++) {
				for(int j = 0; j < columns; j++) {
					if(!imageURL.endsWith("BORDER.bmp"))
					{
						images[count] = image.getSubimage(j * image.getWidth() / columns, 
														  i * image.getHeight() / rows, 
														  image.getWidth() / columns, 
														  image.getHeight() / rows 
						);
					}
					else
					{
						images[count] = image.getSubimage(j * image.getWidth() / columns + 1, 
								  						  i * image.getHeight() / rows + 1, 
								  						  image.getWidth() / columns - (columns + 1), 
								  						  image.getHeight() / rows - (rows + 1) 
						);
					}
					//images[count] = image;
					//BufferedImage img = new BufferedImage(images[count].getWidth(), images[count].getHeight(), BufferedImage.TYPE_INT_ARGB);
					//img.setData(images[count].getData());
					//images[count] = img;
					
					//System.out.println(images[count].getType());
					//images[count] = new BufferedImage()
					//images[count] = Transparency.makeColorTransparent(ImageCache.ImageToBufferedImage(BufferedImage., images[count].getWidth(), images[count].getHeight()), new Color(TRANS_RED, TRANS_GREEN, TRANS_BLUE));
					
					count++;
				}
			}
			sprite = new AnimatedSprite(images, rows * columns);
			sprites.put(imageURL, sprite);
		}
		return sprite;
		
		/*BufferedImage image = getTransparentImage(imageURL);
		
		ImageProducer source = image.getSource();
		
		Image crop_image;
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				sprites[i] = image.getSubimage(j * image.getWidth(), i * image.getHeight(), image.getWidth(), image.getHeight());
				ImageFilter extractFilter = new CropImageFilter(j * image.getWidth(), i * image.getHeight(), image.getWidth(), image.getHeight());
		        ImageProducer producer = new FilteredImageSource(source,extractFilter);
		        crop_image = Toolkit.getDefaultToolkit().createImage(producer);
		        sprites[i] = ImageToBufferedImage(crop_image, crop_image.getWidth(null), crop_image.getHeight(null));
			}
		}*/
	}
	
	/*Function taken from eFreedom http://efreedom.com/Question/1-665406/Make-Color-Transparent-BufferedImage-Save-PNG*/
	public static BufferedImage ImageToBufferedImage(Image image, int width, int height)
	{
	    BufferedImage dest = new BufferedImage(
	        width, height, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = dest.createGraphics();
	    g2.drawImage(image, 0, 0, null);
	    g2.dispose();
	    return dest;
	}
	
	public static String getColorString(Color color) {
		if(color == Color.YELLOW) {
			return "Yellow";
		}
		if(color == Color.RED) {
			return "Red";
		}
		if(color == Color.GREEN) {
			return "Green";
		}
		if(color == Color.PINK) {
			return "Pink";
		}
		if(color == Color.MAGENTA) {
			return "Magenta";
		}
		if(color == Color.CYAN) {
			return "Cyan";
		}
		if(color == Color.ORANGE) {
			return "Orange";
		}
		if(color == Color.WHITE) {
			return "White";
		}
		
		return "Default";
	}
	
}
