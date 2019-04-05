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

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.LinearActuator;
import org.firstinspires.ftc.teamcode.OmniWheels;

public class SkylerHardware implements Hardware {


    public DcMotor lf = null; // Left Front wheel
    public DcMotor rf = null; // Right Front wheel
    public DcMotor lb = null; // Left Rear wheel
    public DcMotor rb = null; // Right Rear wheel
    public DcMotor motorElevator = null; // kind of implemented
    public DcMotor slide = null;
    public DcMotor slideLift = null;
    public CRServo sweeper = null;
    public Servo teamPiece = null;

    public OmniWheels omniWheels;
    public LinearActuator elevator;

    /* local OpMode members. */
    HardwareMap hwMap           = null;
    private ElapsedTime period  = new ElapsedTime();

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // This maps each object to a device in the hardware map.
        lf = hwMap.dcMotor.get("lf");
        rf = hwMap.dcMotor.get("rf");
        lb = hwMap.dcMotor.get("lb");
        rb = hwMap.dcMotor.get("rb");
        motorElevator = hwMap.dcMotor.get("elevator");
        slide = hwMap.dcMotor.get("slide");
        slideLift = hwMap.dcMotor.get("slide lift");
        sweeper = hwMap.crservo.get("sweeper");
        teamPiece = hwMap.servo.get("team piece");

        motorElevator.setDirection(DcMotor.Direction.FORWARD);

        // Make the motors stop abruptly when joystick is released
        //lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Set all motors to zero power
        motorElevator.setPower(0);
        slide.setPower(0);
        slideLift.setPower(0);
        sweeper.setPower(0);

        // This initializes the drivers for what I call the "complex devices" of our robot.
        omniWheels = new OmniWheels(lf, rf, lb, rb);
        omniWheels.init();
        elevator = new LinearActuator(motorElevator, -34000, 0);
        elevator.init();
    }

    public void stop() {
        omniWheels.stop();
        elevator.stop();
        slide.setPower(0);
        slideLift.setPower(0);
        sweeper.setPower(0);
    }
}