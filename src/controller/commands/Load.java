package controller.commands;

import controller.Command;
import java.io.IOException;
import model.ImageManager;
import model.ImageMetaData;
import model.ImageMetaDataImpl;

/**
 * The Load class is a Java implementation of the Command interface that loads an image file into
 * memory with its metadata using model object.
 */
public class Load implements Command {

  private final String fileName;
  private final ImageMetaData image;

  /**
   * This is a constructor for the class to set values of the fields.
   *
   * @param fileName the name of image to be stored in memory.
   * @param image    the image metadata.
   */
  public Load(String fileName, ImageMetaDataImpl image) {
    this.fileName = fileName;
    this.image = image;
  }

  @Override
  public void goToModel(ImageManager manager) throws IOException {
    manager.toLoad(fileName, image);
  }
}
