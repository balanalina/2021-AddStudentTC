package org.example;

import domain.Nota;
import domain.Student;
import domain.Tema;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import repository.NotaXMLRepository;
import repository.StudentXMLRepository;
import repository.TemaXMLRepository;
import service.Service;
import validation.*;

import static org.junit.Assert.assertEquals;

public class WhiteBoxTest {
    private Service service;
    private StudentXMLRepository studentXMLRepository;
    private TemaXMLRepository temaXMLRepository;
    private String invalidIdString;
    private String validId;
    private String description;
    private Integer deadlineGood;
    private Integer startLineGood;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void beforeTest() {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Tema> temaValidator = new TemaValidator();
        Validator<Nota> notaValidator = new NotaValidator();

        studentXMLRepository = new StudentXMLRepository(studentValidator, "studenti.xml");
        temaXMLRepository = new TemaXMLRepository(temaValidator, "teme.xml");
        NotaXMLRepository notaXMLRepository = new NotaXMLRepository(notaValidator, "note.xml");

        service = new Service(studentXMLRepository, temaXMLRepository, notaXMLRepository);
        invalidIdString = "";
        validId = "id";
        description = "dunnow";
        deadlineGood = 7;
        startLineGood = 5;
    }

    @Test
    public void Test1() {
        // valid add
        if (temaXMLRepository.findOne(validId) != null) {
            temaXMLRepository.delete(validId);
        }
        assertEquals(1, service.saveTema(validId, description, deadlineGood, startLineGood));
    }

    @Test
    public void Test2() throws ValidationException {
        // null id
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("ID invalid!");
        assertEquals(1, temaXMLRepository.save(new Tema(null, description, deadlineGood, startLineGood)));
    }

    @Test
    public void Test3() {
        // empty id
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("ID invalid!");
        assertEquals(1, temaXMLRepository.save(new Tema(invalidIdString, description, deadlineGood, startLineGood)));
    }

    @Test
    public void Test4() {
        // valid deadline
        assertEquals(0, service.saveTema(validId, description, 6, 2));
    }

    @Test
    public void Test5() {
        // deadline invalid
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Deadline invalid!");
        assertEquals(0, service.saveTema("1", "Test", 1, 7));
    }

    @Test
    public void Test6() {
        // null description
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Descriere invalida!");
        assertEquals(0, service.saveTema(validId, null, deadlineGood, deadlineGood));
    }

    @Test
    public void Test7() {
        // empty description
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Descriere invalida!");
        assertEquals(0, service.saveTema(validId, "", deadlineGood, deadlineGood));
    }

    @Test
    public void Test8() {
        // empty description
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Descriere invalida!");
        assertEquals(0, service.saveTema(validId, "", deadlineGood, deadlineGood));
    }

    @Test
    public void Test9() {
        // deadline invalid
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Deadline invalid!");
        assertEquals(0, service.saveTema("1", "Test", 15, 7));
    }

    @Test
    public void Test10() {
        // deadline invalid
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Data de primire invalida!");
        assertEquals(0, service.saveTema("1", "Test", 1, 0));
    }
}
