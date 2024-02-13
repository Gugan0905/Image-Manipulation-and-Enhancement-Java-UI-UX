package view;

import controller.InputCommandLine;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * ImageViewImpl is a class that extends the interface and has implementation for the GUI.
 */
public class ImageViewImpl extends JFrame implements ImageView, ActionListener,
    ListSelectionListener {

  private final ChartPanel chartPanel;
  private final JLabel comboboxDisplay;
  private final JLabel fileOpenDisplay;
  private final JLabel inputDisplay;
  private final JLabel prevInputDisplay;
  private final JLabel[] imageLabel;
  private InputCommandLine control;
  private final JComboBox<String> combobox2 = new JComboBox<>();
  private final Map<String, String> cmdHistory = new HashMap<>();
  private final List<String> options2 = new ArrayList<>();


  /**
   * Constructor of the class which initialized the GUI componenets of the program.
   */
  public ImageViewImpl() {
    super();

    setTitle("Image Manipulation and Enhancement");
    setSize(5000, 1000);

    JPanel mainPanel = new JPanel();
    //for elements to be arranged vertically within this panel
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
    //scroll bars around this main panel
    JScrollPane mainScrollPane = new JScrollPane(mainPanel);
    add(mainScrollPane);

    JPanel comboboxPanel = new JPanel();
    comboboxPanel.setBorder(BorderFactory.createTitledBorder("Manipulations"));
    comboboxPanel.setLayout(new BoxLayout(comboboxPanel, BoxLayout.PAGE_AXIS));
    mainPanel.add(comboboxPanel);

    comboboxDisplay = new JLabel("What would you like to do today?");
    comboboxPanel.add(comboboxDisplay);
    String[] options = {"Select an Option", "Load A New Image",
        "Save Current Image", "Greyscale", "Sepia",
        "Greyscale with Component",
        "Horizontal Flip", "Vertical Flip", "Bright/Dark-en",
        "RGB Split", "RGB Combine", "Blur", "Sharpen", "Dither"};
    JComboBox<String> combobox = new JComboBox<>();
    //the event listener when an option is selected
    combobox.setActionCommand("Image options");
    combobox.addActionListener(this);
    for (String option : options) {
      combobox.addItem(option);
    }

    comboboxPanel.add(combobox);

    //Selection lists
    JPanel selectionListPanel = new JPanel();
    selectionListPanel.setBorder(BorderFactory.createTitledBorder("Manipulation History"));
    selectionListPanel.setLayout(new BoxLayout(selectionListPanel, BoxLayout.X_AXIS));
    mainPanel.add(selectionListPanel);

    options2.add("-Select Visited Images-");
    //the event listener when an option is selected
    combobox2.setActionCommand("Visited Images");
    combobox2.addActionListener(this);
    for (String s : options2) {
      combobox2.addItem(s);
    }
    selectionListPanel.add(combobox2);

    //dialog boxes
    JPanel dialogBoxesPanel = new JPanel();
    dialogBoxesPanel.setBorder(BorderFactory.createTitledBorder("Charts"));
    dialogBoxesPanel.setLayout(new BoxLayout(dialogBoxesPanel, BoxLayout.PAGE_AXIS));
    mainPanel.add(dialogBoxesPanel);

    //show an image with a scrollbar
    JPanel imagePanel = new JPanel();
    //a border around the panel with a caption
    imagePanel.setBorder(BorderFactory.createTitledBorder("Showing current Image"));
    imagePanel.setLayout(new GridLayout(1, 0, 10, 10));
    //imagePanel.setMaximumSize(null);
    mainPanel.add(imagePanel);

    String[] images = {"res/Welcome.jpg"};
    imageLabel = new JLabel[images.length];
    JScrollPane[] imageScrollPane = new JScrollPane[images.length];

    for (int i = 0; i < imageLabel.length; i++) {
      imageLabel[i] = new JLabel();
      imageScrollPane[i] = new JScrollPane(imageLabel[i]);
      imageLabel[i].setIcon(new ImageIcon(images[i]));
      imageScrollPane[i].setPreferredSize(new Dimension(100, 300));
      imagePanel.add(imageScrollPane[i]);
    }

    //file open
    JPanel fileopenPanel = new JPanel();
    fileopenPanel.setLayout(new FlowLayout());
    selectionListPanel.add(fileopenPanel);
    fileOpenDisplay = new JLabel("File path will appear here");
    fileopenPanel.add(fileOpenDisplay);

    //JOptionsPane previous input dialog
    JPanel prevInputDialogPanel = new JPanel();
    prevInputDialogPanel.setLayout(new FlowLayout());
    selectionListPanel.add(prevInputDialogPanel);
    prevInputDisplay = new JLabel("Previous Reference Name will appear here");
    prevInputDialogPanel.add(prevInputDisplay);

    //JOptionsPane input dialog
    JPanel inputDialogPanel = new JPanel();
    inputDialogPanel.setLayout(new FlowLayout());
    selectionListPanel.add(inputDialogPanel);
    inputDisplay = new JLabel("Reference Name will appear here");
    inputDialogPanel.add(inputDisplay);

    XYSeriesCollection datasetValuesEmpty = new XYSeriesCollection();
    chartPanel = new ChartPanel(ChartFactory.createXYLineChart("Plot", "X-axis",
        "Y-axis", null));
    chartPanel.setMouseZoomable(true);
    dialogBoxesPanel.add(chartPanel);


  }

  @Override
  public void setControl(InputCommandLine control) {
    this.control = control;
  }

  private void callControl(String command) {
    if (inputDisplay.getText().equals("Reference Name will appear here")) {
      JOptionPane.showMessageDialog(ImageViewImpl.this,
          "No image has been loaded", "Error",
          JOptionPane.PLAIN_MESSAGE);
    } else {
      String[] srcReference = inputDisplay.getText().split(":");
      String destReference = JOptionPane.showInputDialog("Please enter the new Reference Name");
      prevInputDisplay.setText("Previous Reference Name:" + srcReference[srcReference.length - 1]);
      inputDisplay.setText("Reference Name:" + destReference);
      try {
        control.executeCommand(command + " "
            + srcReference[srcReference.length - 1]
            + " " + destReference, 0);
        cmdHistory.put(destReference, srcReference[srcReference.length - 1]
            + ":" + destReference);
        options2.add(destReference);
        combobox2.addItem(options2.get(options2.size() - 1));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private JFreeChart createChartPanel(XYSeriesCollection datasetValues) {
    String chartTitle = "Pixel Distribution";
    String xAxisLabel = "Pixel Value";
    String yAxisLabel = "Frequency";
    return ChartFactory.createXYLineChart(chartTitle, xAxisLabel, yAxisLabel,
        datasetValues);
  }

  @Override
  public void actionPerformed(ActionEvent action) {
    switch (action.getActionCommand()) {
      case "Image options": {
        if (action.getSource() instanceof JComboBox) {
          JComboBox<String> box = (JComboBox<String>) action.getSource();
          comboboxDisplay.setText("You selected: " + box.getSelectedItem());
          switch ((String) box.getSelectedItem()) {
            case "Load A New Image": {
              final JFileChooser fchooser = new JFileChooser(".");
              FileNameExtensionFilter filter = new FileNameExtensionFilter(
                  "JPG, PNG, PPM and BMP Images", "jpg", "png", "ppm", "bmp");
              fchooser.setFileFilter(filter);
              int retValue = fchooser.showOpenDialog(ImageViewImpl.this);
              if (retValue == JFileChooser.APPROVE_OPTION) {
                File f = fchooser.getSelectedFile();
                fileOpenDisplay.setText("Original File Name:" + f.getName());
                prevInputDisplay.setText("Previous Reference Name: -");
                String destReference = JOptionPane.showInputDialog(
                    "Please enter the Reference Name");
                inputDisplay.setText("Reference Name:" + destReference);
                try {
                  control.executeCommand("load "
                      + f.getAbsolutePath()
                      + " " + destReference, 0);
                  cmdHistory.put(destReference, f.getName()
                      + ":" + destReference);
                  options2.add(destReference);
                  combobox2.addItem(options2.get(options2.size() - 1));
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
              }
            }
            break;
            case "Save Current Image": {
              if (inputDisplay.getText().equals("Reference Name will appear here")) {
                JOptionPane.showMessageDialog(ImageViewImpl.this,
                    "No image has been loaded", "Error",
                    JOptionPane.PLAIN_MESSAGE);
              } else {
                final JFileChooser fchooser = new JFileChooser(".");
                int retValue = fchooser.showSaveDialog(ImageViewImpl.this);
                if (retValue == JFileChooser.APPROVE_OPTION) {
                  File f = fchooser.getSelectedFile();
                  String[] srcReference = inputDisplay.getText().split(":");
                  try {
                    control.executeCommand("save "
                        + f.getAbsolutePath()
                        + " " + srcReference[srcReference.length - 1], 0);
                  } catch (IOException e) {
                    throw new RuntimeException(e);
                  }
                }
              }
            }
            break;
            case "Greyscale": {
              callControl("greyscale");
            }
            break;
            case "Greyscale with Component": {
              String[] options = {"Luma", "Value", "Intensity", "Red", "Green", "Blue"};
              int retValue = JOptionPane.showOptionDialog(ImageViewImpl.this,
                  "Please choose component for greyscale", "Options",
                  JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                  options, options[0]);
              switch (retValue) {
                case 0: {
                  callControl("greyscale luma-component");
                }
                break;
                case 1: {
                  callControl("greyscale value-component");
                }
                break;
                case 2: {
                  callControl("greyscale intensity-component");
                }
                break;
                case 3: {
                  callControl("greyscale red-component");
                }
                break;
                case 4: {
                  callControl("greyscale green-component");
                }
                break;
                case 5: {
                  callControl("greyscale blue-component");
                }
                break;
                default: {
                  System.out.println("No such action");
                }
              }
            }
            break;
            case "Sepia": {
              callControl("sepia");
            }
            break;
            case "Horizontal Flip": {
              callControl("horizontal-flip");
            }
            break;
            case "Vertical Flip": {
              callControl("vertical-flip");
            }
            break;
            case "Bright/Dark-en": {
              if (inputDisplay.getText().equals("Reference Name will appear here")) {
                JOptionPane.showMessageDialog(ImageViewImpl.this,
                    "No image has been loaded", "Error",
                    JOptionPane.PLAIN_MESSAGE);
              } else {
                String[] srcReference = inputDisplay.getText().split(":");
                String destReference = JOptionPane.showInputDialog("Please enter "
                    + "the new Reference Name");
                prevInputDisplay.setText(
                    "Previous Reference Name:" + srcReference[srcReference.length - 1]);
                inputDisplay.setText("Reference Name:" + destReference);
                String increment = JOptionPane.showInputDialog("Enter the increment "
                    + "(Negative Integer for Darkening and Positive for Brightening");
                try {
                  control.executeCommand("brighten " + Integer.parseInt(increment)
                      + " "
                      + srcReference[srcReference.length - 1]
                      + " " + destReference, 0);
                  cmdHistory.put(destReference, srcReference[srcReference.length - 1]
                      + ":" + destReference);
                  options2.add(destReference);
                  combobox2.addItem(options2.get(options2.size() - 1));
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
              }
            }
            break;
            case "RGB Split": {
              if (inputDisplay.getText().equals("Reference Name will appear here")) {
                JOptionPane.showMessageDialog(ImageViewImpl.this,
                    "No image has been loaded", "Error",
                    JOptionPane.PLAIN_MESSAGE);
              } else {
                String[] srcReference = inputDisplay.getText().split(":");
                String destReferenceRed = JOptionPane.showInputDialog("Please enter "
                    + "the new Red Reference Name");
                String destReferenceGreen = JOptionPane.showInputDialog("Please enter "
                    + "the new Green Reference Name");
                String destReferenceBlue = JOptionPane.showInputDialog("Please enter "
                    + "the new Blue Reference Name");
                String destReference;
                String[] options = {"Blue", "Green", "Red"};
                int retValue = JOptionPane.showOptionDialog(ImageViewImpl.this,
                    "Please choose component to view", "Options",
                    JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                    options, options[0]);
                prevInputDisplay.setText("Previous Reference Name:"
                    + srcReference[srcReference.length - 1]);
                int type;
                if (retValue == 2) {
                  type = 0;
                  destReference = destReferenceRed;
                } else if (retValue == 0) {
                  type = 2;
                  destReference = destReferenceBlue;
                } else {
                  type = retValue;
                  destReference = destReferenceGreen;
                }
                inputDisplay.setText("Reference Name:" + destReference);

                try {
                  control.executeCommand("rgb-split "
                      + srcReference[srcReference.length - 1]
                      + " " + destReferenceRed
                      + " " + destReferenceGreen
                      + " " + destReferenceBlue, type);
                  cmdHistory.put(destReferenceRed, srcReference[srcReference.length - 1]
                      + ":" + destReferenceRed);
                  cmdHistory.put(destReferenceGreen, srcReference[srcReference.length - 1]
                      + ":" + destReferenceGreen);
                  cmdHistory.put(destReferenceBlue, srcReference[srcReference.length - 1]
                      + ":" + destReferenceBlue);
                  options2.add(destReferenceRed);
                  options2.add(destReferenceGreen);
                  options2.add(destReferenceBlue);
                  combobox2.addItem(options2.get(options2.size() - 1));
                  combobox2.addItem(options2.get(options2.size() - 2));
                  combobox2.addItem(options2.get(options2.size() - 3));
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
              }
            }
            break;
            case "RGB Combine": {
              if (inputDisplay.getText().equals("Reference Name will appear here")) {
                JOptionPane.showMessageDialog(ImageViewImpl.this,
                    "No image has been loaded", "Error",
                    JOptionPane.PLAIN_MESSAGE);
              } else {
                List combineOptions = options2.subList(1, options2.size());
                String destReference = JOptionPane.showInputDialog("Please enter "
                    + "the new Reference Name");
                int retValue1 = JOptionPane.showOptionDialog(ImageViewImpl.this,
                    "Choose 1st image to Combine", "Options",
                    JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                    combineOptions.toArray(), combineOptions.get(0));
                combineOptions.remove(retValue1);
                int retValue2 = JOptionPane.showOptionDialog(ImageViewImpl.this,
                    "Choose 2nd image to Combine", "Options",
                    JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                    combineOptions.toArray(), combineOptions.get(0));
                combineOptions.remove(retValue2);
                int retValue3 = JOptionPane.showOptionDialog(ImageViewImpl.this,
                    "Choose 3rd image to Combine", "Options",
                    JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                    combineOptions.toArray(), combineOptions.get(0));
                combineOptions.remove(retValue3);
                String srcReference1 = options2.get(retValue1);
                String srcReference2 = options2.get(retValue2);
                String srcReference3 = options2.get(retValue3);
                prevInputDisplay.setText("Previous Reference Name:"
                    + srcReference1);
                inputDisplay.setText("Reference Name:" + destReference);
                try {
                  control.executeCommand("rgb-combine "
                      + destReference
                      + " " + srcReference1
                      + " " + srcReference2
                      + " " + srcReference3, 0);
                  cmdHistory.put(destReference, srcReference1
                      + ":" + destReference);
                  cmdHistory.put(destReference, srcReference2
                      + ":" + destReference);
                  cmdHistory.put(destReference, srcReference3
                      + ":" + destReference);
                  options2.add(destReference);
                  combobox2.addItem(options2.get(options2.size() - 1));
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }

              }
            }
            break;
            case "Blur": {
              callControl("blur");
            }
            break;
            case "Sharpen": {
              callControl("sharpen");
            }
            break;
            case "Dither": {
              callControl("dither");
            }
            break;
            default: {
              System.out.println("No such action");
            }
          }
        }
      }
      break;
      case "Visited Images": {
        if (action.getSource() instanceof JComboBox) {
          JComboBox<String> box = (JComboBox<String>) action.getSource();
          String choice = (String) box.getSelectedItem();
          if (!(choice.equals("-Select Visited Images-"))) {
            imageLabel[0].setIcon(new ImageIcon(
                control.controlGetImage(choice)
            ));
            chartPanel.setChart(createChartPanel(control.controlGetHistogram(choice)));
            prevInputDisplay.setText("Previous Reference Name:-");
            inputDisplay.setText("Reference Name:-");

            String[] cmdGiven = (cmdHistory.get(choice)).split(":");
            if (cmdGiven[1].equals(choice)) {
              prevInputDisplay.setText("Previous Reference Name:" + cmdGiven[0]);
              inputDisplay.setText("Reference Name:" + cmdGiven[1]);
            }
          }
        }
      }
      break;
      default: {
        System.out.println(action.getActionCommand());
      }
    }
  }

  @Override
  public void setImage(BufferedImage image1) {
    imageLabel[0].setIcon(new ImageIcon(image1));
  }

  @Override
  public void setHistogram(XYSeriesCollection histogram) {
    chartPanel.setChart(createChartPanel(histogram));
  }

  @Override
  public void valueChanged(ListSelectionEvent e) {
    // We do not have any case where we need to listen to value changes during the execution
    // of the action. This is used for cases like Check Boxes where the user is actively informed
    // what value has been changed in their selection.
  }

  @Override
  public void showEnterCorrectArguments() {
    JOptionPane.showMessageDialog(ImageViewImpl.this,
        "Please Enter the Correct Arguments!", "Error",
        JOptionPane.PLAIN_MESSAGE);
  }

  @Override
  public void showCompleteMessage() {
    JOptionPane.showMessageDialog(ImageViewImpl.this,
        "Executed Successfully!", "Info!",
        JOptionPane.PLAIN_MESSAGE);
  }

  @Override
  public void showImageAttributes(int width, int height, int maxValue) {
    String str1 = "Width of image: " + width + "\n";
    String str2 = "Height of image: " + height + "\n";
    String str3 = "Maximum value of a color in this file (usually 255): " + maxValue + "\n";
    JOptionPane.showMessageDialog(ImageViewImpl.this,
        str1 + str2 + str3, "PPM File Info",
        JOptionPane.PLAIN_MESSAGE);
  }

  @Override
  public void showInvalidPPMFile() {
    JOptionPane.showMessageDialog(ImageViewImpl.this,
        "Invalid PPM file: plain RAW file should begin with P3", "Error",
        JOptionPane.PLAIN_MESSAGE);
  }

  @Override
  public void showInvalidFileLoaded() {
    JOptionPane.showMessageDialog(ImageViewImpl.this,
        "Invalid file type loaded. Accepted File types are " + control.getAccepted(), "Error",
        JOptionPane.PLAIN_MESSAGE);
  }
}
