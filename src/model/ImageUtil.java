package model;

/**
 * This class contains utility methods to read a PPM image from file and simply print its contents.
 * It details all the methods used to perform the image manipulations and enhancements.
 */
class ImageUtil implements ImageUtilInterface {

  @Override
  public ImageMetaData lumaValueIntensityPPM(ImageMetaData
      image,
      int type) {
    if (type != 0 && type != 1 && type != 2) {
      throw new IllegalArgumentException("Enter the correct arguments");
    }

    String token;
    token = image.getToken();
    if (!token.equals("P3")) {
      throw new IllegalArgumentException("Invalid PPM file: plain RAW file should begin with P3");
    }
    int width = image.getWidth();
    int height = image.getHeight();
    int maxValue = image.getMaxValue();
    int[][][] content = new int[3][height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (type == 0) {
          int l = (int) ((0.2126 * image.getImageMatrix()[0][i][j])
              + (0.7152 * image.getImageMatrix()[1][i][j])
              + (0.0722 * image.getImageMatrix()[2][i][j]));
          content[0][i][j] = clamp(l);
          content[1][i][j] = clamp(l);
          content[2][i][j] = clamp(l);
        } else if (type == 1) {
          int v = Math.max(Math.max(image.getImageMatrix()[0][i][j],
                  image.getImageMatrix()[1][i][j]),
              image.getImageMatrix()[2][i][j]);
          content[0][i][j] = clamp(v);
          content[1][i][j] = clamp(v);
          content[2][i][j] = clamp(v);
        } else if (type == 2) {
          int intensity = (image.getImageMatrix()[0][i][j]
              + image.getImageMatrix()[1][i][j]
              + image.getImageMatrix()[2][i][j]) / 3;
          content[0][i][j] = clamp(intensity);
          content[1][i][j] = clamp(intensity);
          content[2][i][j] = clamp(intensity);
        } else {
          throw new IllegalArgumentException("Enter the correct arguments");
        }
      }
    }
    return new ImageMetaDataImpl(token, width, height, maxValue, content);
  }

  @Override
  public ImageMetaData componentSplitPPM(ImageMetaData image, int type) {
    if (type != 0 && type != 1 && type != 2) {
      throw new IllegalArgumentException("Enter the correct arguments");
    }

    String token;
    token = image.getToken();
    if (!token.equals("P3")) {
      throw new IllegalArgumentException("Invalid PPM file: plain RAW file should begin with P3");
    }
    int width = image.getWidth();
    int height = image.getHeight();
    int maxValue = image.getMaxValue();
    int[][][] content = new int[3][height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        content[0][i][j] = clamp(image.getImageMatrix()[type][i][j]);
        content[1][i][j] = clamp(image.getImageMatrix()[type][i][j]);
        content[2][i][j] = clamp(image.getImageMatrix()[type][i][j]);
      }
    }
    return new ImageMetaDataImpl(token, width, height, maxValue, content);
  }

  /**
   * Helper method to flip a given matrix horizontally or vertically based on the indicator.
   *
   * @param matrix the matrix to be flipped.
   * @param type   the indicator for which flip to be used. Horizontal=0, Vertical=1.
   * @return the flipped matrix.
   */
  private int[][] flipHelper(int[][] matrix, int type) {
    if (type == 0) {
      int temp;
      for (int i = 0; i < matrix.length; i++) {
        for (int j = 0; j < matrix[i].length / 2; j++) {
          temp = matrix[i][j];
          matrix[i][j] = matrix[i][matrix[i].length - 1 - j];
          matrix[i][matrix[i].length - 1 - j] = temp;
        }
      }
    } else if (type == 1) {
      int temp;
      for (int i = 0; i < matrix.length / 2; i++) {
        for (int j = 0; j < matrix[i].length; j++) {
          temp = matrix[i][j];
          matrix[i][j] = matrix[matrix.length - 1 - i][j];
          matrix[matrix.length - 1 - i][j] = temp;
        }
      }
    } else {
      throw new IllegalArgumentException("Invalid PPM file: plain RAW file should begin with P3");
    }
    return matrix;
  }

  @Override
  public ImageMetaData flipPPM(ImageMetaData
      image2, int type) {
    ImageMetaData
        image = new ImageMetaDataImpl(image2.getToken(), image2.getWidth(), image2.getHeight(),
            image2.getMaxValue(), image2.getImageMatrix());
    // type = 0 is horizontal, type = 1 is vertical.
    if (type != 0 && type != 1) {
      throw new IllegalArgumentException("Enter the correct arguments");
    }

    String token;
    token = image.getToken();
    if (!token.equals("P3")) {
      throw new IllegalArgumentException("Invalid PPM file: plain RAW file should begin with P3");
    }
    int width = image.getWidth();
    int height = image.getHeight();
    int maxValue = image.getMaxValue();
    int[][][] content = new int[3][height][width];
    int[][] redFlip;
    int[][] greenFlip;
    int[][] blueFlip;
    int[][] redMatrix = new int[height][width];
    int[][] greenMatrix = new int[height][width];
    int[][] blueMatrix = new int[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        redMatrix[i][j] = image.getImageMatrix()[0][i][j];
        greenMatrix[i][j] = image.getImageMatrix()[1][i][j];
        blueMatrix[i][j] = image.getImageMatrix()[2][i][j];
      }
    }

    redFlip = flipHelper(redMatrix, type);
    greenFlip = flipHelper(greenMatrix, type);
    blueFlip = flipHelper(blueMatrix, type);

    content[0] = redFlip;
    content[1] = greenFlip;
    content[2] = blueFlip;

    return new ImageMetaDataImpl(token, width, height, maxValue, content);
  }

  @Override
  public ImageMetaData brightDarkPPM(ImageMetaData image, int increment) {
    String token;
    token = image.getToken();
    if (!token.equals("P3")) {
      throw new IllegalArgumentException("Invalid PPM file: plain RAW file should begin with P3");
    }
    int width = image.getWidth();
    int height = image.getHeight();
    int maxValue = image.getMaxValue();
    int[][][] content = new int[3][height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        content[0][i][j] = clamp(image.getImageMatrix()[0][i][j] + increment);
        content[1][i][j] = clamp(image.getImageMatrix()[1][i][j] + increment);
        content[2][i][j] = clamp(image.getImageMatrix()[2][i][j] + increment);
      }
    }
    return new ImageMetaDataImpl(token, width, height, maxValue, content);
  }


  @Override
  public ImageMetaData componentCombinePPM(ImageMetaData red, ImageMetaData green,
      ImageMetaData blue) {

    String tokenR;
    String tokenG;
    String tokenB;

    tokenR = red.getToken();
    tokenG = green.getToken();
    tokenB = blue.getToken();
    if (!tokenR.equals("P3") || !tokenG.equals("P3") || !tokenB.equals("P3")) {
      throw new IllegalArgumentException("Invalid PPM file: plain RAW file should begin with P3");
    }

    int width = Math.max(Math.max(red.getWidth(), green.getWidth()), blue.getWidth());
    int height = Math.max(Math.max(red.getHeight(), green.getHeight()), blue.getHeight());
    int maxValue = Math.max(Math.max(red.getMaxValue(), green.getMaxValue()), blue.getMaxValue());
    int[][][] content = new int[3][height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        content[0][i][j] = clamp(red.getImageMatrix()[0][i][j]);
        content[1][i][j] = clamp(green.getImageMatrix()[1][i][j]);
        content[2][i][j] = clamp(blue.getImageMatrix()[2][i][j]);
      }
    }

    return new ImageMetaDataImpl(tokenR, width, height, maxValue, content);
  }

  /**
   * Helper function to clamp all pixel values to the maximum and minimum of 255 and 0 resp.
   *
   * @param value the pixel value to be clamped.
   * @return the clamped pixel value.
   */
  private int clamp(int value) {
    return Math.max(0, Math.min(value, 255));
  }


  @Override
  public ImageMetaData blur(ImageMetaData
      image) {
    int[][][] imageMatrix = image.getImageMatrix();

    float[][] filter = {{1 / 16f, 1 / 8f, 1 / 16f}, {1 / 8f, 1 / 4f, 1 / 8f},
        {1 / 16f, 1 / 8f, 1 / 16f}};
    int[][] blurredRedMatrix = convolve(imageMatrix[0], filter);
    int[][] blurredGreenMatrix = convolve(imageMatrix[1], filter);
    int[][] blurredBlueMatrix = convolve(imageMatrix[2], filter);
    int[][][] blurredImage = new int[][][]{blurredRedMatrix, blurredGreenMatrix, blurredBlueMatrix};

    return new ImageMetaDataImpl(image.getToken(), image.getWidth(), image.getHeight(),
        image.getMaxValue(), blurredImage);

  }

  @Override
  public ImageMetaData sharpen(ImageMetaData
      image) {
    int[][][] imageMatrix = image.getImageMatrix();
    float[][] filter = {{-1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f},
        {-1 / 8f, 1 / 4f, 1 / 4f, 1 / 4f, -1 / 8f},
        {-1 / 8f, 1 / 4f, 1f, 1 / 4f, -1 / 8f},
        {-1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f},
        {-1 / 8f, 1 / 4f, 1 / 4f, 1 / 4f, -1 / 8f}
    };

    int[][] sharpenedRedMatrix = convolve(imageMatrix[0], filter);
    int[][] sharpenedGreenMatrix = convolve(imageMatrix[1], filter);
    int[][] sharpenedBlueMatrix = convolve(imageMatrix[2], filter);
    int[][][] sharpenedImage = new int[][][]{sharpenedRedMatrix, sharpenedGreenMatrix,
        sharpenedBlueMatrix};

    return new ImageMetaDataImpl(image.getToken(), image.getWidth(), image.getHeight(),
        image.getMaxValue(), sharpenedImage);

  }

  /**
   * Helper method to apply convolution operation of a filter on a matrix. Involves calling a
   * padding helper method.
   *
   * @param imageMatrix matrix to apply convolution.
   * @param filter      given filter.
   * @return the convolved matrix.
   */
  private int[][] convolve(int[][] imageMatrix, float[][] filter) {
    int rowLength = imageMatrix.length;
    int columnLength = imageMatrix[0].length;
    int rowPadLength = filter.length - 1;
    int colPadLength = filter[0].length - 1;
    int[][] transformedMatrix = new int[rowLength][columnLength];

    int[][] paddedMatrix = padHelper(imageMatrix, rowPadLength, colPadLength);
    for (int i = 0; i < rowLength; i++) {
      for (int j = 0; j < columnLength; j++) {
        float accumulator = 0;
        for (int k = 0; k < filter.length; k++) {
          for (int l = 0; l < filter[0].length; l++) {
            accumulator += filter[k][l] * paddedMatrix[k + i][l + j];
          }
        }
        transformedMatrix[i][j] = clamp((int) accumulator);
      }
    }
    return transformedMatrix;
  }

  /**
   * Simple helper method to adding zero padding to a given matrix.
   *
   * @param imageMatrix  matrix to be padded.
   * @param rowPadLength length of padding in row.
   * @param colPadLength length of padding in column.
   * @return padded matrix.
   */
  private int[][] padHelper(int[][] imageMatrix, int rowPadLength, int colPadLength) {
    int rowLength = imageMatrix.length;
    int columnLength = imageMatrix[0].length;
    int[][] paddedMatrix = new int[rowLength + rowPadLength][columnLength + colPadLength];

    for (int i = 0; i < paddedMatrix.length; i++) {
      for (int j = 0; j < paddedMatrix[0].length; j++) {
        if (i < rowPadLength || i > rowLength + rowPadLength - 1) {
          paddedMatrix[i][j] = 0;
        } else if (j < colPadLength || j > columnLength + colPadLength - 1) {
          paddedMatrix[i][j] = 0;
        } else {
          paddedMatrix[i][j] = imageMatrix[i - rowPadLength][j - colPadLength];
        }
      }
    }
    return paddedMatrix;
  }

  @Override
  public ImageMetaData luma(ImageMetaData
      image) {
    float[][] filter = new float[][]{{0.2126f, 0.7152f, 0.0722f},
        {0.2126f, 0.7152f, 0.0722f},
        {0.2126f, 0.7152f, 0.0722f}};
    int[][][] colorTransformedImage = colorTransformation(image.getImageMatrix(), filter);

    return new ImageMetaDataImpl(image.getToken(), image.getWidth(), image.getHeight(),
        image.getMaxValue(), colorTransformedImage);
  }

  @Override
  public ImageMetaData sepia(ImageMetaData
      image) {
    float[][] filter = new float[][]{{0.393f, 0.769f, 0.189f},
        {0.349f, 0.686f, 0.168f},
        {0.272f, 0.534f, 0.131f}};
    int[][][] colorTransformedImage = colorTransformation(image.getImageMatrix(), filter);
    return new ImageMetaDataImpl(image.getToken(), image.getWidth(), image.getHeight(),
        image.getMaxValue(), colorTransformedImage);
  }

  /**
   * Method to generically apply a linear transformation to a given image matrix.
   *
   * @param imageMatrix          the matrix to be edited.
   * @param linearTransformation the given linear transformation.
   * @return transformed image based on the filter.
   */
  private int[][][] colorTransformation(int[][][] imageMatrix,
      float[][] linearTransformation) {

    int noChannels = imageMatrix.length;
    int noRows = imageMatrix[0].length;
    int noCols = imageMatrix[0][0].length;
    int[][][] transformedMatrix = new int[noChannels][noRows][noCols];
    for (int i = 0; i < imageMatrix[0].length; i++) {
      for (int j = 0; j < imageMatrix[0][0].length; j++) {
        int[] colorInput = new int[]{imageMatrix[0][i][j], imageMatrix[1][i][j],
            imageMatrix[2][i][j]};
        int[] colorOutput = new int[noChannels];
        for (int x = 0; x < linearTransformation.length; x++) {
          float accumulator = 0;
          for (int y = 0; y < linearTransformation[0].length; y++) {
            accumulator += linearTransformation[x][y] * colorInput[y];
          }
          colorOutput[x] = clamp((int) accumulator);
        }
        for (int k = 0; k < noChannels; k++) {
          transformedMatrix[k][i][j] = colorOutput[k];
        }
      }
    }
    return transformedMatrix;
  }

  @Override
  public ImageMetaData dither(ImageMetaData
      image) {
    int[][][] ditheredImageMatrix = ditherHelper(image);
    return new ImageMetaDataImpl(image.getToken(), image.getWidth(),
        image.getHeight(), image.getMaxValue(),
        ditheredImageMatrix);
  }

  private int[][][] ditherHelper(ImageMetaData
      image) {
    int[][][] imageMatrix = image.getImageMatrix();
    int noChannels = imageMatrix.length;
    int noRows = imageMatrix[0].length;
    int noCols = imageMatrix[0][0].length;
    int[][][] transformedMatrix = this.luma(image).getImageMatrix();
    int[][][] paddedMatrix = new int[noChannels][noRows][noCols];
    for (int k = 0; k < noChannels; k++) {
      paddedMatrix[k] = padHelper(imageMatrix[k], 2, 2);
    }

    int[][] paddedRedChannel = paddedMatrix[0];
    System.out.println("Rows" + paddedRedChannel.length);
    System.out.println("Columns" + paddedRedChannel[0].length);
    System.out.println("Trans Rows" + transformedMatrix[0].length);
    System.out.println("Trans Columns" + transformedMatrix[0][0].length);
    for (int i = 0; i < transformedMatrix[0].length; i++) {
      for (int j = 0; j < transformedMatrix[0][0].length; j++) {
        int oldColor = paddedRedChannel[i + 1][j + 1];
        int diff255 = 255 - oldColor;
        int newColor = 0;
        if (diff255 < oldColor) {
          newColor = 255;
        }
        for (int k = 0; k < noChannels; k++) {
          paddedRedChannel[i + 1][j + 1] = newColor;
        }
        int error = oldColor - newColor;
        paddedRedChannel[i + 1][j + 2] += (int) ((7. / 16) * error);
        paddedRedChannel[i + 2][j] += (int) ((3. / 16) * error);
        paddedRedChannel[i + 2][j + 1] += (int) ((5. / 16) * error);
        paddedRedChannel[i + 2][j + 2] += (int) ((1. / 16) * error);
      }
    }
    for (int i = 0; i < transformedMatrix[0].length; i++) {
      for (int j = 0; j < transformedMatrix[0][0].length; j++) {
        for (int k = 0; k < noChannels; k++) {
          transformedMatrix[k][i][j] = paddedRedChannel[i + 1][j + 1];
        }
      }
    }
    return transformedMatrix;
  }
}

