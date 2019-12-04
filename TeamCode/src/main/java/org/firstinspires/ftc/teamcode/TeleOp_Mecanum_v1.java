
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.hardware.HardwareMecanumSkyStone;

@TeleOp(name="ACHS Mecanum TeleOp", group="Linear OpMode")
//@Disabled
public class TeleOp_Mecanum_v1 extends LinearOpMode {

    // Declare OpMode members.
    private org.firstinspires.ftc.teamcode.hardware.HardwareMecanumSkyStone robot   = new HardwareMecanumSkyStone();   // Use hardware from HardwareMecanumSkyStone
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

        // To switch between power levels of drive motors
        boolean highPower = false;

        // Define and  initialize power variables
        double FLPower  = 0;
        double FRPower  = 0;
        double RLPower  = 0;
        double RRPower  = 0;
        double armPower = 0;
        double powerCoefficient = 0.5;

        // Servo position constants
        final double STONE_FLAP_INIT = 0.5;
        final double FOUNDATION_SERVO_HOME = 0.0;

        robot.init(hardwareMap);

        /*
        // Initialize servo positions
        robot.stoneFlap.setPosition(STONE_FLAP_INIT);
        robot.leftFoundationServo.setPosition(FOUNDATION_SERVO_HOME);
        robot.rightFoundationServo.setPosition(1 - FOUNDATION_SERVO_HOME);
         */

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            //------------------------------
            // Drive-train control
            //------------------------------

            // Code for switching between 2 drive power modes
            // Press A to switch to highPower mode (highPower = true)
            // Press B to switch to low-power mode (highPower = false)
            if (gamepad1.a) {
                highPower = true;
                sleep(50);
            }
            if (gamepad1.b) {
                highPower = false;
                sleep(50);
            }

            if (highPower) {
                powerCoefficient = 1;
            } else {
                powerCoefficient = 0.5;
            }

            // Mecanum drive variables
            double horizontal = gamepad1.right_stick_x;
            double vertical   = -gamepad1.right_stick_y;
            double pivot      = gamepad1.left_stick_x;

            // Mecanum control
            FLPower = (pivot + (vertical - horizontal)) * powerCoefficient;
            FRPower = (-pivot + (vertical + horizontal)) * powerCoefficient;
            RLPower = (pivot + (vertical + horizontal)) * powerCoefficient;
            RRPower = (-pivot + (vertical - horizontal)) * powerCoefficient;

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
            telemetry.addData("highPower", "True/False: " + highPower);
            telemetry.addData("Drive Motors", "FL (%.2f), FR (%.2f), RL (%.2f), RR (%.2f)",
                    FLPower, FRPower, RLPower, RRPower);
            telemetry.addData("Arm Motor(s)", "arm_motor (%.2f)", armPower);
            telemetry.addData("Servo(s)", "stone_flap (%.2f), left_foundation (%.2f), right_foundation (%.2f)",
                    robot.stoneFlap.getPosition(), robot.leftFoundationServo.getPosition(), robot.rightFoundationServo.getPosition());
            telemetry.update();
        }
    }
}
