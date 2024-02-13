package model;

import java.awt.image.BufferedImage;
import java.io.IOException;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * ImageManager is an interface that has all the required functionalities that an image manager
 * model should have.
 */
public interface ImageManager {

  /**
   * toLoad function loads the given image and stores it to the hashmap.
   *
   * @param fileName name to be referred later.
   */
  void toLoad(String fileName, ImageMetaData image) throws IOException;


  /**
   * toSave function saves the transformed image in the hashmap to the given location.
   *
   * @param fileName the transformed image in the hashmap to be saved.
   */
  ImageMetaData toSave(String fileName);

  /**
   * toGreyScaleLumaValueIntensity function creates a grayscale version of the image. 0 - using
   * luma. 1 - using value. 2 - using intensity.
   *
   * @param type     number based on the preferred method above.
   * @param srcName  the image which should be transformed from the hashmap.
   * @param destName the name of the transformed image to be put in the hashmap.
   */
  void toGreyScaleLumaValueIntensity(int type, String srcName, String destName);

  /**
   * toGreyScaleLumaValueIntensity function creates a grayscale version of the image. 0 - using red.
   * 1 - using green. 2 - using blue.
   *
   * @param type     number based on the preferred method above.
   * @param srcName  the image which should be transformed from the hashmap.
   * @param destName the name of the transformed image to be put in the hashmap.
   */
  void toGreyScaleComponent(int type, String srcName, String destName);

  /**
   * toFlip function is used to flip the image. 0 - horizontal flip. 1 - vertical flip.
   *
   * @param type     number based on the preferred flip method above.
   * @param srcName  the image which should be transformed from the hashmap.
   * @param destName the name of the transformed image to be put in the hashmap.
   */
  void toFlip(int type, String srcName, String destName);

  /**
   * toBrightenDarken function is used to increase or decrease the brightness of the image. 0 -
   * horizontal flip. 1 - vertical flip.
   *
   * @param increment the increment/decrement threshold.
   * @param srcName   the image which should be transformed from the hashmap.
   * @param destName  the name of the transformed image to be put in the hashmap.
   */
  void toBrightenDarken(int increment, String srcName, String destName);

  /**
   * toRGBSplit function is used to split the rgb components of the image.
   *
   * @param srcName       the image which should be transformed from the hashmap.
   * @param destNameRed   the name of the transformed image of red component to be put in the
   *                      hashmap.
   * @param destNameGreen the name of the transformed image of green component to be put in the
   *                      hashmap.
   * @param destNameBlue  the name of the transformed image of blue component to be put in the
   *                      hashmap.
   */
  void toRGBSplit(String srcName, String destNameRed,
      String destNameGreen, String destNameBlue);

  /**
   * toRGBCombine function is used to combine three rgb components to a single image.
   *
   * @param destName     the name of the transformed image to be put in the hashmap.
   * @param srcNameRed   the name of the red component image in the hashmap.
   * @param srcNameGreen the name of the green component image in the hashmap.
   * @param srcNameBlue  the name of the blue component image in the hashmap.
   */
  void toRGBCombine(String destName, String srcNameRed,
      String srcNameGreen, String srcNameBlue);

  /**
   * toApplyFilter is a function that applies the given filter using convolution. 0 - Gaussian Blur.
   * 1 - Sharpening.
   *
   * @param srcName  the name of the source image to be got from the hashmap.
   * @param destName the name of the transformed image to be put in the hashmap.
   * @param type     the number based on the above notes.
   */
  void toApplyFilter(String srcName, String destName, int type);

  /**
   * toColorTransformation is a function that applies the given linear conversion of image in the
   * three channel. 0 - Luma greyscale. 1 - Sepia.
   *
   * @param srcName  the name of the source image to be got from the hashmap.
   * @param destName the name of the transformed image to be put in the hashmap.
   * @param type     the number based on the above notes.
   */
  void toColorTransformation(String srcName, String destName, int type);

  /**
   * toDither function applies the dithering effect on the image.
   *
   * @param srcName  the name of the source image to be got from the hashmap.
   * @param destName the name of the transformed image to be put in the hashmap.
   */
  void toDither(String srcName, String destName);

  /**
   * Method to return the Image to the Controller when requested with reference name.
   *
   * @param referenceName reference Name of the image.
   * @return the BufferedImage type image.
   */
  BufferedImage getImage(String referenceName);

  /**
   * Method to return the Histogram dataset to the Controller when requested with reference name.
   *
   * @param referenceName reference Name of the image.
   * @return the XYSeriesCollection dataset.
   */
  XYSeriesCollection getHistogram(String referenceName);

}
