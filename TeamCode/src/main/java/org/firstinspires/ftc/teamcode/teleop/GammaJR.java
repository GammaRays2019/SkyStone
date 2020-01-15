package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.hardware.HardwareGammaJR;

@TeleOp(name="Gamma Jr.: Tank Drive", group="Pushbot")
public class GammaJR extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    private HardwareGammaJR robot = new HardwareGammaJR();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            double left = gamepad1.left_stick_y;
            double right = gamepad1.right_stick_y;

            robot.leftDrive.setPower(left);
            robot.rightDrive.setPower(right); //Comment

        }
    }

}
