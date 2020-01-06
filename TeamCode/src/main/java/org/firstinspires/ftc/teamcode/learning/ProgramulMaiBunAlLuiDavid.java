package org.firstinspires.ftc.teamcode.learning;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Disabled

public class ProgramulMaiBunAlLuiDavid extends LinearOpMode {

    private Servo DavidSiRaca;

    @Override

    public void runOpMode() throws InterruptedException {
    }

    class ProfesionalCoachingCuRoibu{
        public void servo() {
            DavidSiRaca = hardwareMap.servo.get("racaFraier");
        }
    }
}
