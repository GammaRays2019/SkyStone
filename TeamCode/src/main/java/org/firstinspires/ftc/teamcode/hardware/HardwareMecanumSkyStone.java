/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 */
public class HardwareMecanumSkyStone
{
    /* Public OpMode members. */
    public ElapsedTime runtime = new ElapsedTime();
    public DcMotor FLDrive   = null; // Front Left Drive
    public DcMotor FRDrive   = null; // Front Right Drive
    public DcMotor RLDrive   = null; // Rear Left Drive
    public DcMotor RRDrive   = null; // Rear Right Drive
    public DcMotor armMotor  = null;
    public Servo   stoneFlap = null;
    public Servo   leftFoundationServo = null;
    public Servo   rightFoundationServo = null;

    public static final double MID_SERVO       =  0.5 ;
    public static final double STONE_FLAP_HOME = 0.5 ;
    public static final double FOUNDATION_SERVO_HOME = 0.0;
    public static final double ARM_UP_POWER    =  0.45 ;
    public static final double ARM_DOWN_POWER  = -0.45 ;

    /* local OpMode members. */
    HardwareMap hwMap           = null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public HardwareMecanumSkyStone(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        FLDrive = hwMap.get(DcMotor.class, "drive_fl"); // Front Left Drive Motor
        FRDrive = hwMap.get(DcMotor.class, "drive_fr"); // Front Right Drive Motor
        RLDrive = hwMap.get(DcMotor.class, "drive_rl"); // Rear Left Drive Motor
        RRDrive = hwMap.get(DcMotor.class, "drive_rr"); // Rear Right Drive Motor
        armMotor = hwMap.get(DcMotor.class, "arm_motor");
        FLDrive.setDirection(DcMotor.Direction.FORWARD);
        FRDrive.setDirection(DcMotor.Direction.REVERSE);
        RLDrive.setDirection(DcMotor.Direction.FORWARD);
        RRDrive.setDirection(DcMotor.Direction.REVERSE);
        armMotor.setDirection(DcMotor.Direction.FORWARD);

        // Set all motors to zero power
        FLDrive.setPower(0);
        FRDrive.setPower(0);
        RLDrive.setPower(0);
        RRDrive.setPower(0);
        armMotor.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        FLDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FRDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RLDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RRDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Define and initialize ALL installed servos.
        stoneFlap = hwMap.get(Servo.class, "stone_flap");
        leftFoundationServo = hwMap.get(Servo.class, "left_foundation");
        rightFoundationServo = hwMap.get(Servo.class, "right_foundation");
        stoneFlap.setPosition(MID_SERVO);
        leftFoundationServo.setPosition(FOUNDATION_SERVO_HOME);
        rightFoundationServo.setPosition(1 - FOUNDATION_SERVO_HOME);
    }
 }

