package Model;

import java.util.ArrayList;

public class LendingByUser {
    public Student student;
    public ArrayList<Lending> lendings;
   

    public LendingByUser(Student student,ArrayList<Lending> lendings) {
        this.student = student;
        this.lendings = lendings;
    }
}
