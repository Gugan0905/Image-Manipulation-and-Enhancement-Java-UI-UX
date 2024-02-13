package controller.commands;

import controller.Command;
import java.io.IOException;
import model.ImageManager;

/**
 * The RGBCombine class is a command that combines red, green, and blue component images into a
 * single transformed image using model object.
 */
public class RGBCombine implements Command {

  private final String destName;
  private final String srcNameRed;
  private final String srcNameGreen;
  private final String srcNameBlue;

  /**
   * This is a constructor for the class to set values of the fields.
   *
   * @param destName     the name of transformed image to be stored in memory.
   * @param srcNameRed   the loaded red component image name in memory.
   * @param srcNameGreen the loaded green component image name in memory.
   * @param srcNameBlue  the loaded blue component image name in memory.
   */
  public RGBCombine(String destName, String srcNameRed, String srcNameGreen,
      String srcNameBlue) {
    this.srcNameRed = srcNameRed;
    this.srcNameGreen = srcNameGreen;
    this.srcNameBlue = srcNameBlue;
    this.destName = destName;
  }

  @Override
  public void goToModel(ImageManager manager) throws IOException {
    manager.toRGBCombine(destName, srcNameRed, srcNameGreen, srcNameBlue);
  }

}
