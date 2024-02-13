package model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import org.jfree.data.xy.XYSeriesCollection;


/**
 * ImageManager is a class that executes all the image transformation functions from the imageUtil
 * class. The ImageManager class is called by the controller to execute the transformations. The
 * class is also responsible for maintain a hashmap that stores all the transformed images which
 * will be used for accessing or storing it to an image file.
 */
public class ImageManagerImpl implements ImageManager {

  //field for dictionary
  private final Map<String, HistogramMetaData> histogramData;
  private final Map<String, ImageMetaData> imageDictionary;
  private final ImageUtilInterface imageUtil = new ImageUtil();

  /**
   * ImageManagerImpl is a constructor that initializes the hashmap.
   */
  public ImageManagerImpl() {

    this.imageDictionary = new HashMap<>();
    this.histogramData = new HashMap<>();

  }

  private int[] flatten(ImageMetaData
      image, int comp) {
    int[] flat = new int[image.getHeight() * image.getWidth()];
    int k = 0;
    while (k < image.getHeight() * image.getWidth()) {
      for (int i = 0; i < image.getHeight(); i++) {
        for (int j = 0; j < image.getWidth(); j++) {
          flat[k] = image.getImageMatrix()[comp][i][j];
          k++;
        }
      }
    }
    return flat;
  }

  private Map<Integer, Integer> returnFreq(int[] list) {
    Map<Integer, Integer> frequencyMap = new HashMap<>();
    for (int s : list) {
      Integer count = frequencyMap.get(s);
      if (count == null) {
        count = 0;
      }
      frequencyMap.put(s, count + 1);
    }
    return frequencyMap;
  }

  private HistogramMetaData createHistogram(ImageMetaData
      image) {
    int[] redValues = flatten(image, 0);
    int[] greenValues = flatten(image, 1);
    int[] blueValues = flatten(image, 2);
    int[] intensityValues = flatten(imageUtil.lumaValueIntensityPPM(image, 2), 0);
    Map<Integer, Integer> redMap = returnFreq(redValues);
    Map<Integer, Integer> greenMap = returnFreq(greenValues);
    Map<Integer, Integer> blueMap = returnFreq(blueValues);
    Map<Integer, Integer> intensityMap = returnFreq(intensityValues);

    HistogramMetaData histogram = new HistogramMetaDataImpl();
    histogram.setDatasetSeries(redMap, "Red Comp");
    histogram.setDatasetSeries(greenMap, "Green Comp");
    histogram.setDatasetSeries(blueMap, "Blue Comp");
    histogram.setDatasetSeries(intensityMap, "Intensity Comp");

    return histogram;
  }

  @Override
  public BufferedImage getImage(String referenceName) {
    ImageMetaData
        image = this.imageDictionary.get(referenceName);

    BufferedImage buffImage = new BufferedImage(image.getWidth(), image.getHeight(),
        BufferedImage.TYPE_INT_RGB);
    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        int r = image.getImageMatrix()[0][i][j];
        int g = image.getImageMatrix()[1][i][j];
        int b = image.getImageMatrix()[2][i][j];

        Color newColor = new Color(r, g, b);
        buffImage.setRGB(j, i, newColor.getRGB());
      }
    }
    return buffImage;
  }

  /**
   * Method to return the Histogram dataset to the Controller when requested with reference name.
   *
   * @param referenceName reference Name of the image.
   * @return the XYSeriesCollection dataset.
   */
  @Override
  public XYSeriesCollection getHistogram(String referenceName) {
    HistogramMetaData histogram = this.histogramData.get(referenceName);
    return histogram.getDataset();
  }

  //method1 . To Load
  @Override
  public void toLoad(String fileName, ImageMetaData image) {
    this.imageDictionary.put(fileName, image);
    this.histogramData.put(fileName, createHistogram(image));
  }

  @Override
  public ImageMetaData toSave(String fileName) {
    return this.imageDictionary.get(fileName);
  }

  @Override
  public void toGreyScaleLumaValueIntensity(int type, String srcName, String destName) {
    if (this.imageDictionary.get(srcName) == null) {
      throw new IllegalArgumentException("Image does not exist in memory");
    }
    this.imageDictionary.put(destName,
        imageUtil.lumaValueIntensityPPM(this.imageDictionary.get(srcName), type));
    this.histogramData.put(destName, createHistogram(imageUtil.lumaValueIntensityPPM(
        this.imageDictionary.get(srcName), type)));

  }

  @Override
  public void toGreyScaleComponent(int type, String srcName, String destName) {
    if (this.imageDictionary.get(srcName) == null) {
      throw new IllegalArgumentException("Image does not exist in memory");
    }
    this.imageDictionary.put(destName,
        imageUtil.componentSplitPPM(this.imageDictionary.get(srcName), type));
    this.histogramData.put(destName, createHistogram(imageUtil.componentSplitPPM(
        this.imageDictionary.get(srcName), type)));
  }

  @Override
  public void toFlip(int type, String srcName, String destName) {
    if (this.imageDictionary.get(srcName) == null) {
      throw new IllegalArgumentException("Image does not exist in memory");
    }
    ImageMetaData
        tempImg = new ImageMetaDataImpl(this.imageDictionary.get(srcName).getToken(),
            this.imageDictionary.get(srcName).getWidth(),
            this.imageDictionary.get(srcName).getHeight(),
            this.imageDictionary.get(srcName).getMaxValue(),
            this.imageDictionary.get(srcName).getImageMatrix());
    this.imageDictionary.put(destName,
        imageUtil.flipPPM(tempImg, type));
    this.histogramData.put(destName, createHistogram(imageUtil.flipPPM(tempImg, type)));
  }

  @Override
  public void toBrightenDarken(int increment, String srcName, String destName) {
    if (this.imageDictionary.get(srcName) == null) {
      throw new IllegalArgumentException("Image does not exist in memory");
    }
    this.imageDictionary.put(destName,
        imageUtil.brightDarkPPM(this.imageDictionary.get(srcName),
            increment));
    this.histogramData.put(destName, createHistogram(imageUtil.brightDarkPPM(
        this.imageDictionary.get(srcName), increment)));

  }

  @Override
  public void toRGBSplit(String srcName, String destNameRed,
      String destNameGreen, String destNameBlue) {
    if (this.imageDictionary.get(srcName) == null) {
      throw new IllegalArgumentException("Image does not exist in memory");
    }
    this.imageDictionary.put(destNameRed,
        imageUtil.componentSplitPPM(this.imageDictionary.get(srcName), 0));
    this.histogramData.put(destNameRed, createHistogram(imageUtil.componentSplitPPM(
        this.imageDictionary.get(srcName), 0)));
    this.imageDictionary.put(destNameGreen, imageUtil.componentSplitPPM(
        this.imageDictionary.get(srcName), 1));
    this.histogramData.put(destNameGreen, createHistogram(imageUtil.componentSplitPPM(
        this.imageDictionary.get(srcName), 1)));
    this.imageDictionary.put(destNameBlue, imageUtil.componentSplitPPM(
        this.imageDictionary.get(srcName), 2));
    this.histogramData.put(destNameBlue, createHistogram(imageUtil.componentSplitPPM(
        this.imageDictionary.get(srcName), 2)));
  }

  @Override
  public void toRGBCombine(String destName, String srcNameRed,
      String srcNameGreen, String srcNameBlue) {
    if ((this.imageDictionary.get(srcNameRed) == null)
        || (this.imageDictionary.get(srcNameGreen) == null)
        || (this.imageDictionary.get(srcNameBlue) == null)) {
      throw new IllegalArgumentException("Image does not exist in memory");
    }
    this.imageDictionary.put(destName,
        imageUtil.componentCombinePPM(this.imageDictionary.get(srcNameRed),
            this.imageDictionary.get(srcNameGreen),
            this.imageDictionary.get(srcNameBlue)));
    this.histogramData.put(destName, createHistogram(imageUtil.componentCombinePPM(
        this.imageDictionary.get(srcNameRed),
            this.imageDictionary.get(srcNameGreen),
            this.imageDictionary.get(srcNameBlue))));

  }

  @Override
  public void toApplyFilter(String srcName, String destName, int type) {
    if (this.imageDictionary.get(srcName) == null) {
      throw new IllegalArgumentException("Image does not exist in memory");
    }
    if (type == 0) {
      this.imageDictionary.put(destName,
          imageUtil.blur(this.imageDictionary.get(srcName)));
      this.histogramData.put(destName, createHistogram(imageUtil.blur(
          this.imageDictionary.get(srcName))));
    } else if (type == 1) {
      this.imageDictionary.put(destName,
          imageUtil.sharpen(this.imageDictionary.get(srcName)));
      this.histogramData.put(destName, createHistogram(imageUtil.sharpen(
          this.imageDictionary.get(srcName))));
    }
  }

  @Override
  public void toColorTransformation(String srcName, String destName, int type) {
    if (this.imageDictionary.get(srcName) == null) {
      throw new IllegalArgumentException("Image does not exist in memory");
    }
    if (type == 0) {
      this.imageDictionary.put(destName,
          imageUtil.luma(this.imageDictionary.get(srcName)));
      this.histogramData.put(destName, createHistogram(imageUtil.luma(this.imageDictionary.get(
          srcName))));
    } else if (type == 1) {
      this.imageDictionary.put(destName,
          imageUtil.sepia(this.imageDictionary.get(srcName)));
      this.histogramData.put(destName, createHistogram(imageUtil.sepia(this.imageDictionary.get(
          srcName))));
    }
  }

  @Override
  public void toDither(String srcName, String destName) {
    if (this.imageDictionary.get(srcName) == null) {
      throw new IllegalArgumentException("Image does not exist in memory");
    }
    this.imageDictionary.put(destName,
        imageUtil.dither(this.imageDictionary.get(srcName)));
    this.histogramData.put(destName, createHistogram(imageUtil.dither(this.imageDictionary.get(
        srcName))));
  }

}
