/* Copyright (c) 2019 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.auto.experimental;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

/**
 * This 2019-2020 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the Skystone game elements.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@TeleOp(name = "Richie Concept: Interpreting Stones", group = "Concept")
//@Disabled
public class RichieConcept_InterpretingStones extends LinearOpMode {

    private ElapsedTime     runtime = new ElapsedTime();

    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";

    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    private static final String VUFORIA_KEY = "Ab7Clv7/////AAABmboAMQo920mOh6pR8GBToMNh3qMjs/M7xOlu6nxN3sGL9BXfH1zp3EDCg3xLibrR9ioGplDCR3ZBkDKzjqEWU659Gwt3pYy82nUIqH6BmjAQWsFSDGqP/ggBaYCSZwCrOT4lUUK4fH44dFoErA4ivRTkw5MofnQO6uZU6CotYINH9sgB4k6CWDT8M3q3VVPeTH6smEzMu9NGCgGk5qnAJchK5rm6uKubAEoJDhRwEhGm/3IgGHEpjoY6Q+bV9KmjQfM5ubqh8VkqSkp5OWcReYpF7EV493V3MGcPR9i/R4rM7/nbDO8YYT/oPIVbvkK3S8ijK+fBMy9sdpTn9bnqPoiDzrP3XBEnnCSR57NhTB+8";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    // Stone edge values should be less than actual value because the method will check
    // if the actual value is greater than these constants.
    // ---
    // Stone Right Edge Values / Used for Blue Alliance Side
    // R for Right Edge
    final double R_RIGHT_STONE_POSITION = 500;
    final double R_CENTER_STONE_POSITION = 300;
    // The Skystone is on the left if no skystone is detected (since we are only looking at 2 stones)
    // - In this case, skystonePosition should remain at -1

    // Stone Left Edge Values / Used for Red Alliance Side
    // L for Left Edge

    @Override
    public void runOpMode() {

//        String leftStone = null;
//        String rightStone = null;

        double skystonePosition = -1;

        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 10.0)) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                      telemetry.addData("# Object Detected", updatedRecognitions.size());
                      // step through the list of recognitions and display boundary info.
                      int i = 0;
                      for (Recognition recognition : updatedRecognitions) {
                          telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                          telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                  recognition.getLeft(), recognition.getTop());
                          telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                  recognition.getRight(), recognition.getBottom());
                          if (recognition.getLabel() == "Skystone") {
                              skystonePosition = recognition.getRight();
                              telemetry.addLine("skystonePosition (Right Edge): " + skystonePosition);
                          }
                      }
                      telemetry.update();
                    }
                }
            }
//            while (opModeIsActive()) {
//                if (tfod != null) {
//                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
//                    if (updatedRecognitions != null) {
//                        if (updatedRecognitions.size() == 2) {
//                            int i = 0;
//                            for (Recognition recognition : updatedRecognitions) {
//                                if (recognition.getLabel() == "Skystone") {
//
//                                } else if (recognition.getLabel() == "Stone") {
//
//                                }
//                            }
//                        }
//                    }
//                }
//            }
        }

        if (tfod != null) {
            tfod.shutdown();
        }

        while (opModeIsActive()) {

            if (skystonePosition >= R_RIGHT_STONE_POSITION) {
                telemetry.addLine("Skystones are on RIGHT.");
                telemetry.update();
            } else if (skystonePosition >= R_CENTER_STONE_POSITION) {
                telemetry.addLine("Skystones are in CENTER.");
                telemetry.update();
            } else {
                telemetry.addLine("Skystones are on LEFT.");
                telemetry.update();
            }

        }

    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
       tfodParameters.minimumConfidence = 0.8;
       tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
       tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }
}
