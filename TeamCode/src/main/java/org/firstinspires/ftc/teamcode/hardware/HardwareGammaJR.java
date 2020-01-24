package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class HardwareGammaJR {
    public ElapsedTime runtime = new ElapsedTime();
    public DcMotor rightDrive = null;
    public DcMotor leftDrive = null;
    public Servo   stoneServo = null;
    public Servo   leftFoundServo = null;
    public Servo   rightFoundServo = null;

    HardwareMap hwMap = null;

    private ElapsedTime period  = new ElapsedTime();

    // Constructor
    public HardwareGammaJR() {

    }


    public void init (HardwareMap aHwMap) {
        hwMap = aHwMap;

        leftDrive = hwMap.get(DcMotor.class, "motor_1");
        rightDrive = hwMap.get(DcMotor.class, "motor_2");
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        leftDrive.setPower(0);
        rightDrive.setPower(0);

        leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        stoneServo = hwMap.get(Servo.class, "stone_servo");
        leftFoundServo = hwMap.get(Servo.class, "l_found_servo");
        rightFoundServo = hwMap.get(Servo.class, "r_found_servo");
    }

}