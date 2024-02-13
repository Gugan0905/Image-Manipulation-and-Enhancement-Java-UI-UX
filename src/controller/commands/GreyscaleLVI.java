package controller.commands;

import controller.Command;
import java.io.IOException;
import model.ImageManager;

/**
 * The GreyscaleLVI class is a Java implementation of the Command interface that converts an image
 * to greyscale using the Luma/Value/Intensity method with help of model object.
 */
public class GreyscaleLVI implements Command {

  private final String srcName;
  private final String destName;
  private final int type;

  /**
   * This is a constructor for the class to set values of the fields.
   *
   * @param type     0-luma, 1-value, 2-intensity.
   * @param srcName  the loaded image name in memory.
   * @param destName the name of transformed image to be stored in memory.
   */
  public GreyscaleLVI(int type, String srcName,
      String destName) {
    this.destName = destName;
    this.srcName = srcName;
    this.type = type;
  }

  @Override
  public void goToModel(ImageManager manager) throws IOException {
    manager.toGreyScaleLumaValueIntensity(type, srcName, destName);
  }
}
