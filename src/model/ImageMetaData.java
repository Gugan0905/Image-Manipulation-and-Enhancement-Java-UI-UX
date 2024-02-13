package model;

/**
 * ImageMetaData is an abstract interface that represents functionalities of an image.
 */
public interface ImageMetaData {

  /**
   * Getter method to return the token of the image.
   *
   * @return the String token.
   */
  String getToken();

  /**
   * Getter method to return the width of the image.
   *
   * @return the integer width.
   */
  int getWidth();

  /**
   * Getter method to return the height of the image.
   *
   * @return the integer height.
   */
  int getHeight();

  /**
   * Getter method to return the maxvalue of the image.
   *
   * @return the integer maxvalue.
   */
  int getMaxValue();

  /**
   * Getter method to return the image contents matrix of the image. components.
   *
   * @return the integer array of the image contents matrix.
   */
  int[][][] getImageMatrix();

  /**
   * Setter method to update the value of the token.
   *
   * @param token token value to be updated.
   */
  void setToken(String token);

  /**
   * Setter method to update the value of the width.
   *
   * @param width width value to be updated.
   */
  void setWidth(int width);

  /**
   * Setter method to update the value of the height.
   *
   * @param height height value to be updated.
   */
  void setHeight(int height);

  /**
   * Setter method to update the value of the maxValue.
   *
   * @param maxValue maxValue to be updated.
   */
  void setMaxValue(int maxValue);

  /**
   * Setter method to update the value of the imageMatrix.
   *
   * @param imageMatrix imageMatrix value to be updated.
   */
  void setImageMatrix(int[][][] imageMatrix);
}
