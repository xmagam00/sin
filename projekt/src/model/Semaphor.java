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
public enum Semaphor {
    RED("RED"),
    GREEN("GREEN");

    private String colorOfSemaphor;



    private Semaphor(String color) {
        this.colorOfSemaphor = color;
    }

    public String getColorSemaphore(){
        return this.colorOfSemaphor;
    }

    @Override
    public String toString() {
        return colorOfSemaphor;
    }
    
    
}
