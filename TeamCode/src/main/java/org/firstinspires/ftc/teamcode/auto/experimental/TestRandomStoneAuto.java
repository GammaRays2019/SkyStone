package org.firstinspires.ftc.teamcode.auto.experimental;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.auto.Auto__Mecanum_Base;

@Autonomous(name="Auto Strafe: TestRandomStoneAuto", group="Gamma Raider")
public class TestRandomStoneAuto extends Auto__Mecanum_Base {

    @Override
    public void runOpMode() {
        initAuto();

        switch (autoMode) {
            case RED_ALLIANCE_BUILDING_SITE_SIDE:
                //Nothing for now
                break;
            case RED_ALLIANCE_DEPOT_SIDE:
                ScoreRandomStoneRed();
                break;
            case BLUE_ALLIANCE_BUILDING_SITE_SIDE:
                //Nothing for now
                break;
            case BLUE_ALLIANCE_DEPOT_SIDE:
                //Nothing for now
                break;
            case MODE_NOT_SELECTED:
                //This one should not happen
                break;
        }

    }

}
