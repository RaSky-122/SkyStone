package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class AutonomousMode_INCESA extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        while(!opModeIsActive() && !isStopRequested()){
            telemetry.addData("Waiting for ", "start.");
            telemetry.update();
        }

        if(opModeIsActive()){

        }
    }
}
