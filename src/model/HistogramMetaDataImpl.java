package model;

import java.util.Map;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * The HistogramMetaDataImpl class implements the HistogramMetaData interface and provides methods
 * to set and retrieve data for a histogram.
 */
public class HistogramMetaDataImpl implements HistogramMetaData {

  private final XYSeriesCollection datasetValues = new XYSeriesCollection();

  @Override
  public XYSeriesCollection getDataset() {
    return this.datasetValues;
  }

  @Override
  public void setDatasetSeries(Map<Integer, Integer> map, String series) {
    XYSeries tempSeries = new XYSeries(series);
    for (int key : map.keySet()) {
      tempSeries.add(key, map.get(key));
    }
    datasetValues.addSeries(tempSeries);
  }
}
