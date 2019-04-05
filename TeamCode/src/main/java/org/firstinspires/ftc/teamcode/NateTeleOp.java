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

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.hardware.NateHardware;


@TeleOp(name="Nate TeleOp", group="Nate")
public class NateTeleOp extends OpMode {
    NateHardware robot = new NateHardware();

    public static final double SERVO_DROP_POSITION = .7;
    public static final double SERVO_HOLD_POSITION = 0;

    @Override
    public void init() {

        /*
         * Initialize the hardware variables.
         * The init() method of the hardware class does all the work here.
         */

        robot.init(hardwareMap);
        //robot.teamPiece.setPosition(SERVO_HOLD_POSITION);
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Ready.");
        /* telemetry.addData("Driver", "Please select a drive mode. Defaulting to STRAFE.");
        robot.omniWheels.setMode(OmniWheels.DriveMode.STRAFE);
        telemetry.addData("Driver", "Please select a brake mode. Defaulting to BRAKE.");
        robot.omniWheels.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); */
    }

    /* Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY */
    @Override
    public void init_loop() {

        /*
        if (gamepad1.a) {
            robot.omniWheels.mode = OmniWheels.DriveMode.STRAFE;
            telemetry.addData("Drive Mode", "STRAFE");
        } else if (gamepad1.b) {
            robot.omniWheels.mode = OmniWheels.DriveMode.JOHN;
            telemetry.addData("Drive Mode", "JOHN");
        }

        if (gamepad1.x) {
            robot.omniWheels.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            telemetry.addData("Brake Mode", "FLOAT");
        } else if (gamepad1.y) {
            robot.omniWheels.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            telemetry.addData("Brake Mode", "BRAKE");
        }
        */

        robot.elevator.setModeDebug(true);
        robot.elevator.elevate(-gamepad1.right_stick_y);
        telemetry.addData("Elevator Pos", robot.elevator.getElevatorPosition());
        telemetry.addData("Desired Pos", robot.elevator.getDesiredPosition());
        telemetry.addData("Distance", robot.elevator.getDistance());

        if (gamepad1.start) {
            robot.elevator.zero();
        }

        telemetry.update();
    }

    @Override
    public void start() { robot.elevator.setModeDebug(false); }

    /* Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP */
    @Override
    public void loop() {
        robot.omniWheels.goByDriver(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        telemetry.addData("Elevator Pos", robot.elevator.getElevatorPosition());
        telemetry.addData("Desired Pos", robot.elevator.getDesiredPosition());
        telemetry.addData("Distance", robot.elevator.getDistance());
        telemetry.addData("Encoder Average", robot.omniWheels.getEncoderAverage());
        telemetry.update();

        //if (gamepad1.y) robot.teamPiece.setPosition(SERVO_DROP_POSITION);
        //else robot.teamPiece.setPosition(SERVO_HOLD_POSITION);

        robot.elevator.elevate(-gamepad1.right_stick_y);

        //robot.slide.setPower(0.5* ((gamepad2.right_bumper? 1:0) - (gamepad2.left_bumper? 1:0)));
        //robot.slideLift.setPower(1 * (gamepad2.right_trigger - gamepad2.left_trigger));

        //this is supposed to make the elevator automatically lift the robot when the "a" button is pressed
        /*
        if (gamepad1.a) {
            if (robot.elevator.getDistance() > robot.elevator.getDownPosition()) {

                robot.elevator.elevate(-1);
                telemetry.addData("Elevator Distance", robot.elevator.getDistance());
                telemetry.addData("Down Position", robot.elevator.getDownPosition());
                telemetry.addData("Difference", Math.abs(robot.elevator.getDistance() -
                        robot.elevator.getDownPosition()));
                telemetry.update();
            } robot.elevator.elevate(0);
        }
        */
        telemetry.update();
    }

    /* Code to run ONCE after the driver hits STOP */
    @Override
    public void stop() { robot.stop(); }
}