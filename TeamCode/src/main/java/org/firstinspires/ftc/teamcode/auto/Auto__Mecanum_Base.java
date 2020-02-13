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

package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.HardwareMecanumSkyStone;
import org.firstinspires.ftc.teamcode.hardware.HardwareSkyStone;

//Team packages

@Autonomous(name="Auto: BASE", group="Gamma Raider")
//@Disabled
public class Auto__Mecanum_Base extends LinearOpMode {

    public enum AutoMode {
        MODE_NOT_SELECTED,
        RED_ALLIANCE_DEPOT_SIDE,
        RED_ALLIANCE_BUILDING_SITE_SIDE,
        BLUE_ALLIANCE_DEPOT_SIDE,
        BLUE_ALLIANCE_BUILDING_SITE_SIDE
    }

    /* Declare OpMode members. */
    private HardwareMecanumSkyStone mrobot   = new HardwareMecanumSkyStone();   // Use hardware from HardwareMecanumSkyStone
                                                                                // m for mecanum
    private ElapsedTime     runtime = new ElapsedTime();

    //static final double     COUNTS_PER_MOTOR_OUTPUT_REV    = 1120  ;  // For REV HD motor 40:1 gear ratio
    static final double     COUNTS_PER_OUTPUT_GEAR_REV     = 67200 ;  // For Drivetrain setup as of 2019/09/28
    static final double     WHEEL_DIAMETER_MM   = 75 ;     // For figuring circumference
    static final double     COUNTS_PER_CM         = ((COUNTS_PER_OUTPUT_GEAR_REV) /
                                                      (WHEEL_DIAMETER_MM * 3.1415)) / 10;

    static final double DRIVE_TRAIN_WIDTH = 35.56; // In centimeters (14 inches)

    // For turning function
    static final double TURN_SCALE_FACTOR = 1.0;
    static final double COUNTS_PER_DEGREE = ((DRIVE_TRAIN_WIDTH * Math.PI) * COUNTS_PER_CM / 360.0) * TURN_SCALE_FACTOR;

    static final double DRIVE_SPEED       = 0.5;
    static final double TURN_SPEED        = 0.3;

    // Servo Constants
    static final double FOUNDATION_SERVO_UP = 0.0;
    static final double FOUNDATION_SERVO_DOWN = 1.0;

    public AutoMode autoMode;

    @Override
    public void runOpMode() {

    }

    // Function to run at the start of OpModes that extend this class
    // Carries out Initialization functions
    public void initAuto() {
        telemetry.setAutoClear(false);
        Telemetry.Item status = telemetry.addData("Status", "Initialized");
        telemetry.update();

        autoMode = SelectAutoMode();

        sleep(500);
        telemetry.clearAll();

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        mrobot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");
        telemetry.update();

        mrobot.FLDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mrobot.FRDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mrobot.RLDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mrobot.RRDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        mrobot.FLDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mrobot.FRDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mrobot.RLDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mrobot.RRDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d :%7d :%7d",
                mrobot.FLDrive.getCurrentPosition(),
                mrobot.FRDrive.getCurrentPosition(),
                mrobot.RLDrive.getCurrentPosition(),
                mrobot.RRDrive.getCurrentPosition());
        telemetry.update();

        mrobot.stoneFlap.setPosition(mrobot.STONE_FLAP_HOME); // Initialize stoneFlap servo position
        sleep(1000); // pause for servos to move

        status.setValue("Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        telemetry.clearAll();
        //status = telemetry.addData("Status", "Delaying");
        //sleep(delayTimeMilliseconds);
        status.setValue("Running");
        telemetry.update();
    }

    //--------------------------------------------
    // High-Level Movement-Path Functions Below
    //--------------------------------------------

    public void StrafeLeftUnderBridge() {
        encoderStrafe(0.5, -75, 10.0);
        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    public void StrafeRightUnderBridge() {
        encoderStrafe(0.5, 75, 10.0);
        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    public void StrafeLeftUnderBridgeLShape() {
        encoderDriveStraight(0.6, 65.0,5.0);
        encoderStrafe(DRIVE_SPEED, -80.0, 5.0);
        telemetry.addData("Path", "Complete");
        telemetry.update();
    }
    public void StrafeRightUnderBridgeLShape() {
        encoderDriveStraight(0.6, 65.0,5.0);
        encoderStrafe(DRIVE_SPEED, 80.0, 5.0);
        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    //Blue Building Site Side
    public void RepositionFoundationAndPark() {
    }

    //Red Building Site Side
    public void RepositionFoundationAndParkOpposite() {
    }

    //Does not take into consideration whether or not the stone is a Skystone
    //For Red Alliance
    public void ScoreRandomStoneRed() {
        //Distance values will need to be adjusted later
        encoderDriveStraight(DRIVE_SPEED, 74.0, 5.0);
        setFoundationServoPosition(FOUNDATION_SERVO_UP); //Should place foundation arm down to grab stone
        sleep(500);
        encoderDriveStraight(DRIVE_SPEED, 15.0, 5.0);
        //encoderDriveStraight(DRIVE_SPEED, -20.0, 5.0);
        encoderTurnByDegrees(1.0, 90); //Turn 90deg right
        setFoundationServoPosition(FOUNDATION_SERVO_DOWN);
        sleep(500);
        //encoderStrafe(DRIVE_SPEED, 10.0, 5.0); //Strafe 10cm right
        encoderDriveStraight(DRIVE_SPEED, 80.0, 5.0); //Should move robot under bridge
        //setFoundationServoPosition(FOUNDATION_SERVO_DOWN); //Should release stone
        encoderDriveStraight(0.25, -10.0, 5.0); //Back up under bridge
    }

    //-----------------------------------
    // Low-Level Functions Below
    //-----------------------------------

    public void setFoundationServoPosition(double position) {
        mrobot.leftFoundationServo.setPosition(position);
        mrobot.rightFoundationServo.setPosition(1 - position);
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
            newLeftTarget = mrobot.FLDrive.getCurrentPosition() + (int)(leftcm * COUNTS_PER_CM);
            newRightTarget = mrobot.FRDrive.getCurrentPosition() + (int)(rightcm * COUNTS_PER_CM);
            mrobot.FLDrive.setTargetPosition(newLeftTarget);
            mrobot.RLDrive.setTargetPosition(newLeftTarget);
            mrobot.FRDrive.setTargetPosition(newRightTarget);
            mrobot.RRDrive.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            mrobot.FLDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            mrobot.RLDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            mrobot.FRDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            mrobot.RRDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            mrobot.FLDrive.setPower(Math.abs(speed));
            mrobot.RLDrive.setPower(Math.abs(speed));
            mrobot.FRDrive.setPower(Math.abs(speed));
            mrobot.RRDrive.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                   (runtime.seconds() < timeoutS) &&
                   (mrobot.FLDrive.isBusy() && mrobot.FRDrive.isBusy()) /*&&*/
                    /* mrobot.RLDrive.isBusy() && mrobot.RRDrive.isBusy() */) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d :%7d :%7d",
                                            mrobot.FLDrive.getCurrentPosition(),
                                            mrobot.FRDrive.getCurrentPosition(),
                                            mrobot.RLDrive.getCurrentPosition(),
                                            mrobot.RRDrive.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            mrobot.FLDrive.setPower(0);
            mrobot.FRDrive.setPower(0);
            mrobot.RLDrive.setPower(0);
            mrobot.RRDrive.setPower(0);

            // Turn off RUN_TO_POSITION
            mrobot.FLDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            mrobot.FRDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            mrobot.RLDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            mrobot.RRDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }

    public void encoderDriveStraight(double power, double distancecm, double timeoutS) {
        double factor = 1;
        double counts = (int)((distancecm / factor) * COUNTS_PER_CM);
        mrobot.FLDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mrobot.FRDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mrobot.RLDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mrobot.RRDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mrobot.FLDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mrobot.FRDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mrobot.RLDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mrobot.RRDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        runtime.reset();
        if (distancecm < 0) {
            mrobot.FLDrive.setPower(-power);
            mrobot.FRDrive.setPower(-power);
            mrobot.RLDrive.setPower(-power);
            mrobot.RRDrive.setPower(-power);
        } else {
            mrobot.FLDrive.setPower(power);
            mrobot.FRDrive.setPower(power);
            mrobot.RLDrive.setPower(power);
            mrobot.RRDrive.setPower(power);
        }

        while (opModeIsActive() &&
                (runtime.seconds() < timeoutS) &&
                Math.abs(mrobot.FLDrive.getCurrentPosition()) < Math.abs(counts)) {
            idle();
        }

        mrobot.FLDrive.setPower(0.0);
        mrobot.FRDrive.setPower(0.0);
        mrobot.RLDrive.setPower(0.0);
        mrobot.RRDrive.setPower(0.0);
    }

    public void encoderStrafe(double power, double distancecm, double timeoutS) {
        //double strafeFactor = 1/Math.sqrt(2);
        //double strafeFactor = 1;
        double strafeFactor = 0.88154666;
        double counts = (int)(distancecm / strafeFactor) * (COUNTS_PER_CM);
        mrobot.FLDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mrobot.FRDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mrobot.RLDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mrobot.RRDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mrobot.FLDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mrobot.FRDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mrobot.RLDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mrobot.RRDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        runtime.reset();
        if (distancecm < 0) {
            mrobot.FLDrive.setPower(-power);
            mrobot.FRDrive.setPower(power);
            mrobot.RLDrive.setPower(power);
            mrobot.RRDrive.setPower(-power);
        } else {
            mrobot.FLDrive.setPower(power);
            mrobot.FRDrive.setPower(-power);
            mrobot.RLDrive.setPower(-power);
            mrobot.RRDrive.setPower(power);
        }

        while (opModeIsActive() &&
                (runtime.seconds() < timeoutS) &&
                Math.abs(mrobot.FLDrive.getCurrentPosition()) < Math.abs(counts)) {
            idle();
        }

        mrobot.FLDrive.setPower(0.0);
        mrobot.FRDrive.setPower(0.0);
        mrobot.RLDrive.setPower(0.0);
        mrobot.RRDrive.setPower(0.0);
    }

    private void encoderTurnByDegrees(double power, double degrees) {
        double counts = degrees * COUNTS_PER_DEGREE;
        mrobot.FLDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mrobot.FRDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mrobot.RLDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mrobot.RRDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mrobot.FLDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mrobot.FRDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mrobot.RLDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mrobot.RRDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if (degrees < 0) {
            mrobot.FLDrive.setPower(-power);
            mrobot.FRDrive.setPower(power);
            mrobot.RLDrive.setPower(-power);
            mrobot.RRDrive.setPower(power);
        } else {
            mrobot.FLDrive.setPower(power);
            mrobot.FRDrive.setPower(-power);
            mrobot.RLDrive.setPower(power);
            mrobot.RRDrive.setPower(-power);
        }

        while (opModeIsActive() &&
                Math.abs(mrobot.FLDrive.getCurrentPosition()) < Math.abs(counts)) {
            idle();
        }

        mrobot.FLDrive.setPower(0.0);
        mrobot.FRDrive.setPower(0.0);
        mrobot.RLDrive.setPower(0.0);
        mrobot.RRDrive.setPower(0.0);
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
