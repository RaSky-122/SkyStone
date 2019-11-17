package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Disabled
public class ProgramuLuiRaca extends LinearOpMode {

    private Servo BunDeFolosit;

    @Override
    public void runOpMode() throws InterruptedException {

    }

    class RaCa{
        public void servo(){
            BunDeFolosit = hardwareMap.servo.get("1");
        }
    }
}
