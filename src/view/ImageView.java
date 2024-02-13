package view;

import controller.InputCommandLine;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * ImageView is an interface that has all the view functionalities for the GUI.
 */
public interface ImageView {

  /**
   * Sets the image to be displayed in the GUI.
   *
   * @param image loaded current image on memory.
   */
  void setImage(BufferedImage image);

  /**
   * Sets the histogram in the GUI with the current image.
   *
   * @param histogram histogram dataset.
   */
  void setHistogram(XYSeriesCollection histogram);

  /**
   * Initializes the controller instance in the view.
   *
   * @param control controller object.
   */
  void setControl(InputCommandLine control);

  /**
   * Calls GUI to inform user of Incorrect Arguments entered.
   */
  void showEnterCorrectArguments();

  /**
   * Calls GUI to inform user of successful execution of an operation.
   */
  void showCompleteMessage();

  /**
   * Calls GUI to inform user of the attributes and metadata of the given image.
   *
   * @param width    width of the given image.
   * @param height   height of the given image.
   * @param maxValue max pixel value of the given image.
   */
  void showImageAttributes(int width, int height, int maxValue);

  /**
   * Calls GUI to inform user of Invalid PPM format loaded.
   */
  void showInvalidPPMFile();

  /**
   * Calls GUI to inform user of Invalid file format loaded.
   */

  void showInvalidFileLoaded();

  /**
   * actionPerformed updates the gui after an action.
   * @param action action event.
   */
  void actionPerformed(ActionEvent action);
}
