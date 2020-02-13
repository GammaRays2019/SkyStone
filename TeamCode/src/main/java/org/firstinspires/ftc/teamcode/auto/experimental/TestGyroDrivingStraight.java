package org.firstinspires.ftc.teamcode.auto.experimental;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

//import org.firstinspires.ftc.teamcode.auto.Auto__Mecanum_Base;
import org.firstinspires.ftc.teamcode.auto.Auto__Mecanum_Base_IMU;

@Autonomous(name="Auto Strafe: TestGyroDrivingStraight", group="Gamma Raider")
public class TestGyroDrivingStraight extends Auto__Mecanum_Base_IMU {

    @Override
    public void runOpMode() {
        initAuto();

        switch (autoMode) {
            case RED_ALLIANCE_BUILDING_SITE_SIDE:
                encoderDriveStraight(0.5, 200, 30.0);
                break;
            case RED_ALLIANCE_DEPOT_SIDE:
                encoderDriveStraight(0.5, 200, 30.0);
                break;
            case BLUE_ALLIANCE_BUILDING_SITE_SIDE:
                encoderDriveStraight(0.5, 200, 30.0);
                break;
            case BLUE_ALLIANCE_DEPOT_SIDE:
                encoderDriveStraight(0.5, 200, 30.0);
                break;
            case MODE_NOT_SELECTED:
                //This one should not happen
                break;
        }

    }

}
