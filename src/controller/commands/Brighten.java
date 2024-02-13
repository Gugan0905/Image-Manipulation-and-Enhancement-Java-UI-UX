package controller.commands;

import controller.Command;
import java.io.IOException;
import model.ImageManager;

/**
 * The Brighten class implements the Command interface and brightens an image by a specified
 * threshold using the model object.
 */
public class Brighten implements Command {

  private final String srcName;
  private final String destName;
  private final int threshold;

  /**
   * This is a constructor for the class to set values of the fields.
   *
   * @param threshold positive values to brighten, negative values to darken.
   * @param srcName   the loaded image name in memory.
   * @param destName  the name of transformed image to be stored in memory.
   */
  public Brighten(int threshold, String srcName,
      String destName) {
    this.destName = destName;
    this.srcName = srcName;
    this.threshold = threshold;
  }

  @Override
  public void goToModel(ImageManager manager) throws IOException {
    manager.toBrightenDarken(threshold, srcName, destName);
  }
}
