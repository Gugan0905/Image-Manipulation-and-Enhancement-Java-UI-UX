package controller.commands;

import controller.Command;
import java.io.IOException;
import model.ImageManager;

/**
 * The GreyscaleSepia class is a Java implementation of the Command interface that transforms an
 * image to either luma or sepia and stores it in memory using model object.
 */
public class GreyscaleSepia implements Command {

  private final String srcName;
  private final String destName;
  private final int type;

  /**
   * This is a constructor for the class to set values of the fields.
   *
   * @param srcName  the loaded image name in memory.
   * @param destName the name of transformed image to be stored in memory.
   * @param type     0-luma, 1-sepia.
   */
  public GreyscaleSepia(String srcName, String destName,
      int type) {
    this.destName = destName;
    this.srcName = srcName;
    this.type = type;
  }

  @Override
  public void goToModel(ImageManager manager) throws IOException {
    manager.toColorTransformation(srcName, destName, type);
  }
}
