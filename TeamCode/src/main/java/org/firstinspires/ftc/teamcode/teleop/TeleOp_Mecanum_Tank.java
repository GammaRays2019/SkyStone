
package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.hardware.HardwareMecanumSkyStone;

@TeleOp(name="ACHS TeleOp 4-Motor Tank v2", group="Linear OpMode")
//@Disabled
public class TeleOp_Mecanum_Tank extends LinearOpMode {

    // Declare OpMode members.
    private HardwareMecanumSkyStone robot   = new HardwareMecanumSkyStone();   // Use hardware from HardwareMecanumSkyStone
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Motor power constants
        final double ARM_POWER = 0.5;

        // Servos
        double stoneFlapServoDelta = 0;
        double foundationServoDelta = 0;

        // Define and  initialize power variables
        double FLPower  = 0;
        double FRPower  = 0;
        double RLPower  = 0;
        double RRPower  = 0;
        double armPower = 0;
        // To switch between power levels of drive motors
        final double LOW_POWER = 0.25;
        final double MEDIUM_POWER = 0.5;
        final double HIGH_POWER = 1.0;
        double powerCoefficient = MEDIUM_POWER;
        String powerMode = "Medium";

        /*
        // Servo position constants
        final double STONE_FLAP_INIT = 0.5;         //Currently handled by Hardware Map
        final double FOUNDATION_SERVO_HOME = 0.0;
         */

        robot.init(hardwareMap);

        /*
        // Initialize servo positions
        robot.stoneFlap.setPosition(STONE_FLAP_INIT);
        robot.leftFoundationServo.setPosition(FOUNDATION_SERVO_HOME);     //Currently handled by Hardware Map
        robot.rightFoundationServo.setPosition(1 - FOUNDATION_SERVO_HOME);
         */

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            //------------------------------
            // Drive-train control
            //------------------------------

            // Code for switching between 3 drive power modes
            // Press Y to switch to HIGH_POWER mode
            // Press B to switch to MEDIUM_POWER mode
            // Press A to switch to LOW_POWER mode
            if (gamepad1.y) {
                powerCoefficient = HIGH_POWER;
                powerMode = "High";
                sleep(50);
            }
            if (gamepad1.b) {
                powerCoefficient = MEDIUM_POWER;
                powerMode = "Medium";
                sleep(50);
            }
            if (gamepad1.a) {
                powerCoefficient = LOW_POWER;
                powerMode = "Low";
                sleep(50);
            }

            // Mecanum drive variables
            // Strafing control on left or right stick OR left and right on d-pad
            double horizontal;
            if (gamepad1.left_stick_x > 0.1 || gamepad1.left_stick_x < -0.1) {
                horizontal = gamepad1.left_stick_x;
            } else if (gamepad1.right_stick_x > 0.1 || gamepad1.right_stick_x < -0.1) {
                horizontal = gamepad1.right_stick_x;
            } else if (gamepad1.dpad_left) {
                horizontal = -1*powerCoefficient;
            } else if (gamepad1.dpad_right) {
                horizontal = 1*powerCoefficient;
            } else {
                horizontal = 0;
            }
            // Alternative strafing concept that only uses sticks
            /*
            if (((gamepad1.left_stick_x > 0.1) || (gamepad1.left_stick_x < -0.1)) &&
                    ((gamepad1.right_stick_x > 0.1) || (gamepad1.right_stick_x < -0.1))) { // If both sticks are engaged
                horizontal = (gamepad1.left_stick_x + gamepad1.right_stick_x) / 2; // Use the mean average of both sticks
            } else if (gamepad1.left_stick_x > 0.1 || gamepad1.left_stick_x < -0.1) {
                horizontal = gamepad1.left_stick_x;
            } else if (gamepad1.right_stick_x > 0.1 || gamepad1.right_stick_x < -0.1) {
                horizontal = gamepad1.right_stick_x;
            }
             */
            double leftVertical  = -gamepad1.left_stick_y;
            double rightVertical = -gamepad1.right_stick_y;

            // Tank variation of Mecanum control
            FLPower = (leftVertical + horizontal) * powerCoefficient;
            FRPower = (rightVertical - horizontal) * powerCoefficient;
            RLPower = (leftVertical - horizontal) * powerCoefficient;
            RRPower = (rightVertical + horizontal) * powerCoefficient;

            //--------------------------------
            // Arm motor control
            //--------------------------------
            armPower = (-gamepad2.left_stick_y * ARM_POWER);
            armPower = Range.clip(armPower, -1.0, 1.0);

            //--------------------------------
            // stoneFlap Servo control
            //--------------------------------
            if (-gamepad2.right_stick_y >= 0.1 || -gamepad2.right_stick_y <= -0.1) {
                if (-gamepad2.right_stick_y >= 0.1 && -gamepad2.right_stick_y < 0.5) {
                    stoneFlapServoDelta = 0.05;
                } else if (-gamepad2.right_stick_y >= 0.5) {
                    stoneFlapServoDelta = 0.10;
                } else if (-gamepad2.right_stick_y <= -0.1 && -gamepad2.right_stick_y > -0.5) {
                    stoneFlapServoDelta = -0.05;
                } else if (-gamepad2.right_stick_y <= -0.5) {
                    stoneFlapServoDelta = -0.10;
                }
            } else if (gamepad2.x) {
                stoneFlapServoDelta = -0.01;
            } else if (gamepad2.b) {
                stoneFlapServoDelta = 0.01;
            } else {
                stoneFlapServoDelta = 0.0;
            }

            if (gamepad2.a) {
                robot.stoneFlap.setPosition(0.0);
            }

            //Foundation puller servo control
            if (gamepad1.right_bumper) {
                foundationServoDelta = -0.02;
            } else if (gamepad1.left_bumper) {
                foundationServoDelta = 0.02;
            } else {
                foundationServoDelta = 0.0;
            }

            //--------------------------------
            // Assign values to hardware
            //--------------------------------

            // Motors
            robot.FLDrive.setPower(FLPower);
            robot.FRDrive.setPower(FRPower);
            robot.RLDrive.setPower(RLPower);
            robot.RRDrive.setPower(RRPower);
            robot.armMotor.setPower(armPower);

            // Servos
            robot.stoneFlap.setPosition(robot.stoneFlap.getPosition() + stoneFlapServoDelta);
            robot.leftFoundationServo.setPosition(robot.leftFoundationServo.getPosition() + foundationServoDelta);
            robot.rightFoundationServo.setPosition(1 - robot.leftFoundationServo.getPosition());

            //-----------------------------
            // Telemetry
            //-----------------------------

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Power Level", "powerMode: " + powerMode + " (" + powerCoefficient + ")");
            telemetry.addData("Drive Motors", "FL (%.2f), FR (%.2f), RL (%.2f), RR (%.2f)",
                    FLPower, FRPower, RLPower, RRPower);
            telemetry.addData("Arm Motor(s)", "arm_motor (%.2f)", armPower);
            telemetry.addData("Servo(s)", "stone_flap (%.2f), left_foundation (%.2f), right_foundation (%.2f)",
                    robot.stoneFlap.getPosition(), robot.leftFoundationServo.getPosition(), robot.rightFoundationServo.getPosition());
            telemetry.update();
        }
    }
}
