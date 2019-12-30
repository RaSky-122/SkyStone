package org.firstinspires.ftc.teamcode.impl;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;

@Disabled
public class ArmCRServo_Impl extends LinearOpMode {

    private CRServo armCRServo;

    @Override
    public void runOpMode() throws InterruptedException {

    }

    public void init(String servoName){
        armCRServo = hardwareMap.crservo.get(servoName);
    }

    public void forwards (boolean button){

        if (button){
            armCRServo.setPower(1);
        }
        else {
            armCRServo.setPower(0);
        }
    }

    public void backwards (boolean button){

        if (button){
            armCRServo.setPower(-1);
        }
        else {
            armCRServo.setPower(0);
        }
    }
}
