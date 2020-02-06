package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Auto Strafe: RepositionFoundation Short", group="Gamma Raider")
public class Auto__Mecanum_RepositionFoundationShort extends Auto__Mecanum_Base {

    @Override
    public void runOpMode() {
        initAuto();

        switch (autoMode) {
            case RED_ALLIANCE_BUILDING_SITE_SIDE:
                encoderDriveStraight(0.2, 3.0, 2.5); //Brayden Step 1

                encoderStrafe(DRIVE_SPEED, 31.5, 5.0); // Step 1 - Strafe Right 31.5cm //Brayden Step 2

                // Step 2 - Drive Forward 71cm //Brayden Step 3
                encoderDriveStraight(DRIVE_SPEED, 71.0, 5.0);

                sleep(500);
                setFoundationServoPosition(FOUNDATION_SERVO_UP); //Brayden Step 4
                sleep(500);

                // Step 3 - Drive Backwards 80cm //Brayden Step 5
                encoderDriveStraight(0.25, -85.0, 10.0);

                sleep(500);
                setFoundationServoPosition(FOUNDATION_SERVO_DOWN); //Brayden Step 6
                sleep(500);

                // Drive forwards to get off of wall
                encoderDriveStraight(0.25, 3.0, 5.0);

                encoderStrafe(DRIVE_SPEED, -74.5, 5.0); // Step 4 - Strafe Left 74.5cm //Brayden Step 7

                encoderDriveStraight(DRIVE_SPEED, 55, 5.0); //Brayden Step 8 //Previous value: 106.63

                encoderStrafe(DRIVE_SPEED, 25, 5.0); //Brayden Step 9 //Previous Value: 74.5

                encoderStrafe(DRIVE_SPEED, -55, 5.0); //Strafe Left to go under bridge

                //encoderDriveStraight(DRIVE_SPEED, -52.07, 5.0); //Brayden Step 10

                //encoderStrafe(DRIVE_SPEED, -115.57, 5.0); //Brayden Step 11

                break;
            case RED_ALLIANCE_DEPOT_SIDE:
                StrafeRightUnderBridge();
                break;
            case BLUE_ALLIANCE_BUILDING_SITE_SIDE:
                encoderDriveStraight(0.2, 3.0, 2.5); //Brayden Step 1

                encoderStrafe(DRIVE_SPEED, -31.5, 5.0); // Step 1 - Strafe Left 31.5cm //Brayden Step 2

                // Step 2 - Drive Forward 71cm //Brayden Step 3
                encoderDriveStraight(DRIVE_SPEED, 71.0, 5.0);

                sleep(500);
                setFoundationServoPosition(FOUNDATION_SERVO_UP); //Brayden Step 4
                sleep(500);

                // Step 3 - Drive Backwards 80cm //Brayden Step 5
                encoderDriveStraight(0.25, -85.0, 10.0);

                sleep(500);
                setFoundationServoPosition(FOUNDATION_SERVO_DOWN); //Brayden Step 6
                sleep(500);

                // Drive forwards to get off of wall
                encoderDriveStraight(0.25, 3.0, 5.0);

                encoderStrafe(DRIVE_SPEED, 74.5, 5.0); // Step 4 - Strafe Right 74.5cm //Brayden Step 7

                encoderDriveStraight(DRIVE_SPEED, 55, 5.0); //Brayden Step 8 //Previous value: 106.63

                encoderStrafe(DRIVE_SPEED, -25, 5.0); //Brayden Step 9 //Previous Value: 74.5

                encoderStrafe(DRIVE_SPEED, 55, 5.0); //Strafe Right to go under bridge

                //encoderDriveStraight(DRIVE_SPEED, -52.07, 5.0); //Brayden Step 10

                //encoderStrafe(DRIVE_SPEED, -115.57, 5.0); //Brayden Step 11

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
