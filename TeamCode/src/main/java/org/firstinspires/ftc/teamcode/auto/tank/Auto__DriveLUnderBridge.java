/* Copyright (c) 2017 FIRST. All rights reserved.
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

package org.firstinspires.ftc.teamcode.auto.tank;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

//Team packages
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.HardwareSkyStone;

@Autonomous(name="Auto: DriveLUnderBridge BUTTON SELECTION", group="Gamma Raider")
@Disabled
public class Auto__DriveLUnderBridge extends LinearOpMode {

    public enum AutoMode {
        MODE_NOT_SELECTED,
        RED_ALLIANCE_DEPOT_SIDE,
        RED_ALLIANCE_BUILDING_SITE_SIDE,
        BLUE_ALLIANCE_DEPOT_SIDE,
        BLUE_ALLIANCE_BUILDING_SITE_SIDE
    }

    /* Declare OpMode members. */
    HardwareSkyStone         robot   = new HardwareSkyStone();   // Use a Pushbot's hardware
    private ElapsedTime     runtime = new ElapsedTime();

    //static final double     COUNTS_PER_MOTOR_OUTPUT_REV    = 1120  ;  // For REV HD motor 40:1 gear ratio
    static final double     COUNTS_PER_OUTPUT_GEAR_REV     = 67200 ;  // For Drivetrain setup as of 2019/09/28
    //static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_MM   = 90 ;     // For figuring circumference
    static final double     COUNTS_PER_CM         = ((COUNTS_PER_OUTPUT_GEAR_REV) /
                                                      (WHEEL_DIAMETER_MM * 3.1415)) / 10;
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;

    @Override
    public void runOpMode() {

        AutoMode autoMode;

        telemetry.setAutoClear(false);
        Telemetry.Item status = telemetry.addData("Status", "Initialized");
        telemetry.update();

        autoMode = SelectAutoMode();

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        robot.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                          robot.leftDrive.getCurrentPosition(),
                          robot.rightDrive.getCurrentPosition());
        telemetry.update();

        robot.stoneFlap.setPosition(robot.STONE_FLAP_HOME); // Initialize stoneFlap servo position
        sleep(1000); // pause for servos to move

        status.setValue("Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        telemetry.clearAll();

        switch (autoMode) {
            case RED_ALLIANCE_BUILDING_SITE_SIDE:
                TurnLeftUnderBridgeFromParked();
                break;
            case RED_ALLIANCE_DEPOT_SIDE:
                TurnRightUnderBridgeFromParked();
                break;
            case BLUE_ALLIANCE_BUILDING_SITE_SIDE:
                TurnRightUnderBridgeFromParked();
                break;
            case BLUE_ALLIANCE_DEPOT_SIDE:
                TurnLeftUnderBridgeFromParked();
                break;
            case MODE_NOT_SELECTED:
                //This one should not happen
                break;
        }

    }

    private void TurnLeftUnderBridgeFromParked() {
        //----------------------------------------------------------------------------------
        // Movement based on approx measurements from 2019-10-12 Scrimmage 2 VERSION 1 HERE
        //----------------------------------------------------------------------------------
        encoderDrive(DRIVE_SPEED, 63.5, 63.5, 5.0);
        encoderDrive(TURN_SPEED, -40, 40, 4.0); //Turn LEFT //40 is still too much, try to figure out how to make function that takes in degrees as input OR research using IMU
        encoderDrive(DRIVE_SPEED, 134.38, 134.38, 5.0);
        telemetry.addData("Path", "Complete");
        telemetry.update();
    }
    private void TurnRightUnderBridgeFromParked() {
        //----------------------------------------------------------------------------------
        // Movement based on approx measurements from 2019-10-12 Scrimmage 2 VERSION 1 HERE
        //----------------------------------------------------------------------------------
        encoderDrive(DRIVE_SPEED, 63.5, 63.5, 5.0);
        encoderDrive(TURN_SPEED, 40, -40, 4.0); //Turn RIGHT //40 is still too much, try to figure out how to make function that takes in degrees as input OR research using IMU
        encoderDrive(DRIVE_SPEED, 134.38, 134.38, 5.0);
        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    /*
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDrive(double speed,
                             double leftcm, double rightcm,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.leftDrive.getCurrentPosition() + (int)(leftcm * COUNTS_PER_CM);
            newRightTarget = robot.rightDrive.getCurrentPosition() + (int)(rightcm * COUNTS_PER_CM);
            robot.leftDrive.setTargetPosition(newLeftTarget);
            robot.rightDrive.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            robot.leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.leftDrive.setPower(Math.abs(speed));
            robot.rightDrive.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                   (runtime.seconds() < timeoutS) &&
                   (robot.leftDrive.isBusy() && robot.rightDrive.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                                            robot.leftDrive.getCurrentPosition(),
                                            robot.rightDrive.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.leftDrive.setPower(0);
            robot.rightDrive.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }

    private AutoMode SelectAutoMode() {
        telemetry.addLine("Press button to select robot position.");
        telemetry.addLine("A: RED_ALLIANCE_DEPOT_SIDE, B: RED_ALLIANCE_BUILDING_SITE_SIDE");
        telemetry.addLine("X: BLUE_ALLIANCE_DEPOT_SIDE, Y: BLUE_ALLIANCE_BUILDING_SITE_SIDE");
        telemetry.update();

        AutoMode autoMode = AutoMode.MODE_NOT_SELECTED;
        Telemetry.Item mode = telemetry.addData("AutoMode", "Not Selected");

        telemetry.update();
        while (autoMode == AutoMode.MODE_NOT_SELECTED) {
           if (gamepad1.a) {
               autoMode = AutoMode.RED_ALLIANCE_DEPOT_SIDE;
           }
           if (gamepad1.b) {
               autoMode = AutoMode.RED_ALLIANCE_BUILDING_SITE_SIDE;
           }
           if (gamepad1.x) {
                autoMode = AutoMode.BLUE_ALLIANCE_DEPOT_SIDE;
           }
           if (gamepad1.y) {
               autoMode = AutoMode.BLUE_ALLIANCE_BUILDING_SITE_SIDE;
           }
           idle();
        }
        mode.setValue(autoMode.toString());
        telemetry.update();
        // Wait for the user to release the button
        while (gamepad1.a || gamepad1.b || gamepad1.x || gamepad1.y) {
            idle();
        }
        return autoMode;
    }

}
