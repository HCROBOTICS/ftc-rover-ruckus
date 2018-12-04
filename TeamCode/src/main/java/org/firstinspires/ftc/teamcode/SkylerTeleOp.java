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
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Sklyer TeleOp", group="Skyler")
public class SkylerTeleOp extends OpMode {

    SkylerHardware robot = new SkylerHardware();
    
    double          lf_speed     = 0;
    double          rf_speed     = 0;
    double          lb_speed     = 0;
    double          rb_speed     = 0;
    
    double          lf_desired   = 0;
    double          rf_desired   = 0;
    double          lb_desired   = 0;
    double          rb_desired   = 0;
    
    final double WHEEL_MAX_SPEED = 0.5;

    boolean turnSweeper = false;
    boolean wasAJustPressed = true;

    @Override
    public void init() {
        /**
         * Initialize the hardware variables.
         * The init() method of the hardware class does all the work here.
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Robot", "Ready.");
        telemetry.addData("Driver", "Please select a drive mode. Defaulting to STRAFE.");
    }

    /* Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY */
    @Override
    public void init_loop() {
        if (gamepad1.a) {
            robot.omniWheels.mode = OmniWheels.DriveMode.STRAFE;
            telemetry.addData("Drive Mode", "STRAFE");
        } else if (gamepad1.b) {
            robot.omniWheels.mode = OmniWheels.DriveMode.JOHN;
            telemetry.addData("Drive Mode", "JOHN");
        }

        telemetry.update();
    }
    
    @Override
    public void start() {}

    /* Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP */
    @Override
    public void loop() {
        // Instead of eight lines of ugly code, we can move the robot with one using the OmniWheels class.
        robot.omniWheels.go(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        /*
        if (gamepad2.a) robot.sweeper.setPower(-0.5);
        else if (gamepad2.b) robot.sweeper.setPower(0);

        if (gamepad2.left_trigger > 0) robot.flipper.setPower(0.5 * gamepad2.left_trigger);
        else if (gamepad2.right_trigger > 0) robot.flipper.setPower(0.5 * -gamepad2.right_trigger);

        if (gamepad2.left_bumper) robot.dumper.setPosition(0.9);
        else if (gamepad2.right_bumper) robot.dumper.setPosition(0.1);

        if (gamepad2.x) robot.lift.setPower(0.35);
        else if (gamepad2.y) robot.lift.setPower(-0.35);
        else robot.lift.setPower(0); */
    }
    
    /* Code to run ONCE after the driver hits STOP */
    @Override
    public void stop() {}
}
