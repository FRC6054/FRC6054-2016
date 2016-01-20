
package org.usfirst.frc.team6054.robot;

//import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	Joystick Left = new Joystick(0);
	Joystick Right = new Joystick(1);
	
	Victor LeftMotor1 = new Victor(0);
	Victor LeftMotor2 = new Victor(1);
	Victor RightMotor1 = new Victor(2);
	Victor RightMotor2 = new Victor(3);


    public void robotInit() {

    }
	
	
    public void disabledInit(){

    }
	
	public void disabledPeriodic() {
	
	}

	
    public void autonomousInit() {
   
    }

    public void autonomousPeriodic() {
    }
    

    public void teleopInit() {

    }

    
    public void teleopPeriodic() {
    	LeftMotor1.set(Left.getY());
    	LeftMotor2.set(Left.getY());
    	
    	RightMotor1.set(Right.getY());
    	RightMotor2.set(Right.getY());
       
    }

    public void testPeriodic() {

    }
}
