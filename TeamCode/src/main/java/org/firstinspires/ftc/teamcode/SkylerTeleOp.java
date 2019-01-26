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

import org.firstinspires.ftc.teamcode.hardware.SkylerHardware;

@TeleOp(name="Sklyer TeleOp", group="Skyler")
public class SkylerTeleOp extends OpMode {
    SkylerHardware robot = new SkylerHardware();

    public static final double SERVO_DROP_POSITION = 1;
    public static final double SERVO_HOLD_POSITION = 0;

    private boolean isSweeperRunning;
    private boolean isAPressed;
    private boolean isBPressed;

    private void toggleSweeper(double power) {
        if (isSweeperRunning) {
            robot.sweeper.setPower(0);
            isSweeperRunning = false;
        } else {
            robot.sweeper.setPower(power);
            isSweeperRunning = true;
        }
    }

    @Override
    public void init() {
        /*
         * Initialize the hardware variables.
         * The init() method of the hardware class does all the work here.
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Robot", "Ready.");
        telemetry.addData("Driver", "Please select a drive mode. Defaulting to STRAFE.");
        robot.omniWheels.setMode(OmniWheels.DriveMode.STRAFE);
        telemetry.addData("Driver", "Please select a brake mode. Defaulting to FLOAT.");
        robot.omniWheels.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    /* Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY */
    @Override
    public void init_loop() {

        robot.teamPiece.setPosition(SERVO_HOLD_POSITION);

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

        robot.elevator.setModeDebug(true);
        robot.elevator.elevate(-gamepad1.right_stick_y);
        telemetry.addData("Elevator Pos", robot.elevator.getElevatorPosition());
        telemetry.addData("Desired Pos", robot.elevator.getDesiredPosition());
        telemetry.addData("Distance", robot.elevator.getDistance());
        if (gamepad1.start) robot.elevator.zero();

        telemetry.update();
    }

    @Override
    public void start() {
        robot.elevator.setModeDebug(false);
    }

    /* Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP */
    @Override
    public void loop() {

        robot.teamPiece.setPosition(SERVO_HOLD_POSITION);

        robot.omniWheels.goByDriver(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
        robot.elevator.elevate(gamepad1.right_stick_y);
        if (gamepad1.a) {
            if (!isAPressed) {
                isAPressed = true;
                toggleSweeper(0.5);
            }
        } else if (gamepad1.b) {
            if (!isBPressed) {
                isBPressed = true;
                toggleSweeper(-0.5);
            }
        } else {
            isAPressed = false;
            isBPressed = false;
        }

        robot.slide.setPower((gamepad1.right_bumper? 1 : 0) - (gamepad1.left_bumper? 1 : 0));

        robot.slideLift.setPower(0.8 * (gamepad1.right_trigger - gamepad1.left_trigger));

        telemetry.addData("Elevator Pos", robot.elevator.getElevatorPosition());
        telemetry.addData("Desired Pos", robot.elevator.getDesiredPosition());
        telemetry.addData("Distance", robot.elevator.getDistance());
        telemetry.update();
    }

    /* Code to run ONCE after the driver hits STOP */
    @Override
    public void stop() {
        robot.stop();
    }
}
