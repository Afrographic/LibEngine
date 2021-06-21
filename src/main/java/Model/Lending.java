/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author X M G
 */
public class Lending {

    public int idBorrow;   
    public String lendDate ;
    public LibItem libItem;
    public Student student;
    public int  duration;
    public String returnDay;
    public long delayDay;


    public Lending(int idBorow,String lendDate,LibItem libItem, Student student,int  duration,String returnDay,long delayDay) {
        this.idBorrow=idBorow;
        this.lendDate = lendDate;
        this.libItem = libItem;
        this.student = student;
        this.duration = duration;
        this.returnDay = returnDay;
        this.delayDay = delayDay;
    }
}
