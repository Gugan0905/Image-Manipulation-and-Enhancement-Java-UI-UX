package controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import org.jfree.data.xy.XYSeriesCollection;
import view.ImageView;

/**
 * An InputCommandLine is an interface that contains all the functions which should be required for
 * the controller.
 */
public interface InputCommandLine {

  /**
   * The command line interaction to execute the command or script.
   *
   * @param rd the command line input from the user from the runner program. Can be System.in or the
   *           main args or from our tests.
   */
  void run(Readable rd);

  /**
   * executeCommand is a function that calls the execution of different image transformations.
   *
   * @param command the command to be executed.
   * @param ref     reference image to show.
   * @throws IllegalArgumentException if incorrect argument is given.
   * @throws IOException              if error while reading file.
   */
  void executeCommand(String command, int ref) throws IllegalArgumentException, IOException;

  /**
   * To initialize the view instance in controller.
   *
   * @param v view interface object.
   */
  void setView(ImageView v);

  /**
   * Helper method to access and return an image given its reference name.
   *
   * @param referenceName reference name to access the image.
   * @return Buffered Image.
   */
  BufferedImage controlGetImage(String referenceName);

  /**
   * Helper method to access and return the histogram data given its reference name.
   *
   * @param referenceName reference name to access the histogram data.
   * @return Histogram Dataset.
   */
  XYSeriesCollection controlGetHistogram(String referenceName);

  /**
   * Helper method to return accepted file types.
   *
   * @return String of accepted file types.
   */
  String getAccepted();
}
