package view;

/**
 * OutputLine is an interface that has all the required functions of an output screen.
 */
public interface OutputLine {

  /**
   * printInstructions is a function that prints user-friendly welcome instructions.
   */
  void printInstructions();

  /**
   * PrintInput takes a String as an argument and prints that line to the user.
   *
   * @param str The string to print.
   */
  void printInput(String str);

  /**
   * Prints a message to the user to enter correct arguments.
   */
  void printEnterCorrectArguments();

  /**
   * Prints a message to the console that the file was not found.
   *
   * @param fileName The name of the file that was not found.
   */
  void printFileNotFound(String fileName);

  /**
   * Prints that the file format is invalid to the user.
   */
  void printInvalidPPMFile();

  /**
   * Prints the meta-data of an image to the user.
   *
   * @param width    the width of the image
   * @param height   the height of the image
   * @param maxValue The maximum value of a pixel in the image.
   */
  void printImageAttributes(int width, int height, int maxValue);
}
