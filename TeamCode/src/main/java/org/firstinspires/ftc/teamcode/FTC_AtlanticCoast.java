
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="ACHS Drive", group="Linear OpMode")
//@Disabled
public class FTC_AtlanticCoast extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor motor_1 = null;
    private DcMotor motor_2 = null;
    private DcMotor armMotor = null;
    //private CRServo stoneFlap = null;
    private Servo stoneFlap = null;

    /*
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Button State Change detectors for switching between high and low power modes//Adapted from Arduino "State Change Detection" Tutorial//
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private int aButtonChangeCounter = 0;
    private boolean aButtonState = false;
    private boolean lastAButtonState = false;
    private int bButtonChangeCounter = 0;
    private boolean bButtonState = false;
    private boolean lastBButtonState = false;
    //^ as of 2019/09/27, currently using one button (a) as toggle, to utilize two buttons, uncomment code below
    */

    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //Motor power constants
        final double ARM_POWER = 0.5;

        //Motors
        motor_1 = hardwareMap.get(DcMotor.class, "motor_1");
        motor_2 = hardwareMap.get(DcMotor.class, "motor_2");
        armMotor = hardwareMap.get(DcMotor.class, "arm_motor");

        motor_1.setDirection(DcMotor.Direction.FORWARD);
        motor_2.setDirection(DcMotor.Direction.REVERSE);
        armMotor.setDirection(DcMotor.Direction.FORWARD);

        //CR Servo
        //stoneFlap = hardwareMap.get(CRServo.class, "stone_flap");
        //stoneFlap.setDirection(CRServo.Direction.FORWARD);

        //Servo
        stoneFlap = hardwareMap.get(Servo.class, "stone_flap");
        double stoneFlapServoDelta = 0;

        //To switch between power levels of drive motors
        boolean highPower = false;

        //Define and  initialize power variables
        double leftPower = 0;
        double rightPower = 0;
        double armPower = 0;

        //Servo position constants
        final double STONE_FLAP_INIT = 0.5;

        //Initialize servo positions
        stoneFlap.setPosition(STONE_FLAP_INIT);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            //------------------------------
            // Drive-train control
            //------------------------------

            //Code for switching between 2 drive power modes

            /*
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Button State Change detectors for switching between high and low power modes//Adapted from Arduino "State Change Detection" Tutorial//
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            aButtonState = gamepad1.a;
            //bButtonState = gamepad1.b;
            //Compare the button state to its previous state
            if (aButtonState != lastAButtonState) {
                //if the current state is true, then the button
                aButtonChangeCounter++;
                //Delay a little bit to avoid bouncing??? -- but we are handling bouncing, so this may not be necessary now
                //sleep(50);
            }
            //save the current state as the last state, for the next time through the loop
            lastAButtonState = aButtonState;
            */

            /*
            if (bButtonState != lastBButtonState) {
                //if the current state is true, then the button
                bButtonChangeCounter++;
                //Delay a little bit to avoid bouncing??? -- but we are handling bouncing, so this may not be necessary now
                //sleep(50);
            }
            //save the current state as the last state, for the next time through the loop
            lastBButtonState = bButtonState;
            */

            /*
            //switches highPower between true and false every 2 changes in button state by checking the modulo of the
            //button change counter. (modulo gives remainder of division)
            if (aButtonChangeCounter % 2 == 0) {
                if (highPower == false) {
                    highPower = true;
                } else {
                    highPower = false;
                }
            }
            */

            /*
            if (bButtonChangeCounter % 2 == 0) {
                highPower = false;
            }
            */

            //Press A to switch to highPower mode (highPower = true)
            //Press B to switch to low-power mode (highPower = false)
            if (gamepad1.a) {
                highPower = true;
                sleep(50);
            }
            if (gamepad1.b) {
                highPower = false;
                sleep(50);
            }

            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            if (highPower == true) {
                leftPower = -gamepad1.left_stick_y;
                rightPower = -gamepad1.right_stick_y;
            } else {
                leftPower = (-gamepad1.left_stick_y * 0.5);
                rightPower = (-gamepad1.right_stick_y * 0.5);
            }

            //--------------------------------
            // Arm motor control
            //--------------------------------
            armPower = (-gamepad2.left_stick_y * ARM_POWER);
            armPower = Range.clip(armPower, -1.0, 1.0);

            //--------------------------------
            // stoneFlap Servo control
            //--------------------------------
            /*
            //stoneFlap CRServo control
            if (gamepad2.dpad_left) {
                stoneFlap.setPower(-1.0);
            } else if (gamepad2.dpad_right) {
                stoneFlap.setPower(1.0);
            } else {
                stoneFlap.setPower(0.0);
            }
            */
            //May want to make an alternate version of the teleop thst splits the TeleOp program into two threads (FTC_AtlanticCoast_multithreaded ?)
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
                stoneFlap.setPosition(0.0);
            }

            motor_1.setPower(leftPower);
            motor_2.setPower(rightPower);
            armMotor.setPower(armPower);

            stoneFlap.setPosition(stoneFlap.getPosition() + stoneFlapServoDelta);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("highPower", "True/False: " + highPower);
            telemetry.addData("Drive Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.addData("Arm Motor(s)", "arm_motor (%.2f)", armPower);
            telemetry.addData("Servo(s)", "stone_flap (%.2f)", stoneFlap.getPosition());
            telemetry.update();
        }
    }
}
