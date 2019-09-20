
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        final double ARM_POWER = 0.2;

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


        //power
        double leftPower = 0;
        double rightPower = 0;
        //double motor3Power = 0;
        //double motor4Power = 0;
        double armPower = 0;

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
                armPower = (-gamepad2.left_stick_y * 0.3);
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

            motor_1.setPower(leftPower);
            motor_2.setPower(rightPower);
            //motor_3.setPower(motor3Power);
            //motor_4.setPower(motor4Power);
            armMotor.setPower(armPower);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Drive Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.addData("Arm Motor(s)", "arm_motor (%.2f)", armPower);
            telemetry.update();
        }
    }
}
