package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Auto Strafe: Strafe L-Shape Under Bridge", group="Gamma Raider")
public class Auto__Mecanum_StrafeLShapeUnderBridge extends Auto__Mecanum_Base {

    @Override
    public void runOpMode() {
        initAuto();

        switch (autoMode) {
            case RED_ALLIANCE_BUILDING_SITE_SIDE:
                StrafeLeftUnderBridgeLShape();
                break;
            case RED_ALLIANCE_DEPOT_SIDE:
                StrafeRightUnderBridgeLShape();
                break;
            case BLUE_ALLIANCE_BUILDING_SITE_SIDE:
                StrafeRightUnderBridgeLShape();
                break;
            case BLUE_ALLIANCE_DEPOT_SIDE:
                StrafeLeftUnderBridgeLShape();
                break;
            case MODE_NOT_SELECTED:
                //This one should not happen
                break;
        }
    }

}
