package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.HardwareGammaJR;

//@Disabled
@TeleOp(name="Gamma Jr.: Tank Drive (richie)", group="Pushbot")
public class GammaJR_richie extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    private HardwareGammaJR robot = new HardwareGammaJR();

    private final double STONE_SERVO_DELTA = 0.01;
    private final double FOUND_SERVO_DELTA = 0.01;

    private double stoneServoDelta = 0.0;
    private double foundServoDelta = 0.0;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        // Initialize Servos
        robot.stoneServo.setPosition(0.0);
        robot.leftFoundServo.setPosition(0.0);
        robot.rightFoundServo.setPosition(0.0);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            double left = gamepad1.left_stick_y;
            double right = gamepad1.right_stick_y;

            robot.leftDrive.setPower(left);
            robot.rightDrive.setPower(right);

            if (gamepad1.a) {
                stoneServoDelta = STONE_SERVO_DELTA;
            } else if (gamepad1.b) {
                stoneServoDelta = -STONE_SERVO_DELTA;
            } else {
                stoneServoDelta = 0;
            }

            if (gamepad1.x) {
                foundServoDelta = FOUND_SERVO_DELTA;
            } else if (gamepad1.y){
                foundServoDelta = -FOUND_SERVO_DELTA;
            } else {
                foundServoDelta = 0;
            }

            robot.stoneServo.setPosition(robot.stoneServo.getPosition() + stoneServoDelta);
            robot.leftFoundServo.setPosition(robot.leftFoundServo.getPosition() + foundServoDelta);
            robot.rightFoundServo.setPosition(robot.rightFoundServo.getPosition() - foundServoDelta);

        }
    }

}
