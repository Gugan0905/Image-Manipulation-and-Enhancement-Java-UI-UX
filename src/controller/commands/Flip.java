package controller.commands;

import controller.Command;
import java.io.IOException;
import model.ImageManager;

/**
 * The Flip class is a Java implementation of the Command interface that flips an image using the
 * ImageManager class.
 */
public class Flip implements Command {

  private final String srcName;
  private final String destName;
  private final int type;

  /**
   * This is a constructor for the class to set values of the fields.
   *
   * @param type     0-horizontal, 1-vertical.
   * @param srcName  the loaded image name in memory.
   * @param destName the name of transformed image to be stored in memory.
   */
  public Flip(int type, String srcName,
      String destName) {
    this.destName = destName;
    this.srcName = srcName;
    this.type = type;
  }

  @Override
  public void goToModel(ImageManager manager) throws IOException {
    manager.toFlip(type, srcName, destName);
  }
}
