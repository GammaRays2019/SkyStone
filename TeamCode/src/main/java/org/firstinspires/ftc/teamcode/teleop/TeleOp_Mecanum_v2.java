
package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.hardware.HardwareMecanumSkyStone;

@TeleOp(name="ACHS Mecanum TeleOp v2", group="Linear OpMode")
//@Disabled
public class TeleOp_Mecanum_v2 extends LinearOpMode {

    // Declare OpMode members.
    private HardwareMecanumSkyStone robot   = new HardwareMecanumSkyStone();   // Use hardware from HardwareMecanumSkyStone
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Motor power constants
        final double ARM_POWER = 0.5;
        final double LOW_POWER = 0.25;
        final double MEDIUM_POWER = 0.5;
        final double HIGH_POWER = 1.0;

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
        double powerCoefficient = MEDIUM_POWER;
        String powerMode = "Medium";
        // To switch between power levels of arm motor
        double[] armPowerLevels = {0.25, ARM_POWER, 1.0};
        int armPowerIndex = 1;

        /*
        // Servo position constants
        final double STONE_FLAP_INIT = 0.5;      //Currently handled by Hardware Map
        final double FOUNDATION_SERVO_HOME = 0.0;
         */

        robot.init(hardwareMap);

        /*
        // Initialize servo positions
        robot.stoneFlap.setPosition(STONE_FLAP_INIT);
        robot.leftFoundationServo.setPosition(FOUNDATION_SERVO_HOME);   //Currently handled by Hardware Map
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
            double horizontal;
            if (gamepad1.dpad_left) {
                horizontal = -1*powerCoefficient;
            } else if (gamepad1.dpad_right) {
                horizontal = 1*powerCoefficient;
            } else {
                horizontal = gamepad1.left_stick_x;
            }
            double vertical   = -gamepad1.left_stick_y;
            double pivot      = gamepad1.right_stick_x;

            // Mecanum control
            FLPower = (pivot + (vertical + horizontal)) * powerCoefficient;
            FRPower = (-pivot + (vertical - horizontal)) * powerCoefficient;
            RLPower = (pivot + (vertical - horizontal)) * powerCoefficient;
            RRPower = (-pivot + (vertical + horizontal)) * powerCoefficient;

            //--------------------------------
            // Arm motor control
            //--------------------------------

            // Code for increasing or decreasing arm power level
            // Control on Gamepad 2
            // Press right_bumper to increase
            // Press left_bumper to decrease
            if (gamepad2.right_bumper){
                // Don't let the index value exceed the size of the array
                if (armPowerIndex >= armPowerLevels.length-1) {
                    armPowerIndex = armPowerLevels.length-1;
                } else {
                    armPowerIndex++;
                    sleep(100);
                }
            }
            if (gamepad2.left_bumper) {
                // Don't let the index value be lower than zero
                if (armPowerIndex <= 0) {
                    armPowerIndex = 0;
                } else {
                    armPowerIndex--;
                    sleep(100);
                }
            }

            armPower = (-gamepad2.left_stick_y * armPowerLevels[armPowerIndex]);
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

            //--------------------------------
            // Foundation puller servo control
            //--------------------------------
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
            telemetry.addLine("Arm Power Level: " + armPowerLevels[armPowerIndex] + " (" + armPowerLevels.length + " levels available" );
            telemetry.addData("Arm Motor(s)", "arm_motor (%.2f)", armPower);
            telemetry.addData("Servo(s)", "stone_flap (%.2f), left_foundation (%.2f), right_foundation (%.2f)",
                    robot.stoneFlap.getPosition(), robot.leftFoundationServo.getPosition(), robot.rightFoundationServo.getPosition());

//            telemetry.addData("armPowerIndex", armPowerIndex);
//            telemetry.addData("armPowerLevels[armPowerIndex]", armPowerLevels[armPowerIndex]);
//            telemetry.addData("armPowerLevels.length", armPowerLevels.length);

            telemetry.update();
        }
    }
}
