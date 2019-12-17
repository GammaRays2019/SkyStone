package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Auto Strafe: RepositionFoundation", group="Gamma Raider")
public class Auto__Mecanum_RepositionFoundation extends Auto__Mecanum_Base {

    @Override
    public void runOpMode() {
        initAuto();

        switch (autoMode) {
            case RED_ALLIANCE_BUILDING_SITE_SIDE:
                //encoderDrive(DRIVE_SPEED, 3.0, 3.0, 2.5);
                encoderDriveStraight(0.2, 3.0, 2.5); //Brayden Step 1

                encoderStrafe(DRIVE_SPEED, 31.5, 5.0); // Step 1 - Strafe Right 31.5cm //Brayden Step 2

                //encoderDrive(DRIVE_SPEED, 71.0, 71.0, 5.0); // Step 2 - Drive Forward 71cm //Brayden Step 3
                encoderDriveStraight(DRIVE_SPEED, 71.0, 5.0);

                sleep(500);
                setFoundationServoPosition(FOUNDATION_SERVO_UP); //Brayden Step 4
                sleep(500);

                //encoderDrive(DRIVE_SPEED, -74.0, -74.0, 5.0); // Step 3 - Drive Backwards 74cm //Brayden Step 5
                encoderDriveStraight(DRIVE_SPEED, -74.0, 5.0);

                sleep(500);
                setFoundationServoPosition(FOUNDATION_SERVO_DOWN); //Brayden Step 6
                sleep(500);

                encoderStrafe(DRIVE_SPEED, -74.5, 5.0); // Step 4 - Strafe Left 74.5cm //Brayden Step 7

                //New Steps 12/13/2019 BELOW

                encoderDriveStraight(DRIVE_SPEED, 106.63, 5.0); //Brayden Step 8

                encoderStrafe(DRIVE_SPEED, 74.5, 5.0); //Brayden Step 9

                encoderDriveStraight(DRIVE_SPEED, -52.07, 5.0); //Brayden Step 10

                encoderStrafe(DRIVE_SPEED, -115.57, 5.0); //Brayden Step 11

//                sleep(3000); //temporary Pause
//
//                //encoderDrive(DRIVE_SPEED, 55.0, 55.0, 5.0); // Step 5 - Drive Forward 55cm
//                encoderDriveStraight(DRIVE_SPEED, 45.0, 5.0);
//
//                encoderStrafe(DRIVE_SPEED, 14.5, 5.0); // Step 6 - Strafe Right 14.5
//
//                encoderStrafe(DRIVE_SPEED, -61.5, 5.0); // Step 7 - Strafe Left 61.5cm
                break;
            case RED_ALLIANCE_DEPOT_SIDE:
                StrafeRightUnderBridge();
                break;
            case BLUE_ALLIANCE_BUILDING_SITE_SIDE:
                //encoderDrive(DRIVE_SPEED, 3.0, 3.0, 2.5);
                encoderDriveStraight(0.2, 3.0, 2.5); //Brayden Step 1

                encoderStrafe(DRIVE_SPEED, -31.5, 5.0); // Step 1 - Strafe Left 31.5cm //Brayden Step 2

                //encoderDrive(DRIVE_SPEED, 71.0, 71.0, 5.0); // Step 2 - Drive Forward 71cm //Brayden Step 3
                encoderDriveStraight(DRIVE_SPEED, 71.0, 5.0);

                sleep(500);
                setFoundationServoPosition(FOUNDATION_SERVO_UP); //Brayden Step 4
                sleep(500);

                //encoderDrive(DRIVE_SPEED, -74.0, -74.0, 5.0); // Step 3 - Drive Backwards 74cm //Brayden Step 5
                encoderDriveStraight(DRIVE_SPEED, -74.0, 5.0);

                sleep(500);
                setFoundationServoPosition(FOUNDATION_SERVO_DOWN); //Brayden Step 6
                sleep(500);

                encoderStrafe(DRIVE_SPEED, 74.5, 5.0); // Step 4 - Strafe Right 74.5cm //Brayden Step 7

                //New Steps 12/13/2019 BELOW

                encoderDriveStraight(DRIVE_SPEED, 106.63, 5.0); //Brayden Step 8

                encoderStrafe(DRIVE_SPEED, -74.5, 5.0); //Brayden Step 9

                encoderDriveStraight(DRIVE_SPEED, -52.07, 5.0); //Brayden Step 10

                encoderStrafe(DRIVE_SPEED, 115.57, 5.0); //Brayden Step 11

//                sleep(3000); //temporary Pause
//
//                //encoderDrive(DRIVE_SPEED, 55.0, 55.0, 5.0); // Step 5 - Drive Forward 55cm
//                encoderDriveStraight(DRIVE_SPEED, 45.0, 5.0);
//
//                encoderStrafe(DRIVE_SPEED, -14.5, 5.0); // Step 6 - Strafe Left 14.5
//
//                encoderStrafe(DRIVE_SPEED, 61.5, 5.0); // Step 7 - Strafe Right 61.5cm
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
