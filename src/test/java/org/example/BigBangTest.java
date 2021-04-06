package org.example;

import domain.Nota;
import domain.Pair;
import domain.Student;
import domain.Tema;
import junit.framework.TestCase;
import org.junit.Test;
import repository.NotaXMLRepository;
import repository.StudentXMLRepository;
import repository.TemaXMLRepository;
import service.Service;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.Validator;


public class BigBangTest extends TestCase {
    Validator<Student> studentValidator = new StudentValidator();
    Validator<Tema> temaValidator = new TemaValidator();
    Validator<Nota> notaValidator = new NotaValidator();
    StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, "studenti.xml");
    TemaXMLRepository fileRepository2 = new TemaXMLRepository(temaValidator, "teme.xml");
    NotaXMLRepository fileRepository3 = new NotaXMLRepository(notaValidator, "note.xml");
    Service service = new Service(fileRepository1, fileRepository2, fileRepository3);

    @Test
    public void testAll()
    {
        Student s1 = new Student("8", "Maria", 111);
        //add non unique student
        addStudent(s1);
        Tema a1 = new Tema("5", "Salut", 12, 2);
        //add non unique assignment
        addAssignment(a1);
        //add grade
        Nota g1 = new Nota(new Pair(s1.getID(),a1.getID()), 8, 10, "ok");
        addNota(g1);
    }

    public void addStudent(Student s1){
        assertEquals(0, service.saveStudent(s1.getID(), s1.getNume(), s1.getGrupa()));
    }

    public void addAssignment(Tema a1){
        assertEquals(0, service.saveTema(a1.getID(), a1.getDescriere(), a1.getDeadline(), a1.getStartline()));
    }

    public void addNota(Nota g1){
        assertEquals(1, service.saveNota(g1.getID().getObject1(), g1.getID().getObject2(),g1.getNota(), g1.getSaptamanaPredare(), g1.getFeedback()));

    }
}



