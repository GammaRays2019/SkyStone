
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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
    //private DcMotor motor_3 = null;
    //private DcMotor motor_4 = null;
    private DcMotor armMotor = null;
    //private CRServo stoneFlap = null;
    private Servo stoneFlap = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        final double ARM_POWER = 0.5;

        //Motors
        motor_1 = hardwareMap.get(DcMotor.class, "motor_1");
        motor_2 = hardwareMap.get(DcMotor.class, "motor_2");
        //motor_3 = hardwareMap.get(DcMotor.class, "motor_3");
        //motor_4 = hardwareMap.get(DcMotor.class, "motor_4");
        armMotor = hardwareMap.get(DcMotor.class, "arm_motor");


        motor_1.setDirection(DcMotor.Direction.FORWARD);
        motor_2.setDirection(DcMotor.Direction.REVERSE);
        //motor_3.setDirection(DcMotor.Direction.FORWARD);
        //motor_4.setDirection(DcMotor.Direction.FORWARD);
        armMotor.setDirection(DcMotor.Direction.REVERSE);

        //CR Servo
        //stoneFlap = hardwareMap.get(CRServo.class, "stone_flap");
        //stoneFlap.setDirection(CRServo.Direction.FORWARD);

        //Servo
        stoneFlap = hardwareMap.get(Servo.class, "stone_flap");

        //power
        double leftPower = 0;
        double rightPower = 0;
        //double motor3Power = 0;
        //double motor4Power = 0;
        double armPower = 0;

        double stoneFlapPosition = 0.5;

        stoneFlap.setPosition(stoneFlapPosition);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            leftPower  = -gamepad1.left_stick_y;
            rightPower = -gamepad1.right_stick_y;
            //motor3Power = -gamepad2.left_stick_y;
            //motor4Power = -gamepad2.right_stick_y;
            if (-gamepad2.right_stick_y <= -0.2)  {
                armPower = ARM_POWER;
            } else if(-gamepad2.right_stick_y >= 0.2) {
                armPower = -ARM_POWER;
            } else {
                armPower = (-gamepad2.left_stick_y * 0.6);
            }
            armPower = Range.clip(armPower, -1.0, 1.0);

             //power for motor 3 and 4
          //  if (gamepad1.left_bumper) {
          //      // motor power
          //      motor_3.setPower(0.4);
          //      motor_4.setPower(0.4);
          //  } else {
          //      //motor power
          //      motor_3.setPower(0);
          //      motor_4.setPower(0);
          //  }

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

            //stoneFlap Servo control
            if (gamepad2.dpad_left) {
                stoneFlapPosition = stoneFlap.getPosition() - 0.05;
            } else if (gamepad2.dpad_right) {
                stoneFlapPosition = stoneFlap.getPosition() + 0.05;
            }
            while (gamepad2.x) {
                stoneFlapPosition = stoneFlap.getPosition() - 0.05;
            }
            while (gamepad2.b) {
                stoneFlapPosition = stoneFlap.getPosition() + 0.05;
            }

            motor_1.setPower(leftPower);
            motor_2.setPower(rightPower);
            //motor_3.setPower(motor3Power);
            //motor_4.setPower(motor4Power);
            armMotor.setPower(armPower);

            stoneFlap.setPosition(stoneFlapPosition);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Drive Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.addData("Arm Motor(s)", "arm_motor (%.2f)", armPower);
            telemetry.addData("Servo(s)", "stone_flap (%.2f)", stoneFlap.getPosition());
            telemetry.update();
        }
    }
}
