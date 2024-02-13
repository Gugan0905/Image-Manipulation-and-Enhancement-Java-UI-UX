package controller.commands;

import controller.Command;
import java.io.IOException;
import model.ImageManager;

/**
 * The GreyscaleComp class is a Java implementation of the Command interface that converts an image
 * to grayscale component using r/g/b and saves it as a new component.
 */
public class GreyscaleComp implements Command {

  private final String srcName;
  private final String destName;
  private final int type;

  /**
   * This is a constructor for the class to set values of the fields.
   *
   * @param type     0-red, 1-green, 2-blue
   * @param srcName  the loaded image name in memory.
   * @param destName the name of transformed image to be stored in memory.
   */
  public GreyscaleComp(int type, String srcName,
      String destName) {
    this.destName = destName;
    this.srcName = srcName;
    this.type = type;
  }

  @Override
  public void goToModel(ImageManager manager) throws IOException {
    manager.toGreyScaleComponent(type, srcName, destName);
  }
}
