package org.firstinspires.ftc.teamcode.impl;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Disabled
public class StoneCollector_Impl extends LinearOpMode {

    private DcMotor leftCollector;
    private DcMotor rightCollector;

    @Override
    public void runOpMode() throws InterruptedException {

    }

    class Init {
        public void initLeft(String motorName) {

            leftCollector = hardwareMap.dcMotor.get(motorName);
            leftCollector.setDirection(DcMotorSimple.Direction.FORWARD);
        }

        public void initRight(String motorName) {

            rightCollector = hardwareMap.dcMotor.get(motorName);
            rightCollector.setDirection(DcMotorSimple.Direction.REVERSE);
        }
    }

    public void initBoth (String leftName, String rightName){

        new Init().initLeft(leftName);
        new Init().initRight(rightName);
    }

    public void collect (boolean button){

        if (button){
            leftCollector.setPower(0.7);
            rightCollector.setPower(0.7);
        }
        else {
            leftCollector.setPower(0);
            rightCollector.setPower(0);
        }
    }

    public void throwOut (boolean button){

        if (button){
            leftCollector.setPower(-0.7);
            rightCollector.setPower(-0.7);
        }
        else {
            leftCollector.setPower(0);
            rightCollector.setPower(0);
        }
    }
}
