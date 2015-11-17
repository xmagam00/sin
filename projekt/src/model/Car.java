/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Vojtech
 */
public class Car {
    
    private Direction direction;
    private int id;
    private String name;
    
    public Car(int id , Direction d){
        this.id = id;
        this.direction = d;
        String name = "auto" + id + this.getNameDirection(d);
        this.name = name;
    }
    
    public int getID(){
        return this.id;
    }
    
    public void setID(int id){
        this.id = id;
    }
    
    public Direction getDirection(){
        return this.direction;
    }
    
    public void setDirection(Direction d){
        this.direction = d;
    }
    
    public String getNameDirection(Direction d){
        String result = null;
        switch (d){
            case RIGHT:     result = "Right";
                            break;
            case STRAIGHT:  result = "Straight";
                            break;
            default:        result = null;
                            break;
                
        }
        return result;
    }
    
}
