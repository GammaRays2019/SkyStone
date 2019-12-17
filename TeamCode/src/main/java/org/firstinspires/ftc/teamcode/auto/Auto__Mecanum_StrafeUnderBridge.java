package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Auto Strafe: Strafe Under Bridge", group="Gamma Raider")
public class Auto__Mecanum_StrafeUnderBridge extends Auto__Mecanum_Base {

    @Override
    public void runOpMode() {
        initAuto();

        switch (autoMode) {
            case RED_ALLIANCE_BUILDING_SITE_SIDE:
                StrafeLeftUnderBridge();
                break;
            case RED_ALLIANCE_DEPOT_SIDE:
                StrafeRightUnderBridge();
                break;
            case BLUE_ALLIANCE_BUILDING_SITE_SIDE:
                StrafeRightUnderBridge();
                break;
            case BLUE_ALLIANCE_DEPOT_SIDE:
                StrafeLeftUnderBridge();
                break;
            case MODE_NOT_SELECTED:
                //This one should not happen
                break;
        }
    }

}
