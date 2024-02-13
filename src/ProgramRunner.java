import controller.InputCommandLine;
import controller.InputCommandLineImpl;
import java.io.InputStreamReader;
import java.io.StringReader;
import javax.swing.JFrame;
import view.ImageViewImpl;

/**
 * ProgramRunner class is the point of contact for the user and the main function is executed by the
 * user inorder to give user command.
 */
public class ProgramRunner {

  /**
   * main function is executed by the user to start the program.
   *
   * @param args default value
   */
  public static void main(String[] args) {
    System.out.println("Welcome!");
    Readable rd = new InputStreamReader(System.in);
    InputCommandLine commandLineController = new InputCommandLineImpl();

    ImageViewImpl view = new ImageViewImpl();
    view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    view.setVisible(false);
    commandLineController.setView(view);

    if (args.length >= 1) {
      if (args[0].equals("-text")) {
        System.out.println("Running Command Prompt Mode...\n");
        commandLineController.run(rd);
      } else if (args[0].equals("-file")) {
        System.out.println("Running file " + args[1] + "...\n");
        Readable rd2 = new StringReader("run " + args[1]);
        commandLineController.run(rd2);
      }
    } else {
      view.setVisible(true);
      System.out.println("Running GUI('Gewey')...");
      commandLineController.setView(view);
    }
  }
}
