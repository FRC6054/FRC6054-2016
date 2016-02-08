
package org.usfirst.frc.team6054.robot;


import edu.wpi.first.wpilibj.CameraServer;											//importing classes
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.vision.USBCamera;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;
import com.ni.vision.NIVision;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.Ultrasonic.Unit;



/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */



public class Robot extends IterativeRobot {                                                      //naming all of the inputs to the computer and the roboRio

	DriverStation station = DriverStation.getInstance();
	
	Joystick Left = new Joystick(0);
	Joystick Right = new Joystick(1);
	
	Victor LeftMotor1 = new Victor(0);
	Victor LeftMotor2 = new Victor(1);
	Victor RightMotor1 = new Victor(2);
	Victor RightMotor2 = new Victor(3);
	
	VictorSP ballMotor = new VictorSP(4);
	
	VictorSP LEDRing = new VictorSP(7);

	Servo yaw = new Servo(8);
	Servo pitch = new Servo(9);
	
	CameraServer server;	
	USBCamera camera = new USBCamera ("cam1");
	NIVision.Image frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
	
	Timer Timer = new Timer(); 

	public void robotInit() {

		camera.openCamera();											//initializing the USBCamera
		camera.startCapture();
		camera.getImage(frame);
		CameraServer.getInstance().setImage(frame);
		server = CameraServer.getInstance();
		server.setQuality(50);
		server.startAutomaticCapture(camera);
	    	
			}


	public void disabledInit(){

    }
	
	public void disabledPeriodic() {
	
	}

	public void autonomousInit() {
    	
    	camera.setExposureManual(0);										//setting camera settings for green finder
    	camera.setWhiteBalanceManual(7366);
    	camera.updateSettings();
    	LEDRing.set(1.0); 
     	
     	Timer.start();														//starts timer for autonomous wait command

    }

	public void autonomousPeriodic() {
    
	  													
    	NetworkTable.setServerMode();										//sets networktables as server to send data
    	NetworkTable datatable = NetworkTable.getTable("datatable");		//naming the COG capture variables from roborealm
		double x = datatable.getNumber("COG_X", 0.0);
    	double y = datatable.getNumber("COG_Y", 0.0);


    	NetworkTable corners = NetworkTable.getTable("corners");			//naming the corner capture variables from roborealm
    	double NEX = corners.getNumber("NORTHEAST_X", 0.0);
    	double NEY = corners.getNumber("NORTHEAST_Y", 0.0);
    	double NWX = corners.getNumber("NORTHWEST_X", 0.0);
    	double NWY = corners.getNumber("NORTHWEST_Y", 0.0);
    	double SEX = corners.getNumber("SOUTHEAST_X", 0.0);
    	double SEY = corners.getNumber("SOUTHEAST_Y", 0.0);
    	double SWX = corners.getNumber("SOUTHWEST_X", 0.0);
    	double SWY = corners.getNumber("SOUTHWEST_Y", 0.0);
    	
    		LeftMotor1.set(0.3);
    		LeftMotor2.set(0.3);
    		RightMotor1.set(-0.3);			// get over ramp
    		RightMotor2.set(-0.3);
    		Timer.delay(5);	
    		
    		LeftMotor1.set(0);
    		LeftMotor2.set(0);
    		RightMotor1.set(0);
    		RightMotor2.set(0);		//temp wait
    		Timer.delay(3);
    		
    		yaw.set(0.2);
    		pitch.set(0.6);			// this doesnt work  :(
    		
    		LEDRing.set(1.0);    //turns on LED to see the green U - shape
        	
    		
//    	while (SEY < SWY && isAutonomous()) {
//    		SEY = corners.getNumber("SOUTHEAST_Y", 0.0);
//    		SWY = corners.getNumber("SOUTHWEST_Y", 0.0);		// while not lineed up keep going strait
//        	LeftMotor1.set(0.3);
//        	LeftMotor2.set(0.3);
//        	RightMotor1.set(-0.3);
//        	RightMotor2.set(-0.3);
//        	}	

//    	LeftMotor1.set(0);
//		LeftMotor2.set(0);
//		RightMotor1.set(0);		//temp wait
//		RightMotor2.set(0);
//		Timer.delay(3);
		
    	yaw.set(0.5);
    	Timer.delay(.5);
    	
    	
    	
    	
    	while (((x>380 || x<340) && x>0) && isAutonomous()) {
    		x = datatable.getNumber("COG_X", 0.0);                             //getting lined up
    		if (x>380) {
    			LeftMotor1.set(0.3);
            	LeftMotor2.set(0.3);
            	RightMotor1.set(0);                                          //going right
            	RightMotor2.set(0);
    		}
    		if (x<340) {
    			LeftMotor1.set(0);
            	LeftMotor2.set(0);
            	RightMotor1.set(-0.3);                                        //going left
            	RightMotor2.set(-0.3);
    		}
    	}
    	
    	
    	while (y<450 && isAutonomous()) {
    		x = datatable.getNumber("COG_X", 0.0);                             // towards the goal
    		y = datatable.getNumber("COG_Y", 0.0);
    		if (x<380 && x>340) {
    			LeftMotor1.set(0.3);
            	LeftMotor2.set(0.3);
            	RightMotor1.set(-0.3);                                            // go straight
            	RightMotor2.set(-0.3);
    		}
    		if (x>380) {
    			LeftMotor1.set(0.35);
            	LeftMotor2.set(0.35);
            	RightMotor1.set(-0.25);                                         //if it becomes unaligned, turn right
            	RightMotor2.set(-0.25);
    		}
    		else if (x<340 && x>0) {
    			LeftMotor1.set(0.25);
            	LeftMotor2.set(0.25);                                        //if it becomes unaligned, turn left
            	RightMotor1.set(-0.35);
            	RightMotor2.set(-0.35);
    		}
    		else {
    			Timer.delay(0.01);                                          //wait
    		}
    	}
    	
    	LeftMotor1.set(0);
    	LeftMotor2.set(0);                                       //stop motors
    	RightMotor1.set(0);
    	RightMotor2.set(0);
 
    	if (y > 450) {
    		y = datatable.getNumber("COG_Y", 0.0);
    		pitch.set(.75);
    		LEDRing.set(0);
    		Timer.delay(0.25);                                          //blink LED ring
    		LEDRing.set(1);
    		Timer.delay(0.25);
    		LEDRing.set(0);
    		Timer.delay(0.25);
    		LEDRing.set(1);
    		Timer.delay(0.25);
    		
    	}
        }
    		   	



	public void teleopInit() {

		camera.setBrightness(50);										//sets new camera settings for comfortable driving
		camera.setWhiteBalanceAuto();
		camera.setExposureAuto();
		camera.updateSettings();

	}

    
	public void teleopPeriodic() {
    	double Rightaxis = Left.getRawAxis(4);									//sets variables on the joysticks for controlling the robot
    	double Leftaxis = Left.getRawAxis(0);                 
    	double Righttrigger = (Left.getRawAxis(3) * -1);    
    	double Lefttrigger = Left.getRawAxis(2);
    	double Rightspeed = Left.getRawAxis(3);
    	double Leftspeed = Left.getRawAxis(3);
    	double RightspeedReverse = Left.getRawAxis(2);        
    	double LeftspeedReverse = Left.getRawAxis(2);
    	double SwillP;
    	boolean rbON = true;
    	boolean lbON = true;
    	boolean ybON = true;
    	boolean bbON = true;
    	boolean abON = true;
    	boolean xbON = true;
    	
    	if (abON = Left.getRawButton(1)) {													//turns LED on
    		LEDRing.set(1);
    	}
    	
    	if (xbON = Left.getRawButton(3)) {														//turns LED off
    		LEDRing.set(0);
    	}
    	
    	while (Left.getRawAxis(4) > 0.2 || Left.getRawAxis(4) < -0.2)  {					//zeroturn command
    		LeftMotor1.set((Left.getRawAxis(4)));
        	LeftMotor2.set((Left.getRawAxis(4)));
        	                                                    
    		RightMotor1.set((Left.getRawAxis(4)));
        	RightMotor2.set((Left.getRawAxis(4)));
    	}
    	
    	if (Leftaxis > 0.1) {																//forward turning
    		Leftaxis *= -1;                     
    		Rightspeed *= Leftaxis + 1;
    	}
    	
    	else if (Leftaxis < -0.1) {
    		Leftspeed *= Leftaxis + 1;          
    	}
    	
    	
    	
    	while (Lefttrigger > 0.1) {												//reverse command
    		Lefttrigger = Left.getRawAxis(2);
    		Leftaxis = Left.getRawAxis(0);
    		RightspeedReverse = Left.getRawAxis(2);        
        	LeftspeedReverse = Left.getRawAxis(2);
        	
    		if (Leftaxis > 0.1) {   											//reverse turning
    			Leftaxis *= -1; 
    			RightspeedReverse *= Leftaxis + 1;
    	}
    	
    		else if (Leftaxis < -0.1) {
    			LeftspeedReverse *= Leftaxis + 1;          
    	}
    		LeftMotor1.set(((LeftspeedReverse) * -1));							//travelling in reverse with respect to the left trigger
    		LeftMotor2.set(((LeftspeedReverse) * -1));
    		
    		RightMotor1.set((RightspeedReverse));
    		RightMotor2.set((RightspeedReverse));
    	}

    	ballMotor.set(Right.getRawAxis(1) * -1);
    	
    	LeftMotor1.set(Leftspeed);												//sets motors for forward driving
    	LeftMotor2.set(Leftspeed);                  
    	
	    RightMotor1.set(Rightspeed * -1);
    	RightMotor2.set(Rightspeed * -1);          
    	
    	if (rbON = Left.getRawButton(6)) {										//setting camera servo for left and right
    		yaw.set(0.3);
    	}
    	
    	if (lbON = Left.getRawButton(5)) {
    		yaw.set(0.5);
    	}
    	
    	if (ybON = Left.getRawButton(4)) {										//setting camera servo for up and down
    		pitch.set(0.3);
    	}
    	if (bbON = Left.getRawButton(2)) {
    		pitch.set(0.6);
    	}
    	
    }
   

	public void testPeriodic() {
	
    }
}	