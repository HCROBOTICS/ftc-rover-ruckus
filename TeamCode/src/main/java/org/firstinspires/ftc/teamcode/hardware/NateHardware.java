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
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.LinearActuator;
import org.firstinspires.ftc.teamcode.OmniWheels;

public class NateHardware implements Hardware {

    public static final double SERVO_HOLD_POSITION = 0.0;

    public DcMotor lf = null; // Left Front wheel
    public DcMotor rf = null; // Right Front wheel
    public DcMotor lb = null; // Left Rear wheel
    public DcMotor rb = null; // Right Rear wheel
    public OmniWheels omniWheels = null;
    //public Servo teamPiece = null;
    public DcMotor slide = null;
    public DcMotor slideLift = null;

    public DcMotor motorElevator = null;
    public LinearActuator elevator = null;

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
        motorElevator = hwMap.dcMotor.get("elevator");
        //teamPiece = hwMap.servo.get("teamPiece");
        slide = hwMap.dcMotor.get("slide");
        slideLift = hwMap.dcMotor.get("slide lift");

        /*
        When the arm gets mounted to our robot, the following code will be implemented.

        Slide = hwMap.dcMotor.get("slide");
        SlideLift = hwMap.dcMotor.get("slideLift");
        */

        motorElevator.setDirection(DcMotor.Direction.FORWARD);
        //teamPiece.setPosition(SERVO_HOLD_POSITION);

        /*
        When the arm gets mounted to our robot, the following code will be implemented.

        Slide.setDirection(DcMotor.Direction.FORWARD);
        SlideLift.setDirection(DcMotor.Direction.FORWARD);
         */

        // make the motors stop abruptly when joystick is released
        //lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        // Set all motors to zero power
        motorElevator.setPower(0);
        slide.setPower(0);
        slideLift.setPower(0);

        // Set all motors to run without encoders.
        motorElevator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Initialize the omni-wheels "driver".
        omniWheels = new OmniWheels(lf, rf, lb, rb);
        omniWheels.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        omniWheels.init();
        elevator = new LinearActuator(motorElevator, -38500, 0);
        elevator.init();
    }

    public void stop() {
        omniWheels.stop();
        elevator.stop();
    }
}