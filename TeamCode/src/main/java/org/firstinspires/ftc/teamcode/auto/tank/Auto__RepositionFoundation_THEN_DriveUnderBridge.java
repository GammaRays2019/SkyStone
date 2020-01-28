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
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.firstinspires.ftc.teamcode.hardware.HardwareSkyStone;

//Team packages

@Autonomous(name="Auto: RepositionFoundation_THEN_DriveUnderBridge BUTTON SELECTION", group="Pushbot")
@Disabled
public class Auto__RepositionFoundation_THEN_DriveUnderBridge extends LinearOpMode {

    /*
    On 11/8/2019, had to put all foundation servo declaration and initialization stuff in this class
    because I couldn't access it from HardwareSkyStone, even though I could still access the drive
    motors and stoneFlap servo from there.
    Very weird issue.
    Hopefully can resolve later so all hardware can be defined in HardwareSkyStone (or another
    Hardware Map).

    --Richie Anderson, 11/08/2019
     */
    /* ISSUE RESOLVED! */

    public enum AutoMode {
        MODE_NOT_SELECTED,
        RED_ALLIANCE_DEPOT_SIDE,
        RED_ALLIANCE_BUILDING_SITE_SIDE,
        BLUE_ALLIANCE_DEPOT_SIDE,
        BLUE_ALLIANCE_BUILDING_SITE_SIDE
    }

    /* Declare OpMode members. */
    private HardwareSkyStone        robot   = new HardwareSkyStone();   // Use hardware from HardwareSkyStone
    private ElapsedTime     runtime = new ElapsedTime();

    //static final double     COUNTS_PER_MOTOR_OUTPUT_REV    = 1120  ;  // For REV HD motor 40:1 gear ratio
    static final double     COUNTS_PER_OUTPUT_GEAR_REV     = 67200 ;  // For Drivetrain setup as of 2019/09/28
    //static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_MM   = 90 ;     // For figuring circumference
    static final double     COUNTS_PER_CM         = ((COUNTS_PER_OUTPUT_GEAR_REV) /
                                                      (WHEEL_DIAMETER_MM * 3.1415)) / 10;

    static final double DRIVE_TRAIN_WIDTH = 34.925; // In centimeters

    // For turning function
    static final double TURN_SCALE_FACTOR = 1.0;
    static final double COUNTS_PER_DEGREE = ((DRIVE_TRAIN_WIDTH * Math.PI) * COUNTS_PER_CM / 360.0) * TURN_SCALE_FACTOR;

    static final double DRIVE_SPEED       = 0.5;
    static final double TURN_SPEED        = 0.3;

    // Servo Constants
    private static final double FOUNDATION_SERVO_HOME = 0.0;
    private static final double FOUNDATION_SERVO_UP = 0.0;
    private static final double FOUNDATION_SERVO_DOWN = 1.0;

    @Override
    public void runOpMode() {

        AutoMode autoMode;

        //long delayTimeMilliseconds;

        telemetry.setAutoClear(false);
        Telemetry.Item status = telemetry.addData("Status", "Initialized");
        telemetry.update();

        autoMode = SelectAutoMode();

        sleep(500);
        telemetry.clearAll();
        //delayTimeMilliseconds = SelectDelayTime();

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
        status = telemetry.addData("Status", "Delaying");
        //sleep(delayTimeMilliseconds);
        status.setValue("Running");
        telemetry.update();

        switch (autoMode) {
            case RED_ALLIANCE_BUILDING_SITE_SIDE:
                RepositionFoundationAndParkOpposite();
                break;
            case RED_ALLIANCE_DEPOT_SIDE:
                TurnRightUnderBridgeFromParked();
                break;
            case BLUE_ALLIANCE_BUILDING_SITE_SIDE:
                RepositionFoundationAndPark();
                break;
            case BLUE_ALLIANCE_DEPOT_SIDE:
                TurnLeftUnderBridgeFromParked();
                break;
            case MODE_NOT_SELECTED:
                //This one should not happen
                break;
        }

    }

    private void driveForwardUnderBridge() {
        encoderDrive(0.6, 20, 20, 10.0);
    }

    private void TurnLeftUnderBridgeFromParked() {
        //----------------------------------------------------------------------------------
        // Movement based on approx measurements from 2019-10-12 Scrimmage 2 VERSION 1 HERE
        //----------------------------------------------------------------------------------
        encoderDrive(0.6, 63.5, 63.5, 5.0);
        encoderDrive(0.5, -40, 40, 4.0); //Turn LEFT //40 is still too much, try to figure out how to make function that takes in degrees as input OR research using IMU
        encoderDrive(0.6, 134.38, 134.38, 5.0);
        telemetry.addData("Path", "Complete");
        telemetry.update();
    }
    private void TurnRightUnderBridgeFromParked() {
        //----------------------------------------------------------------------------------
        // Movement based on approx measurements from 2019-10-12 Scrimmage 2 VERSION 1 HERE
        //----------------------------------------------------------------------------------
        encoderDrive(0.6, 63.5, 63.5, 5.0);
        encoderDrive(0.5, 40, -40, 4.0); //Turn RIGHT //40 is still too much, try to figure out how to make function that takes in degrees as input OR research using IMU
        encoderDrive(0.6, 134.38, 134.38, 5.0);
        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    //Blue Building Site Side
    private void RepositionFoundationAndPark() {
        // Task 1 --- Pull foundation
        // Task 2 --- Push foundation into building site
        // Task 3 --- Park under bridge

        // New code that starts aligned diagonally with building site tape but not touching it
        encoderDrive(DRIVE_SPEED, 20, 20, 5.0);
        encoderTurnByDegrees(TURN_SPEED, 60); //Turn RIGHT to face foundation
        encoderDrive(DRIVE_SPEED, 52, 52, 5.0);

        /* Old code that starts in building site
        encoderDrive(DRIVE_SPEED, 79.25, 79.25, 5.0); //Drive forward to prepare for pulling foundation[Task 1, Step 1]
        */

        setFoundationServoPosition(FOUNDATION_SERVO_DOWN); //Put foundation puller down[Task 1, Step 2]
        sleep(1000);
        encoderDrive(DRIVE_SPEED, -63.15, -63.15, 5.0); //Drive in reverse to pull foundation[Task 1, Step 3]
        sleep(1000);
        setFoundationServoPosition(FOUNDATION_SERVO_UP); //Move foundation puller back up[Task 1, Step 4]
        sleep(1000);
        encoderTurnByDegrees(TURN_SPEED, 105); //Turn RIGHT to face bridge to prepare for pushing foundation[Task 2, Step 1]
        encoderDrive(DRIVE_SPEED, 68.7, 68.7, 5.0); //Drive forward towards bridge[Task 2, Step 2]
        sleep(500);
        encoderTurnByDegrees(TURN_SPEED, -105); //Turn LEFT to face wall across from staring position[Task 2, Step 3]
        encoderDrive(DRIVE_SPEED, 108.7, 108.7, 5.0); //Drive forward towards wall[Task 2, Step 4]
        encoderTurnByDegrees(TURN_SPEED, -100); //Turn LEFT to face wall next to foundation, across from bridge[Task 2, Step 5]
        encoderDrive(DRIVE_SPEED, 65, 65, 5.0); //Drive forward towards wall[Task 2, Step 6]
        encoderTurnByDegrees(TURN_SPEED, -105); //Turn LEFT to face long side of foundation[Task 2, Step 7]
        encoderDrive(DRIVE_SPEED, 83, 83, 8.0); //Drive forward to push foundation[Task 2, Step 8]
        encoderDrive(DRIVE_SPEED, -14.5, -14.5, 5.0); //Driving forward continued???[Task 2, Step 8???]
                                                                            //Originally meant to be backing up
        encoderTurnByDegrees(TURN_SPEED, -120); //Turn LEFT to orient towards bridge[Task 3, Step 1]
        encoderDrive(DRIVE_SPEED, 120, 120, 5.0); //Drive forward towards bridge[Task 3, Step 2]
        /*
        encoderTurnByDegrees(TURN_SPEED, 55);
        encoderDrive(DRIVE_SPEED, 70.5, 70.5, 5.0); //Driving forward continued[Task 3, Step 3???]
        encoderTurnByDegrees(TURN_SPEED, -45); //Straighten up to drive under bridge
        encoderDrive(DRIVE_SPEED, 20, 15, 5.0); //Driving forward continued[Task 3, Step 4???]
         */
    }

    //Red Building Site Side
    private void RepositionFoundationAndParkOpposite() {

        // Task 1 --- Pull foundation
        // Task 2 --- Push foundation into building site
        // Task 3 --- Park under bridge

        //NewER code that starts with back straight on wall
        encoderDrive(DRIVE_SPEED, 26.67, 26.67, 5.0);
        encoderTurnByDegrees(TURN_SPEED, 90); //RIGHT
        encoderDrive(DRIVE_SPEED, 43.8, 43.8, 5.0);
        encoderTurnByDegrees(TURN_SPEED, -90); //LEFT
        encoderDrive(DRIVE_SPEED, 48.26, 48.26, 5.0);

//        // New code that starts aligned diagonally with building site tape but not touching it
//        encoderDrive(DRIVE_SPEED, 20, 20, 5.0);
//        encoderTurnByDegrees(TURN_SPEED, -60); //Turn LEFT to face foundation
//        encoderDrive(DRIVE_SPEED, 52, 52, 5.0);

        /* Old code that starts in building site
        encoderDrive(DRIVE_SPEED, 79.25, 79.25, 5.0); //Drive forward to prepare for pulling foundation[Task 1, Step 1]
        */

        setFoundationServoPosition(FOUNDATION_SERVO_DOWN); //Put foundation puller down[Task 1, Step 2]
        sleep(1000);
        encoderDrive(DRIVE_SPEED, -63.15, -63.15, 5.0); //Drive in reverse to pull foundation[Task 1, Step 3]
        sleep(1000);
        setFoundationServoPosition(FOUNDATION_SERVO_UP); //Move foundation puller back up[Task 1, Step 4]
        sleep(1000);
        encoderTurnByDegrees(TURN_SPEED, -105); //Turn LEFT to face bridge to prepare for pushing foundation[Task 2, Step 1]
        encoderDrive(DRIVE_SPEED, 70.7, 70.7, 5.0); //Drive forward towards bridge[Task 2, Step 2]
        sleep(500);
        encoderTurnByDegrees(TURN_SPEED, 105); //Turn RIGHT to face wall across from staring position[Task 2, Step 3]
        encoderDrive(DRIVE_SPEED, 108.7, 108.7, 5.0); //Drive forward towards wall[Task 2, Step 4]
        encoderTurnByDegrees(TURN_SPEED, 100); //Turn RIGHT to face wall next to foundation, across from bridge[Task 2, Step 5]
        encoderDrive(DRIVE_SPEED, 65, 65, 5.0); //Drive forward towards wall[Task 2, Step 6]
        encoderTurnByDegrees(TURN_SPEED, 105); //Turn RIGHT to face long side of foundation[Task 2, Step 7]
        encoderDrive(DRIVE_SPEED, 83, 83, 8.0); //Drive forward to push foundation[Task 2, Step 8]
        encoderDrive(DRIVE_SPEED, -14.5, -14.5, 5.0); //Driving forward continued???[Task 2, Step 8???]
        //Originally meant to be backing up
        encoderTurnByDegrees(TURN_SPEED, 120); //Turn RIGHT to orient towards bridge[Task 3, Step 1]
        encoderDrive(DRIVE_SPEED, 120, 120, 5.0); //Drive forward towards bridge[Task 3, Step 2]
        /*
        encoderTurnByDegrees(TURN_SPEED, 55);
        encoderDrive(DRIVE_SPEED, 70.5, 70.5, 5.0); //Driving forward continued[Task 3, Step 3???]
        encoderTurnByDegrees(TURN_SPEED, -45); //Straighten up to drive under bridge
        encoderDrive(DRIVE_SPEED, 20, 15, 5.0); //Driving forward continued[Task 3, Step 4???]
         */

//        // Task 1 --- Pull foundation
//        // Task 2 --- Push foundation into building site
//        // Task 3 --- Park under bridge
//
//        // New code that starts aligned diagonally with building site tape but not touching it
//        encoderDrive(DRIVE_SPEED, 20, 20, 5.0);
//        encoderTurnByDegrees(TURN_SPEED, -60); //Turn LEFT to face foundation
//        encoderDrive(DRIVE_SPEED, 52, 52, 5.0);
//
//        /* Old code that starts in building site
//        encoderDrive(DRIVE_SPEED, 79.25, 79.25, 5.0); //Drive forward to prepare for pulling foundation[Task 1, Step 1]
//         */
//
//        setFoundationServoPosition(FOUNDATION_SERVO_DOWN); //Put foundation puller down[Task 1, Step 2]
//        sleep(1000);
//        encoderDrive(DRIVE_SPEED, -63.15, -63.15, 5.0); //Drive in reverse to pull foundation[Task 1, Step 3]
//        sleep(1000);
//        setFoundationServoPosition(FOUNDATION_SERVO_UP); //Move foundation puller back up[Task 1, Step 4]
//        sleep(1000);
//        encoderTurnByDegrees(TURN_SPEED, -105); //Turn LEFT to face bridge to prepare for pushing foundation[Task 2, Step 1]
//        encoderDrive(DRIVE_SPEED, 68.7, 68.7, 5.0); //Drive forward towards bridge[Task 2, Step 2]
//        sleep(500);
//        encoderTurnByDegrees(TURN_SPEED, 105); //Turn RIGHT to face wall across from staring position[Task 2, Step 3]
//        encoderDrive(DRIVE_SPEED, 108.7, 108.7, 5.0); //Drive forward towards wall[Task 2, Step 4]
//        encoderTurnByDegrees(TURN_SPEED, 100); //Turn RIGHT to face wall next to foundation, across from bridge[Task 2, Step 5]
//        encoderDrive(DRIVE_SPEED, 65, 65, 5.0); //Drive forward towards wall[Task 2, Step 6]
//        encoderTurnByDegrees(TURN_SPEED, 105); //Turn RIGHT to face long side of foundation[Task 2, Step 7]
//        encoderDrive(DRIVE_SPEED, 83, 83, 8.0); //Drive forward to push foundation[Task 2, Step 8]
//        encoderDrive(DRIVE_SPEED, -14.5, -14.5, 5.0); //Driving forward continued???[Task 2, Step 8???]
//                                                                            //Originally meant to be backing up
//        encoderTurnByDegrees(TURN_SPEED, 120); //Turn LEFT to orient towards bridge[Task 3, Step 1]
//        encoderDrive(DRIVE_SPEED, 120, 120, 5.0); //Drive forward towards bridge[Task 3, Step 2]
//        /*
//        encoderTurnByDegrees(TURN_SPEED, -55);
//        encoderDrive(DRIVE_SPEED, 70.5, 70.5, 5.0); //Driving forward continued[Task 3, Step 3???]
//        encoderTurnByDegrees(TURN_SPEED, 45); //straighten up to drive under bridge
//        encoderDrive(DRIVE_SPEED, 20, 15, 5.0); //Driving forward continued[Task 3, Step 4???]
//         */
    }

    private void setFoundationServoPosition(double position) {
        robot.leftFoundationServo.setPosition(position);
        robot.rightFoundationServo.setPosition(1 - position);
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

    private void encoderTurnByDegrees(double power, double degrees) {
        double counts = degrees * COUNTS_PER_DEGREE;
        robot.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if (degrees < 0) {
            //Turning left
            robot.leftDrive.setPower(-power);
            robot.rightDrive.setPower(power);
        } else {
            robot.leftDrive.setPower(power);
            robot.rightDrive.setPower(-power);
        }

        while (opModeIsActive() &&
                Math.abs(robot.leftDrive.getCurrentPosition()) < Math.abs(counts)) {
            idle();
        }

        robot.leftDrive.setPower(0.0);
        robot.rightDrive.setPower(0.0);
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

    /*
    private long SelectDelayTime() {
        telemetry.addLine("Use bumpers to increment delay time by 1000ms.");
        telemetry.addLine("Press A when done.");
        telemetry.update();

        int delayTimeMilliseconds = 0;
        Telemetry.Item delay = telemetry.addData("Delay","%d (Not Set)",delayTimeMilliseconds);

        telemetry.update();

        while (gamepad1.a == false) {
            if (gamepad1.left_bumper) {
                delayTimeMilliseconds -= 1000;
                if (delayTimeMilliseconds < 0) {
                    delayTimeMilliseconds = 0;
                }
                // Wait for the bumper to be released
                while (gamepad1.left_bumper) {
                    idle();
                }
            }
            if (gamepad1.right_bumper) {
                delayTimeMilliseconds += 1000;
                if (delayTimeMilliseconds > 20000) {
                    delayTimeMilliseconds = 20000;
                }
                while (gamepad1.right_bumper) {
                    idle();
                }
            }
            delay.setValue("% (Not Set)", delayTimeMilliseconds);
            telemetry.update();
        }
        delay.setValue("%d", delayTimeMilliseconds);
        telemetry.update();

        // Wait for user to release the a button
        while (gamepad1.a) {
            idle();
        }
        return delayTimeMilliseconds;
    }
     */

}
