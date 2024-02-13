package model;

import java.util.Map;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * HistogramMetaData is an interface that has the desired functionalities required when a histogram
 * should be implemented.
 */
public interface HistogramMetaData {

  /**
   * Method to return the histogram dataset.
   *
   * @return XYSeriesCollection type dataset of the pixel values.
   */
  XYSeriesCollection getDataset();

  /**
   * Method to add integer frequency map to the XYSeriesCollection dataset.
   *
   * @param map    the input map of frequencies of pixel values.
   * @param series the string name of the series.
   */
  void setDatasetSeries(Map<Integer, Integer> map, String series);
}
