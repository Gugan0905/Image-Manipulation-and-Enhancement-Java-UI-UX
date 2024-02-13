package controller.commands;

import controller.Command;
import java.io.IOException;
import model.ImageManager;

/**
 * The RGBSplit class is a command that splits and image into red, green, and blue component
 * using model object.
 */
public class RGBSplit implements Command {

  private final String srcName;
  private final String destNameRed;
  private final String destNameGreen;
  private final String destNameBlue;

  /**
   * This is a constructor for the class to set values of the fields.
   *
   * @param srcName       the name of loaded image in memory.
   * @param destNameRed   the name of red component image to be stored in memory.
   * @param destNameGreen the name of green component image to be stored in memory.
   * @param destNameBlue  the name of blue component image to be stored in memory.
   */
  public RGBSplit(String srcName,
      String destNameRed, String destNameGreen, String destNameBlue) {
    this.destNameRed = destNameRed;
    this.destNameGreen = destNameGreen;
    this.destNameBlue = destNameBlue;
    this.srcName = srcName;
  }

  @Override
  public void goToModel(ImageManager manager) throws IOException {
    manager.toRGBSplit(srcName, destNameRed, destNameGreen, destNameBlue);
  }
}
