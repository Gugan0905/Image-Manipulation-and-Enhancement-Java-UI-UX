package view;

import static org.junit.Assert.assertEquals;

import controller.InputCommandLine;
import controller.InputCommandLineImpl;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import javax.swing.JFrame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * ImageManagerImplTest is a class that tests all the behaviours of code of view.
 */
public class OutputLineImplTest {

  private final ByteArrayOutputStream out = new ByteArrayOutputStream();
  private final ByteArrayOutputStream err = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private final PrintStream originalErr = System.err;
  private final InputCommandLine inp = new InputCommandLineImpl();

  @Before
  public void setStreams() {
    System.setOut(new PrintStream(out));
    System.setErr(new PrintStream(err));
    ImageViewImpl view = new ImageViewImpl();
    view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    view.setVisible(false);
    inp.setView(view);
  }

  @After
  public void restoreInitialStreams() {
    System.setOut(originalOut);
    System.setErr(originalErr);
  }

  @Test
  public void testLoad5() {
    Readable rd = new StringReader("load testres/goose-invalidP6.ppm goose1\n"
        + "quit");

    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Invalid PPM file: plain RAW file should begin with P3", output[2]);
  }

  @Test
  public void testLoad6() {
    Readable rd = new StringReader("load res/goose.ppm goose1\n"
        + "quit");

    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("The given command is executed", output[output.length - 1]);
  }
}