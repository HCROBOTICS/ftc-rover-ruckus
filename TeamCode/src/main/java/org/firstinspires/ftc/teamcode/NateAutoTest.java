package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.hardware.NateHardware;


@TeleOp(name="Nate Autonomous Test", group="Nate")

public class NateAutoTest extends OpMode {
    NateHardware robot = new NateHardware();

    double lfReset = 0;
    double rfReset = 0;
    double lbReset = 0;
    double rbReset = 0;

    double lfDifference = (robot.omniWheels.getLfPosition() - lfReset);
    double rfDifference = (robot.omniWheels.getRfPosition() - rfReset);
    double lbDifference = (robot.omniWheels.getLbPosition() - lbReset);
    double rbDifference = (robot.omniWheels.getRbPosition() - rbReset);



    public static final double SERVO_DROP_POSITION = .7;
    public static final double SERVO_HOLD_POSITION = 0;

    @Override
    public void init() {

        robot.init(hardwareMap);
        //robot.teamPiece.setPosition(SERVO_HOLD_POSITION);
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Ready.");

    }

    /* Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY */
    @Override
    public void init_loop() {

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

        //if (gamepad1.y) robot.teamPiece.setPosition(SERVO_DROP_POSITION);
        //else robot.teamPiece.setPosition(SERVO_HOLD_POSITION);

        robot.elevator.elevate(gamepad1.right_trigger - gamepad1.left_trigger);

        telemetry.update();

        if (gamepad1.back) {
            lfReset = -robot.omniWheels.getLfPosition();
            rfReset = -robot.omniWheels.getRfPosition();
            lbReset = -robot.omniWheels.getLbPosition();
            rbReset = -robot.omniWheels.getRbPosition();
        }

        telemetry.addData("LF", lfDifference);
        telemetry.addData("RF", rfDifference);
        telemetry.addData("LB", lbDifference);
        telemetry.addData("RB", rbDifference);
        telemetry.addData("Right", (Math.abs((rfDifference + rbDifference) / 2)));
        telemetry.addData("Left", (Math.abs((lfDifference + lbDifference) / 2)));
        telemetry.addData("All", (Math.abs((rfDifference + lfDifference + rbDifference + lbDifference) / 4)));
        telemetry.update();
    }

    /* Code to run ONCE after the driver hits STOP */
    @Override
    public void stop() { robot.stop(); }
}