package frc.team321.robot.utilities.enums;

public enum LinearSlidePosition {
    BOTTOM(0), CARRY(5000), SWITCH(20000), SCALE(50000);

    public int position;

    LinearSlidePosition(int position){
        this.position = position;
    }
}
