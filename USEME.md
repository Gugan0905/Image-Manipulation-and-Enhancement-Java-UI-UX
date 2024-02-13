## IME: Image Manipulation and Enhancement

###  Teammates
* Maria Anson - NUID 002931419
* Gugan Kathiresan - NUID 002756523


### Supported Types

#### To Run Script File
    java -jar Program.jar -file path-of-script-file
#### To Run on Command Prompt
    java -jar Program.jar -text 
#### To Start GUI
    java -jar Program.jar 

## GUI
Pane 1 - Contains different image manipulation option
Pane 2 - Shows the image name, reference name and the previous reference image
Pane 3 - Shows the Histogram
Pane 4 - The current image

### Supported Commands

* load 
  * Syntax - load src-file-path reference-name
  * Example - load res/goose.ppm goose
  * Conditions - 
    * Load command must be executed first before referring to the image in any other 
    operation.

* save
    * Syntax - save destination-file-path reference-name
    * Example - save res/goose-Final.ppm goose
    * Conditions -
        * The reference file name must be loaded before saving.
* greyscale
  * Syntax - greyscale original-reference-name destination-reference-name
  * Example - greyscale goose goose-greyscale
  * Conditions -
    * If reference name given as the next argument after greyscale, default operation is luma.
* greyscale red-component
  * Syntax - greyscale red-component original-reference-name destination-reference-name
  * Example - greyscale red-component goose goose-greyscale
  * Conditions -
      * If red-component given as the next argument after greyscale, default operation is red.
* greyscale green-component
  * Syntax - greyscale green-component original-reference-name destination-reference-name
  * Example - greyscale green-component goose goose-greyscale
  * Conditions -
      * If green-component given as the next argument after greyscale, default operation is green.
* greyscale blue-component
    * Syntax - greyscale blue-component original-reference-name destination-reference-name
    * Example - greyscale blue-component goose goose-greyscale
    * Conditions -
        * If blue-component given as the next argument after greyscale, default operation is blue.
* greyscale luma-component
    * Syntax - greyscale luma-component original-reference-name destination-reference-name
    * Example - greyscale luma-component goose goose-greyscale
    * Conditions -
        * If luma-component given as the next argument after greyscale, default operation is luma.
* greyscale value-component
    * Syntax - greyscale value-component original-reference-name destination-reference-name
    * Example - greyscale value-component goose goose-greyscale
    * Conditions -
        * If value-component given as the next argument after greyscale, default operation value.
* greyscale intensity-component
    * Syntax - greyscale intensity-component original-reference-name destination-reference-name
    * Example - greyscale intensity-component goose goose-greyscale
    * Conditions -
        * If intensity-component given as the next argument after greyscale, default is intensity.
* sepia
    * Syntax - sepia original-reference-name destination-reference-name
    * Example - sepia goose goose-sepia
    * Conditions -
        * original reference name must exist before executing.
* horizontal-flip
    * Syntax - horizontal-flip original-reference-name destination-reference-name
    * Example - horizontal-flip goose goose-hor
    * Conditions -
        * original reference name must exist before executing.
* vertical-flip
    * Syntax - vertical-flip original-reference-name destination-reference-name
    * Example - vertical-flip goose goose-ver
    * Conditions -
        * original reference name must exist before executing.
* brighten
    * Syntax - brighten original-reference-name destination-reference-name
    * Example - brighten 10 goose goose-bright
    * Conditions -
        * Positive increment is brightening.
        * Negative increment is darkening.
        * Increments can only take integer values.
* rgb-split
    * Syntax - rgb-split original-reference-name destination-red-name destination-green-name
      destination-blue-name
    * Example - rgb-split goose goose-red goose-green goose-blue
    * Conditions -
        * original reference name must exist before executing.
* rgb-combine
    * Syntax - rgb-combine destination-reference-name original-red-name original-green-name
      original-blue-name
    * Example - rgb-combine goose-red goose-green goose-blue goose
    * Conditions -
        * original reference name must exist before executing.
* blur
    * Syntax - blur original-reference-name destination-reference-name
    * Example - blur goose goose-blur
    * Conditions -
        * original reference name must exist before executing.
* sharpen
    * Syntax - sharpen original-reference-name destination-reference-name
    * Example - sharpen goose goose-sharpen
    * Conditions -
        * original reference name must exist before executing.
* dither
    * Syntax - dither original-reference-name destination-reference-name
    * Example - dither goose goose-dither
    * Conditions -
        * original reference name must exist before executing.
* run
    * Syntax - run script-file-path.txt
    * Example - run scriptToTestAllMethods.txt
    * Conditions -
        * .txt script file must exist.
        * Comments are ignored.