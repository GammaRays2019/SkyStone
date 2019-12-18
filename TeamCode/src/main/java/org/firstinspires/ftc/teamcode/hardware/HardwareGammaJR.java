package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class HardwareDummy {
    public ElapsedTime runtime = new ElapsedTime();
    public DcMotor rightDrive = null;
    public DcMotor leftDrive = null;

    HardwareMap hwMap = null;

    private ElapsedTime period  = new ElapsedTime();

    // Constructor
    public HardwareDummy () {

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
    }

}