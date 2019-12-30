package org.firstinspires.ftc.teamcode.impl;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Disabled
public class CatchServo_Impl extends LinearOpMode {

    private Servo catchServo;

    @Override
    public void runOpMode() throws InterruptedException {

    }

    public void init(String servoNamme){
        catchServo = hardwareMap.servo.get(servoNamme);
    }

    public void grab (boolean button){
        if(button){
            catchServo.setPosition(catchServo.getPosition() - 0.01);
        }
    }

    public void drop (boolean button){
        if (button){
            catchServo.setPosition(catchServo.getPosition() + 0.01);
        }
    }
}
