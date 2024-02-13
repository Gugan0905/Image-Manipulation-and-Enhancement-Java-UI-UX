package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import controller.InputCommandLine;
import controller.InputCommandLineImpl;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.Scanner;
import javax.swing.JFrame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import view.ImageViewImpl;


/**
 * ImageManagerImplTest is a class that tests all the behaviours of code of model.
 */
public class ImageManagerImplTest {

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

  private String loadPPMTest(String path1) throws FileNotFoundException {
    Scanner sc;
    sc = new Scanner(new FileInputStream(path1));
    StringBuilder builder = new StringBuilder();
    //read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s).append(System.lineSeparator());
      }
    }
    return builder.toString().replace("\n", "").replace(" ", "");
  }

  private boolean checkClamp(String path1) throws FileNotFoundException {
    Scanner sc;
    sc = new Scanner(new FileInputStream(path1));
    StringBuilder builder = new StringBuilder();
    //read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s).append(System.lineSeparator());
      }
    }
    sc = new Scanner(builder.toString());

    String token;

    token = sc.next();
    if (!token.equals("P3")) {
      System.out.println("Invalid PPM Format");
    }
    int width = sc.nextInt();
    int height = sc.nextInt();
    int maxValue = sc.nextInt();
    if (maxValue > 255 || maxValue < 0) {
      return false;
    }

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int r = sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();
        if (r > 255 || r < 0) {
          return false;
        }
        if (g > 255 || g < 0) {
          return false;
        }
        if (b > 255 || b < 0) {
          return false;
        }
      }
    }
    return true;
  }

  @Test
  public void testLoad6() {
    Readable rd = new StringReader("load testres/goose-invalidLimit.ppm goose1\n"
        + "quit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("The given command is executed", output[output.length - 1]);
  }

  @Test
  public void testSave1() {
    Readable rd = new StringReader("save testres/goose1.ppm goose2\n"
        + "quit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testGray7() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "greyscale red-component goose-Nope goose-greyscale-red\n"
        + "greyscale blue-component goose-Nope goose-greyscale-red\n"
        + "greyscale green-component goose-Nope goose-greyscale-red\n"
        + "greyscale value-component goose-Nope goose-greyscale-red\n"
        + "greyscale luma-component goose-Nope goose-greyscale-red\n"
        + "greyscale intensity-component goose-Nope goose-greyscale-red\n"
        + "quit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testFlip3() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "horizontal-flip goose goose-horizontal\n"
        + "horizontal-flip goose-horizontal goose-2xhorizontal\n"
        + "save testres/goose-2xhoriztonal.ppm goose-2xhorizontal\n"
        + "quit");
    inp.run(rd);
    //assertEquals(loadPPMTest("res/goose.ppm"), loadPPMTest("testres/goose-2xhoriztonal.ppm"));
    String[] output = out.toString().split("\n");
    assertEquals("The given command is executed", output[output.length - 1]);
  }

  @Test
  public void testFlip4() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "vertical-flip goose goose-vertical\n"
        + "vertical-flip goose-vertical goose-2xvertical\n"
        + "save testres/goose-2xvertical.ppm goose-2xvertical\n"
        + "quit");
    inp.run(rd);
    //assertEquals(loadPPMTest("res/goose.ppm"), loadPPMTest("testres/goose-2xvertical.ppm"));
    String[] output = out.toString().split("\n");
    assertEquals("The given command is executed", output[output.length - 1]);
  }

  @Test
  public void testFlip5() throws IOException {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "vertical-flip goose goose-vertical\n"
        + "horizontal-flip goose-vertical goose-verthor\n"
        + "save testres/goose-verthor.ppm goose-verthor\n"
        + "horizontal-flip goose goose-horizontal\n"
        + "vertical-flip goose-horizontal goose-horvert\n"
        + "save testres/goose-horvert.ppm goose-horvert\n"
        + "quit");
    inp.run(rd);
    assertEquals(loadPPMTest("testres/goose-verthor.ppm"),
        loadPPMTest("testres/goose-horvert.ppm"));
  }

  @Test
  public void testFlip7() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "horizontal-flip goose-Nope goose-horizontal\n"
        + "vertical-flip goose-Nope goose-vertical\n"
        + "quit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testBright2() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "brighten 10 goose-Nope goose-brighter\n"
        + "brighten -10 goose-Nope goose-darker\n"
        + "quit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testBright4() throws IOException {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "brighten 1 goose goose-brighter\n"
        + "save testres/goose-brighter.ppm goose-brighter\n"
        + "quit");
    inp.run(rd);
    assertTrue(checkClamp("testres/goose-brighter.ppm"));
  }

  @Test
  public void testBright5() throws IOException {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "brighten 1000 goose goose-brighter\n"
        + "save testres/goose-brighter.ppm goose-brighter\n"
        + "quit");
    inp.run(rd);
    assertTrue(checkClamp("testres/goose-brighter.ppm"));
  }

  @Test
  public void testBright6() throws IOException {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "brighten -1 goose goose-darker\n"
        + "save testres/goose-darker.ppm goose-darker\n"
        + "quit");
    inp.run(rd);
    assertTrue(checkClamp("testres/goose-darker.ppm"));
  }

  @Test
  public void testBright7() throws IOException {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "brighten -1000 goose goose-darker\n"
        + "save testres/goose-darker.ppm goose-darker\n"
        + "quit");
    inp.run(rd);
    assertTrue(checkClamp("testres/goose-darker.ppm"));
  }

  @Test
  public void testBright8() throws IOException {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "brighten 0 goose goose-zerobrighter\n"
        + "save testres/goose-zerobrighter.ppm goose-zerobrighter\n"
        + "quit");
    inp.run(rd);
    assertEquals(loadPPMTest("res/goose.ppm"), loadPPMTest("testres/goose-zerobrighter.ppm"));
  }

  @Test
  public void testSplit2() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "rgb-split goose-Nope goose-red goose-green goose-blue\n"
        + "quit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testCombine2() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "rgb-split goose goose-red goose-green goose-blue\n"
        + "rgb-combine goose-combine goose-Nope goose-green goose-blue\n"
        + "quit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }
}