package frc.team321.robot.utilities.enums;

public enum LinearSlidePosition {
    BOTTOM(0), CARRY(10000), SWITCH(30000), SCALE(50000);

    public int position;

    LinearSlidePosition(int position){
        this.position = position;
    }
}
