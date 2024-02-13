package model;

/**
 * An interface for all the image utilities expected.
 */
public interface ImageUtilInterface {


  /**
   * Takes an image object and generate a greyscale version of it by using its luma, value or
   * intensity (based on indicator) parameters.
   *
   * @param image the ImageMetaData that contains the pixel values of the image in PPM format.
   * @param type  the indicator for which parameter to be used. Luma=0, Value=1, Intensity=2.
   * @return ImageMetaData object that contains the new transformed image.
   */
  ImageMetaData lumaValueIntensityPPM(ImageMetaData image,
      int type);

  /**
   * Takes an image object  and generate a greyscale version of it by using its red, green or blue
   * (based on indicator) components.
   *
   * @param image the ImageMetaData that contains the pixel values of the image in PPM format.
   * @param type  the indicator for which component to be used. Red=0, Green=1, Blue=2.
   * @return ImageMetaData object that contains the new transformed image.
   */
  ImageMetaData componentSplitPPM(ImageMetaData
      image, int type);

  /**
   * Takes an image object  and generate a flipped version of it either horizontally or vertically
   * (based on indicator).
   *
   * @param image2 the ImageMetaData that contains the pixel values of the image in PPM format.
   * @param type   the indicator for which flip to be used. Horizontal=0, Vertical=1.
   * @return ImageMetaData object that contains the new transformed image.
   */
  ImageMetaData flipPPM(ImageMetaData
      image2, int type);

  /**
   * Takes an image object and generate a brighter or darker version of it.
   *
   * @param image     the ImageMetaData that contains the pixel values of the image in PPM format.
   * @param increment the pixel by which the image is made brighter or darker. Positive values for
   *                  brightening and negative for darkening.
   * @return ImageMetaData object that contains the new transformed image.
   */
  ImageMetaData brightDarkPPM(ImageMetaData
      image, int increment);

  /**
   * Takes three image object and combine them to form a combined tinted PPM image.
   *
   * @param red   ImageMetaData that contains the pixel values of red greyscale image in PPM
   *              format.
   * @param green ImageMetaData that contains the pixel values of green greyscale image in PPM
   *              format.
   * @param blue  ImageMetaData that contains the pixel values of blue greyscale image in PPM
   *              format.
   * @return ImageMetaData object that contains the new combined image.
   */
  ImageMetaData componentCombinePPM(ImageMetaData red, ImageMetaData green,
      ImageMetaData blue);

  /**
   * Blurs the given image using a filter.
   *
   * @param image input image object
   * @return new transformed image object.
   */
  ImageMetaData blur(ImageMetaData image);

  /**
   * Sharpens the given image using a filter.
   *
   * @param image input image object.
   * @return new transformed image object.
   */
  ImageMetaData sharpen(ImageMetaData image);

  /**
   * luma is used to create a greyscale version of the image using the linear transformation matrix
   * below.
   *
   * @param image input image object.
   * @return new transformed image object.
   */
  ImageMetaData luma(ImageMetaData image);

  /**
   * sepia is used to create a sepia version of the image using the linear transformation matrix
   * below.
   *
   * @param image input image object.
   * @return new transformed image object.
   */
  ImageMetaData sepia(ImageMetaData image);

  /**
   * dither is used to create a dithered dot matrix version of the image.
   *
   * @param image input image object.
   * @return new transformed image object.
   */
  ImageMetaData dither(ImageMetaData image);
}
