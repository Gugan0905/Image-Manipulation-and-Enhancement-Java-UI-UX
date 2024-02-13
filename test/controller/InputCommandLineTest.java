package controller;

import static org.junit.Assert.assertEquals;

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
 * InputCommandLineTest is a class that tests all the behaviours of code from a user perspective.
 */
public class InputCommandLineTest {

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

  @Test
  public void testLoad1() {
    Readable rd = new StringReader("load res/goose.ppm goose1\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("The given command is executed", output[output.length - 1]);
  }

  @Test
  public void testLoad3() {
    Readable rd = new StringReader("load res/goose.ppm\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testLoad4() {
    Readable rd = new StringReader("laad res/goose.ppm goose1\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);

  }

  @Test
  public void testSave2() throws IOException {
    Readable rd = new StringReader("load res/goose.ppm goose1\n"
        + "save testres/goose1.ppm goose1\nquit");

    inp.run(rd);
    assertEquals(loadPPMTest("res/goose.ppm"), loadPPMTest("testres/goose1.ppm"));
  }

  @Test
  public void testSave3() {
    Readable rd = new StringReader("load res/goose.ppm goose1\nsave goose1\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testSave4() throws IOException {
    Readable rd = new StringReader("load res/goose.ppm goose1\n"
        + "save testres/goose1.ppm goose1\nquit");

    inp.run(rd);

    assertEquals(loadPPMTest("res/goose.ppm").charAt(0)
        + String.valueOf(loadPPMTest("res/goose.ppm").charAt(1)), "P3");
  }


  @Test
  public void testGray1() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "greyscale red-component goose goose-greyscale-red\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("The given command is executed", output[output.length - 1]);
  }

  @Test
  public void testGray2() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "greyscale green-component goose goose-greyscale-green\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("The given command is executed", output[output.length - 1]);
  }

  @Test
  public void testGray3() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "greyscale blue-component goose goose- greyscale-blue\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testGray4() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "greyscale value-component goose goose-greyscale-value\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("The given command is executed", output[output.length - 1]);
  }

  @Test
  public void testGray5() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "greyscale luma-component goose goose-greyscale-luma\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("The given command is executed", output[output.length - 1]);
  }

  @Test
  public void testGray6() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "greyscale intensity-component"
        + "goose goose-greyscale-intensity\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testGray8() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "greyscale red-component goose-greyscale-red\n"
        + "greyscale blue-component goose-greyscale-red\n"
        + "greyscale green-component goose-greyscale-red\n"
        + "greyscale value-component goose-greyscale-red\n"
        + "greyscale luma-component goose-greyscale-red\n"
        + "greyscale intensity-component goose-greyscale-red\n" + " quit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }


  @Test
  public void testGray9() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "greyscale orange-component goose-greyscale-orange\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testGray10() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "greyscale goose goose-greyscale-luma\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("The given command is executed", output[output.length - 1]);
  }

  @Test
  public void testSepia1() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "sepia goose goose-sepia\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("The given command is executed", output[output.length - 1]);
  }

  @Test
  public void testSepia2() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "sepia goose\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testSepia3() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "sepia goose-Nope goose-sepia\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testSepia4() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "sepiya goose goose-sepia\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testFlip1() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "horizontal-flip goose goose-horizontal\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("The given command is executed", output[output.length - 1]);
  }

  @Test
  public void testFlip2() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "vertical-flip goose goose-vertical\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("The given command is executed", output[output.length - 1]);
  }

  @Test
  public void testFlip6() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "hertizontal-flip goose goose-horizontal\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testFlip8() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "horizontal-flip goose-horizontal\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testFlip9() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "vertical-flip goose-vertical\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testBright1() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "brarkhten 10 goose goose-brighter\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testBright3() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "brighten 10 goose-brighter\nbrighten -10 goose-darker\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testSplit1() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "rgb-splat goose goose-red goose-green goose-blue\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testSplit3() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "rgb-split goose goose-red goose-blue\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testSplit4() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "rgb-split goose goose-red goose-green goose-blue\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("The given command is executed", output[output.length - 1]);
  }

  @Test
  public void testCombine1() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "rgb-split goose goose-red goose-green goose-blue\n"
        + "rgb-combi goose-combine goose-red goose-green goose-blue\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testCombine3() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "rgb-split goose goose-red goose-green goose-blue\n"
        + "rgb-combine goose-combine goose-green goose-blue\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testCombine4() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "rgb-split goose goose-red goose-green goose-blue\n"
        + "rgb-combine goose-combine goose-red goose-green goose-blue\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("The given command is executed", output[output.length - 1]);
  }

  @Test
  public void testBlur1() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "blur goose goose-blur\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("The given command is executed", output[output.length - 1]);
  }

  @Test
  public void testBlur2() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "blur goose\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testBlur3() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "blur goose-Nope goose-blur\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testBlur4() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "bloor goose goose-blur\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }


  @Test
  public void testSharp1() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "sharpen goose goose-sharpen\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("The given command is executed", output[output.length - 1]);
  }

  @Test
  public void testSharp2() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "sharpen goose\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testSharp3() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "sharpen goose-Nope goose-sharpen\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testSharp4() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "shilpen goose goose-sharpen\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testDither1() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "dither goose goose-dither\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("The given command is executed", output[output.length - 1]);
  }

  @Test
  public void testDither2() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "dither goose\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testDither3() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "dither goose-Nope goose-dither\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testDither4() {
    Readable rd = new StringReader("load res/goose.ppm goose\n"
        + "dieter goose goose-dither\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Enter the correct arguments", output[output.length - 1]);
  }

  @Test
  public void testRun1() {
    Readable rd = new StringReader("run res/scriptToTestAllMethods.txt\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("Script Executed!", output[output.length - 1]);
  }

  @Test
  public void testRun2() {
    Readable rd = new StringReader("run NOTscriptToTestAllMethods.txt\nquit");
    inp.run(rd);
    String[] output = out.toString().split("\n");
    assertEquals("The script given in NOTscriptToTestAllMethods.txt"
        + " is not present.Please check if the file exists.", output[output.length - 1]);
  }
}
