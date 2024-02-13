package view;

/**
 * OutputLineImpl is a class that executes all the functionalities and extends the interface of
 * OutputLine.
 */
public class OutputLineImpl implements OutputLine {

  @Override
  public void printInstructions() {
    System.out.println("Enter your command one by one.\nEnter quit to exit the prompt.");
  }

  @Override
  public void printInput(String str) {
    System.out.println(str);
  }

  @Override
  public void printEnterCorrectArguments() {
    System.out.println("Enter the correct arguments");
  }

  @Override
  public void printFileNotFound(String fileName) {
    System.out.println("File " + fileName + " not found!");
  }

  @Override
  public void printInvalidPPMFile() {
    System.out.println("Invalid PPM file: plain RAW file should begin with P3");
  }

  @Override
  public void printImageAttributes(int width, int height, int maxValue) {
    System.out.println("Width of image: " + width);
    System.out.println("Height of image: " + height);
    System.out.println("Maximum value of a color in this file (usually 255): " + maxValue);
  }

}
