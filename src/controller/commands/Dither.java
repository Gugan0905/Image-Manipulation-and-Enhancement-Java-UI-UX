package controller.commands;

import controller.Command;
import java.io.IOException;
import model.ImageManager;

/**
 * The Dither class is a Java implementation of the Command interface that dithers an image using an
 * ImageManager object.
 */
public class Dither implements Command {

  private final String srcName;
  private final String destName;

  /**
   * This is a constructor for the class to set values of the fields.
   *
   * @param srcName  the loaded image name in memory.
   * @param destName the name of transformed image to be stored in memory.
   */
  public Dither(String srcName,
      String destName) {
    this.destName = destName;
    this.srcName = srcName;
  }

  @Override
  public void goToModel(ImageManager manager) throws IOException {
    manager.toDither(srcName, destName);
  }
}
