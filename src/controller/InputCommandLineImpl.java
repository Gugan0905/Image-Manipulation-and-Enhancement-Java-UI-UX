package controller;

import controller.commands.ApplyFilter;
import controller.commands.Brighten;
import controller.commands.Dither;
import controller.commands.Flip;
import controller.commands.GreyscaleComp;
import controller.commands.GreyscaleLVI;
import controller.commands.GreyscaleSepia;
import controller.commands.Load;
import controller.commands.RGBCombine;
import controller.commands.RGBSplit;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import javax.imageio.ImageIO;
import model.ImageManager;
import model.ImageManagerImpl;
import model.ImageMetaData;
import model.ImageMetaDataImpl;
import org.jfree.data.xy.XYSeriesCollection;
import view.ImageView;
import view.OutputLine;
import view.OutputLineImpl;

/**
 * InputCommandLineImpl class is used to execute the command line from the terminal.
 */
public class InputCommandLineImpl implements InputCommandLine {

  /**
   * Image Manager is a class that stores the different transformation of images in memory as well
   * as calls all the functions from image utility helper class.
   */
  private final ImageManager manager = new ImageManagerImpl();
  private ImageView view;
  private final OutputLine outputLine = new OutputLineImpl();
  // list of accepted file types.
  private final List<String> accepted = new ArrayList<>(Arrays.asList(
      "ppm", "jpg", "bmp", "ppm"));

  @Override
  public void setView(ImageView v) {
    view = v;
    //provide view with all the callbacks
    view.setControl(this);
  }

  @Override
  public BufferedImage controlGetImage(String referenceName) {
    return this.manager.getImage(referenceName);
  }

  @Override
  public XYSeriesCollection controlGetHistogram(String referenceName) {
    return this.manager.getHistogram(referenceName);
  }

  /**
   * checkNumberOfArguments is a function that checks if the required number of arguments is present
   * in the given command. This function raises an IllegalArgumentException if mismatch in number of
   * arguments is found.
   *
   * @param commandArguments the arguments in the command.
   * @param number           the number of arguments required for the function.
   * @throws IllegalArgumentException when mismatch in number of arguments or a wrong command.
   */
  private void checkNumberOfArguments(String[] commandArguments, int number)
      throws IllegalArgumentException {
    if (commandArguments.length != number) {
      throw new IllegalArgumentException("Invalid number of arguments");
    }
  }

  /**
   * isNumeric is a function to check if the given string is numeric or not.
   *
   * @param strNum The string to be tested.
   * @return The method is returning a boolean value.
   */
  private boolean isNumeric(String strNum) {
    if (strNum == null) {
      return false;
    }
    try {
      Double.parseDouble(strNum);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  /**
   * getRgbFromBuff is a function to convert a BufferedImage into an RGB matrix with consists of
   * three elements for each rgb channel. Can be expanded for more channels as well.
   *
   * @param buffImage the buffered image read by the controller.
   * @return the image matrix format of the image contents.
   */
  private int[][][] getRgbFromBuff(BufferedImage buffImage) {
    int[][][] rgb = new int[3][buffImage.getHeight()][buffImage.getWidth()];
    int[][] r;
    int[][] g;
    int[][] b;
    int width;
    int height;

    width = buffImage.getWidth();
    height = buffImage.getHeight();

    r = new int[height][width];
    g = new int[height][width];
    b = new int[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        r[i][j] = (buffImage.getRGB(j, i) >> 16) & 0xFF;
        g[i][j] = (buffImage.getRGB(j, i) >> 8) & 0xFF;
        b[i][j] = (buffImage.getRGB(j, i) >> 0) & 0xFF;
      }
    }

    rgb[0] = r;
    rgb[1] = g;
    rgb[2] = b;

    return rgb;
  }

  private BufferedImage getBuffFromRGB(ImageMetaData image) {
    BufferedImage buffImage = new BufferedImage(image.getWidth(), image.getHeight(),
        BufferedImage.TYPE_INT_RGB);
    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        int r = image.getImageMatrix()[0][i][j];
        int g = image.getImageMatrix()[1][i][j];
        int b = image.getImageMatrix()[2][i][j];

        Color newColor = new Color(r, g, b);
        buffImage.setRGB(j, i, newColor.getRGB());
      }
    }
    return buffImage;
  }

  /**
   * Method to convert the stored ImageMetaData image into PPM form.
   *
   * @param image the ImageMetaData type image stored in the memory.
   * @return string builder of an image data from image object in ppm format.
   */
  private StringBuilder convert2PPM(ImageMetaData image) {

    StringBuilder ppm = new StringBuilder();

    String token;
    token = image.getToken();
    int width = image.getWidth();
    int height = image.getHeight();
    int maxValue = image.getMaxValue();
    ppm.append(token).append(System.lineSeparator());
    ppm.append(width);
    ppm.append(" ");
    ppm.append(height).append(System.lineSeparator());
    ppm.append(maxValue).append(System.lineSeparator());

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        ppm.append(image.getImageMatrix()[0][i][j]).append(System.lineSeparator());
        ppm.append(image.getImageMatrix()[1][i][j]).append(System.lineSeparator());
        ppm.append(image.getImageMatrix()[2][i][j]).append(System.lineSeparator());
      }
    }

    return ppm;
  }

  /**
   * Helper function to clamp all pixel values to the maximum and minimum of 255 and 0 resp.
   *
   * @param value the pixel value to be clamped.
   * @return the clamped pixel value.
   */
  private int clamp(int value) {
    return Math.max(0, Math.min(value, 255));
  }

  /**
   * Helper to check if the PPM file to be saved is in the correct format. The token must be P3.
   *
   * @param image the ImageMetaDataImpl storing the PPM image file.
   * @return the boolean result of whether the PPM is in the correct format.
   */
  private boolean checkFormat(ImageMetaData image) {
    return image.getToken().equals("P3");
  }

  /**
   * Read an image file in the PPM format and print the colors.
   *
   * @param fileName the path of the file.
   * @return StringBuilder variable that contains the pixel values of the image in PPM format.
   */
  private ImageMetaDataImpl readPPM(String fileName) {
    Scanner sc;

    try {
      sc = new Scanner(new FileInputStream(fileName));
    } catch (FileNotFoundException e) {
      outputLine.printFileNotFound(fileName);
      return null;
    }
    StringBuilder builder = new StringBuilder();
    //read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s).append(System.lineSeparator());
      }
    }
    //now set up the scanner to read from the string we just built
    sc = new Scanner(builder.toString());
    String token;

    token = sc.next();
    if (!token.equals("P3")) {
      outputLine.printInvalidPPMFile();
      view.showInvalidPPMFile();
    }
    int width = sc.nextInt();
    int height = sc.nextInt();
    int maxValue = sc.nextInt();

    int[][][] content = new int[3][height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int r = sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();
        content[0][i][j] = clamp(r);
        content[1][i][j] = clamp(g);
        content[2][i][j] = clamp(b);
      }
    }

    return new ImageMetaDataImpl(token, width, height, maxValue, content);
  }

  /**
   * Method to load (read) the image based on the file path and file name. This method allows
   * loading for ppm files and all files that can be loaded using buffered images. Currently, the
   * accepted file types can be found in the "accepted" private final parameter in the controller.
   *
   * @param filePath path of the file to be loaded.
   * @param fileName reference name to given to the file in the memory.
   * @return a Load class type object to facilitate the calling of the model.
   * @throws IOException when error is faced while handling image.
   */
  private Command controlLoad(String filePath, String fileName) throws IOException {
    String[] splitPath = filePath.split("\\.(?=[^\\.]+$)");
    BufferedImage buffImage;
    Command tempCmd = null;
    if (!(accepted.contains(splitPath[splitPath.length - 1]))) {
      view.showInvalidFileLoaded();
    } else {
      //For ppm type.
      if (splitPath[splitPath.length - 1].equals("ppm")) {
        ImageMetaDataImpl tempImg = readPPM(filePath);
        tempCmd = new Load(fileName, tempImg);
      } else {
        // For other "accepted" types.
        try {
          buffImage = ImageIO.read(new File(filePath));
        } catch (FileNotFoundException e) {
          outputLine.printFileNotFound(fileName);
          return null;
        }
        ImageMetaDataImpl tempImg = new ImageMetaDataImpl("P3", buffImage.getWidth(),
            buffImage.getHeight(), 255, getRgbFromBuff(buffImage));
        tempCmd = new Load(fileName, tempImg);
      }
    }
    return tempCmd;
  }

  private void controlSave(String filePath, String fileName) throws IOException {
    ImageMetaData tempImg = this.manager.toSave(fileName);
    if (tempImg == null) {
      throw new IllegalArgumentException("Image does not exist");
    }
    String[] splitPath = filePath.split("\\.(?=[^\\.]+$)");
    if (splitPath[splitPath.length - 1].equals("ppm")) {
      if (!checkFormat(tempImg)) {
        throw new IllegalArgumentException("The PPM file should be in P3 format");
      } else {
        File file = new File(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
          writer.append(convert2PPM(tempImg));
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    } else {
      File output = new File(filePath);
      ImageIO.write(getBuffFromRGB(tempImg),
          splitPath[splitPath.length - 1], output);
    }
  }

  /**
   * executeScript is a function that uses the execute function for a single command while parsing
   * through each line in the given script.
   *
   * @param command the run command with script name
   * @throws IOException if error while reading file.
   */
  private void executeScript(String command) throws IOException {
    String scriptPath = command.split(" ")[1];
    if (!new File(scriptPath).exists()) {
      throw new FileNotFoundException("The script given in " + scriptPath + " is not present.");
    } else {
      Scanner sc;
      sc = new Scanner(new FileInputStream(scriptPath));
      while (sc.hasNextLine()) {
        String commandInScript = sc.nextLine();
        if (commandInScript.length() > 0) {
          if (commandInScript.charAt(0) != '#') {
            if (commandInScript.charAt(0) == 'q' && commandInScript.charAt(1) == 'u'
                && commandInScript.charAt(2) == 'i' && commandInScript.charAt(3) == 't') {
              return;
            }
            executeCommand(commandInScript, 0);
          }
        }
      }
      System.out.println("Script Executed!");
    }
  }

  /**
   * execute is a function that is used to execute either a single command or a script file. If the
   * first word of the command is "run", then execute the script, otherwise execute the command.
   *
   * @param command The command to be executed.
   */
  private void execute(String command) throws IOException {
    if (command.split(" ")[0].equals("run")) {
      this.executeScript(command);
    } else {
      this.executeCommand(command, 0);
    }
  }

  /**
   * executeCommand is a function that calls the execution of different image transformations.
   *
   * @param command the command to be executed.
   * @param ref     reference image to show.
   * @throws IllegalArgumentException if incorrect argument is given.
   * @throws IOException              if error while reading file.
   */
  @Override
  public void executeCommand(String command, int ref) throws IllegalArgumentException, IOException {
    String[] commandArguments = command.split(" ");
    String action = commandArguments[0];
    Command cmd = null;
    List<String> referenceName = new ArrayList<>();
    // Switch case to handle different action
    switch (action) {
      case "load":
        checkNumberOfArguments(commandArguments, 3);
        cmd = controlLoad(commandArguments[1], commandArguments[2]);
        if (cmd == null) {
          break;
        }
        referenceName.add(commandArguments[2]);
        break;
      case "save":
        checkNumberOfArguments(commandArguments, 3);
        controlSave(commandArguments[1], commandArguments[2]);
        referenceName.add(commandArguments[2]);
        break;
      case "greyscale":
        if (commandArguments.length == 3) {
          checkNumberOfArguments(commandArguments, 3);
          cmd = new GreyscaleSepia(commandArguments[1], commandArguments[2], 0);
          referenceName.add(commandArguments[2]);
        } else if (commandArguments.length == 4) {
          String type = commandArguments[1];
          checkNumberOfArguments(commandArguments, 4);
          switch (type) {
            case "red-component":
              cmd = new GreyscaleComp(0, commandArguments[2], commandArguments[3]);
              referenceName.add(commandArguments[3]);
              break;
            case "green-component":
              cmd = new GreyscaleComp(1, commandArguments[2], commandArguments[3]);
              referenceName.add(commandArguments[3]);
              break;
            case "blue-component":
              cmd = new GreyscaleComp(2, commandArguments[2], commandArguments[3]);
              referenceName.add(commandArguments[3]);
              break;
            case "luma-component":
              cmd = new GreyscaleLVI(0, commandArguments[2], commandArguments[3]);
              referenceName.add(commandArguments[3]);
              break;
            case "value-component":
              cmd = new GreyscaleLVI(1, commandArguments[2], commandArguments[3]);
              referenceName.add(commandArguments[3]);
              break;
            case "intensity-component":
              cmd = new GreyscaleLVI(2, commandArguments[2], commandArguments[3]);
              referenceName.add(commandArguments[3]);
              break;
            default:
              throw new IllegalArgumentException(
                  "The greyscale type " + type + " is not supported.");
          }
        } else {
          throw new IllegalArgumentException("The greyscale type is not supported.");
        }
        break;
      case "sepia":
        checkNumberOfArguments(commandArguments, 3);
        cmd = new GreyscaleSepia(commandArguments[1], commandArguments[2], 1);
        referenceName.add(commandArguments[2]);
        break;
      case "horizontal-flip":
        checkNumberOfArguments(commandArguments, 3);
        cmd = new Flip(0, commandArguments[1], commandArguments[2]);
        referenceName.add(commandArguments[2]);
        break;
      case "vertical-flip":
        checkNumberOfArguments(commandArguments, 3);
        cmd = new Flip(1, commandArguments[1], commandArguments[2]);
        referenceName.add(commandArguments[2]);
        break;
      case "brighten":
        checkNumberOfArguments(commandArguments, 4);
        if (!this.isNumeric(commandArguments[1])) {
          throw new IllegalArgumentException("The threshold given should be an integer value");
        } else {
          int threshold = Integer.parseInt(commandArguments[1]);
          cmd = new Brighten(threshold, commandArguments[2], commandArguments[3]);
          referenceName.add(commandArguments[3]);
        }
        break;
      case "rgb-split":
        checkNumberOfArguments(commandArguments, 5);
        cmd = new RGBSplit(commandArguments[1], commandArguments[2], commandArguments[3],
            commandArguments[4]);
        referenceName.add(commandArguments[2]);
        referenceName.add(commandArguments[3]);
        referenceName.add(commandArguments[4]);
        break;
      case "rgb-combine":
        checkNumberOfArguments(commandArguments, 5);
        cmd = new RGBCombine(commandArguments[1], commandArguments[2], commandArguments[3],
            commandArguments[4]);
        referenceName.add(commandArguments[1]);
        break;
      case "blur":
        checkNumberOfArguments(commandArguments, 3);
        cmd = new ApplyFilter(commandArguments[1], commandArguments[2], 0);
        referenceName.add(commandArguments[2]);
        break;
      case "sharpen":
        checkNumberOfArguments(commandArguments, 3);
        cmd = new ApplyFilter(commandArguments[1], commandArguments[2], 1);
        referenceName.add(commandArguments[2]);
        break;
      case "dither":
        checkNumberOfArguments(commandArguments, 3);
        cmd = new Dither(commandArguments[1], commandArguments[2]);
        referenceName.add(commandArguments[2]);
        break;
      default:
        throw new IllegalArgumentException("The command " + action + " is not supported.");
    }
    if (cmd != null) {
      cmd.goToModel(this.manager);
      cmd = null;
      if (referenceName.size() > 0) {
        view.setImage(this.manager.getImage(referenceName.get(ref)));
        view.setHistogram(this.manager.getHistogram(referenceName.get(ref)));
      }
      //view.showCompleteMessage();
    }
  }

  /**
   * The command line interaction to execute the command or script.
   *
   * @param rd the command line input from the user from the runner program.
   */
  @Override
  public void run(Readable rd) {
    boolean terminate = false;
    Scanner sc = new Scanner(rd);
    outputLine.printInstructions();
    // Receiving input from the user till 'quit' is given
    while (!terminate) {
      if (!sc.hasNextLine()) {
        terminate = true;
      }
      while (sc.hasNextLine()) {
        String command = sc.nextLine();
        command = command.strip();
        if (command.equals("quit")) {
          terminate = true;
//          System.exit(0);
        } else {
          try {
            // Executing the command
            this.execute(command);
            outputLine.printInput("The given command is executed");
          } catch (IOException e) {
            outputLine.printInput(e.getMessage() + "Please check if the file exists.");
            //throw new RuntimeException(e);
          } catch (IllegalArgumentException iae) {
            outputLine.printInput(iae.getMessage());
            outputLine.printEnterCorrectArguments();
            //view.showEnterCorrectArguments();
          }
        }
      }
    }
  }

  @Override
  public String getAccepted() {
    return accepted.toString();
  }
}
