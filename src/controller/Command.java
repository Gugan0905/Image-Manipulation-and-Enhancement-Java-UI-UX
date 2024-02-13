package controller;

import java.io.IOException;
import model.ImageManager;

/**
 * Command is an interface that is used to implement all the other image manipulation commands.
 */
public interface Command {

  /**
   * go function is responsible for invoking the model object to call the necessary image
   * manipulation as per the input from the user.
   *
   * @param manager model object.
   * @throws IOException when ioexception error is encountered while handling images.
   */
  void goToModel(ImageManager manager) throws IOException;
}
