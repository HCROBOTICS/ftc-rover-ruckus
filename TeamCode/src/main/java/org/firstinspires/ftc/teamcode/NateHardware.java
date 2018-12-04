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

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.OmniWheels;

public class NateHardware {
    public static final double MID_SERVO       =  0.4 ;
    public static final double ARM_UP_POWER    =  1 ;
    public static final double ARM_DOWN_POWER  = -1 ;

    public DcMotor lf = null; // Left Front wheel
    public DcMotor rf = null; // Right Front wheel
    public DcMotor lb = null; // Left Rear wheel
    public DcMotor rb = null; // Right Rear wheel
    /* These are for moving the arm. */
    public DcMotor shoulder = null; // The one on the robot that connects the arm to the body
    public DcMotor elbow = null; // The one in the middle of the arm

    public OmniWheels omniWheels = null;
    public NateGrabber grabber = null;

    public Servo lServo = null;
    public Servo rServo = null;
    public Servo midServo = null;

    /* local OpMode members. */
    HardwareMap hwMap           = null;
    private ElapsedTime period  = new ElapsedTime();

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        lf = hwMap.dcMotor.get("lf");
        rf = hwMap.dcMotor.get("rf");
        lb = hwMap.dcMotor.get("lb");
        rb = hwMap.dcMotor.get("rb");
        shoulder = hwMap.dcMotor.get("shoulder");
        elbow = hwMap.dcMotor.get("elbow");
        lServo = hwMap.servo.get("left servo");
        rServo = hwMap.servo.get("right servo");
        midServo = hwMap.servo.get("mid servo");
        lf.setDirection(DcMotor.Direction.REVERSE);
        rf.setDirection(DcMotor.Direction.FORWARD);
        lb.setDirection(DcMotor.Direction.REVERSE);
        rb.setDirection(DcMotor.Direction.FORWARD);
        shoulder.setDirection(DcMotor.Direction.FORWARD);
        elbow.setDirection(DcMotor.Direction.FORWARD);
        // Set all motors to zero power
        lf.setPower(0);
        rf.setPower(0);
        lb.setPower(0);
        rb.setPower(0);
        shoulder.setPower(0);
        elbow.setPower(0);
        // Set all motors to run without encoders.
        lf.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shoulder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        elbow.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        // Initialize the omni-wheels "driver".
        omniWheels = new OmniWheels(lf, rf, lb, rb, OmniWheels.DriveMode.STRAFE);
        grabber = new NateGrabber(lServo, rServo, midServo);
        grabber.init();

    }

    public void stop() {
        grabber.stop();
    }
}