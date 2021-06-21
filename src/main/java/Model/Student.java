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
public class Student {

    public int idStud;
    public String fullname;
    public String tel;
    public String email;
    public String depart;
    public String sex;
    public String matricule;
    

    public Student(int idStud, String fullname,
            String tel,
            String email,
            String depart,String sex,String matricule) {

        this.idStud = idStud;
        this.tel = tel;
        this.fullname = fullname;
        this.email = email;
        this.depart = depart;
        this.sex = sex;
        this.matricule = matricule;

    }

}
