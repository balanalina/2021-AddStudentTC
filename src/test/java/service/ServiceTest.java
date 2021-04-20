package service;

import domain.Nota;
import domain.Pair;
import domain.Student;
import domain.Tema;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import repository.NotaXMLRepository;
import repository.StudentXMLRepository;
import repository.TemaXMLRepository;
import validation.*;

import static org.junit.Assert.assertEquals;
import org.junit.rules.ExpectedException;

public class ServiceTest {
    Validator<Student> studentValidator = new StudentValidator();
    Validator<Tema> temaValidator = new TemaValidator();
    Validator<Nota> notaValidator = new NotaValidator();
    StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, "studenti.xml");
    TemaXMLRepository fileRepository2 = new TemaXMLRepository(temaValidator, "teme.xml");
    NotaXMLRepository fileRepository3 = new NotaXMLRepository(notaValidator, "note.xml");
    Service service = new Service(fileRepository1, fileRepository2, fileRepository3);

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void beforeTest() {
        Validator<Student> studentValidator;
        Validator<Tema> temaValidator;
        Validator<Nota> notaValidator;
        StudentXMLRepository fileRepository1;
        TemaXMLRepository fileRepository2;
        NotaXMLRepository fileRepository3;
        Service service;
    }
    @Test
    public void testIdUnique() {

        assertEquals(0, service.saveStudent("8", "Maria", 150));
        service.deleteStudent("8"); // we must delete every successful student added
        // works only with new id
    }

    @Test
    public void testIdNonEmpty(){
        //null to the id
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("ID invalid!");
        assertEquals(1, fileRepository1.save(new Student(null, "Maria", 150)));

        //compile error int instead of String
        //assertEquals(0, service.saveStudent(1, "Maria", 150));

        //empty id case
        assertEquals(1, service.saveStudent("", "Maria", 150));
    }

    @Test
    public void testNameNonEmpty(){
        //null name
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Nume invalid!");
        assertEquals(1, service.saveStudent("8", null, 150));

        //empty name
        assertEquals(1, service.saveStudent("8", "", 150));

        //integer instead of String
        //assertEquals(1, service.saveStudent("8", 1, 150));
    }

    @Test
    public void testGroup(){
        //out of boundaries
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Grupa invalida!");
        assertEquals(1, service.saveStudent("8", "Maria", 109));
        assertEquals(1, service.saveStudent("8", "Maria", 110));

        //in
        assertEquals(1, service.saveStudent("8", "Maria", 111));
        service.deleteStudent("8");
        assertEquals(1, service.saveStudent("8", "Maria", 937));
        service.deleteStudent("8");

        //out
        assertEquals(1, service.saveStudent("8", "Maria", 938));
        assertEquals(1, service.saveStudent("8", "Maria", 939));
    }

    @Test
    public void testTemaDeadLine(){

        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Deadline invalid!");
        assertEquals(1, service.saveTema("5", "Salut", 0, 2));
    }

    @Test
    public void testTemaStartLine(){

        assertEquals(1, service.saveTema("6", "File", 8, 3));
        service.deleteTema("6");
    }
}