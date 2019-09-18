
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="ACHS Drive", group="Linear OpMode")
//@Disabled
public class FTC_AtlanticCoast extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor motor_1 = null;
    private DcMotor motor_2 = null;
    //private DcMotor motor_3 = null;
    //private DcMotor motor_4 = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //Motors
        motor_1  = hardwareMap.get(DcMotor.class, "motor_1");
        motor_2 = hardwareMap.get(DcMotor.class, "motor_2");
        //motor_3 = hardwareMap.get(DcMotor.class, "motor_3");
        //motor_4 = hardwareMap.get(DcMotor.class, "motor_4");


        motor_1.setDirection(DcMotor.Direction.FORWARD);
        motor_2.setDirection(DcMotor.Direction.REVERSE);
        //motor_3.setDirection(DcMotor.Direction.FORWARD);
        //motor_4.setDirection(DcMotor.Direction.FORWARD);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            //power
            double leftPower;
            double rightPower;
            //double motor3Power;
            //double motor4Power;

            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            leftPower  = -gamepad1.left_stick_y;
            rightPower = -gamepad1.right_stick_y;
            //motor3Power = -gamepad2.left_stick_y;
            //motor4Power = -gamepad2.right_stick_y;

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

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.update();
        }
    }
}
