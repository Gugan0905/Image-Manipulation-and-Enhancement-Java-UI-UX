package model;

/**
 * ImageMetaDataImpl is a class that is used to represent the image.
 */
public class ImageMetaDataImpl implements ImageMetaData {

  private String token;
  private int width;
  private int height;
  private int maxValue;
  private int[][][] imageMatrix;

  /**
   * It is a constructor used to create an object of the image class.
   *
   * @param token       token of the image.
   * @param width       width of the image.
   * @param height      height of the image.
   * @param maxValue    maxvalue in the image.
   * @param imageMatrix the 3 channels of the image in array.
   */
  public ImageMetaDataImpl(String token, int width, int height, int maxValue,
      int[][][] imageMatrix) {
    this.token = token;
    this.width = width;
    this.height = height;
    this.maxValue = maxValue;
    this.imageMatrix = imageMatrix;
  }

  @Override
  public String getToken() {
    return token;
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public int getMaxValue() {
    return maxValue;
  }

  @Override
  public int[][][] getImageMatrix() {
    return imageMatrix;
  }

  @Override
  public void setToken(String token) {
    this.token = token;
  }

  @Override
  public void setWidth(int width) {
    this.width = width;
  }

  @Override
  public void setHeight(int height) {
    this.height = height;
  }

  @Override
  public void setMaxValue(int maxValue) {
    this.maxValue = maxValue;
  }

  @Override
  public void setImageMatrix(int[][][] imageMatrix) {
    this.imageMatrix = imageMatrix;
  }
}
