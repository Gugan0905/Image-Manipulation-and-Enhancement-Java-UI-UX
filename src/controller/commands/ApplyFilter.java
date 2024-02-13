package controller.commands;

import controller.Command;
import java.io.IOException;
import model.ImageManager;


/**
 * The ApplyFilter class is a command that applies a filter to an image using the ImageManager
 * object to blur/sharpen image.
 */
public class ApplyFilter implements Command {

  private final String srcName;
  private final String destName;
  private final int type;

  /**
   * This is a constructor for the class to set values of the fields.
   *
   * @param srcName  the loaded image name in memory.
   * @param destName the name of transformed image to be stored in the memory.
   * @param type     0-blur, 1-sharpen.
   */
  public ApplyFilter(String srcName,
      String destName, int type) {
    this.destName = destName;
    this.srcName = srcName;
    this.type = type;
  }

  @Override
  public void goToModel(ImageManager manager) throws IOException {
    manager.toApplyFilter(srcName, destName, type);
  }
}
