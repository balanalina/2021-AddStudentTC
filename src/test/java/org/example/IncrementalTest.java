package org.example;

import domain.Nota;
import domain.Student;
import domain.Pair;
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

import static junit.framework.TestCase.assertEquals;

public class IncrementalTest {
    private Service service;
    private StudentXMLRepository studentXMLRepository;
    private TemaXMLRepository temaXMLRepository;
    private NotaXMLRepository notaXMLRepository;
    private String validStudentId;
    private String validStudentName;
    private Integer validStudentGroup;

    private String validAssignmentId;
    private String validAssignmentDescription;
    private Integer validAssignmentDeadline;
    private Integer getValidAssignmentStartLine;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void beforeTest() {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Tema> temaValidator = new TemaValidator();
        Validator<Nota> notaValidator = new NotaValidator();

        studentXMLRepository = new StudentXMLRepository(studentValidator, "studenti.xml");
        temaXMLRepository = new TemaXMLRepository(temaValidator, "teme.xml");
        notaXMLRepository = new NotaXMLRepository(notaValidator, "note.xml");
        service = new Service(studentXMLRepository, temaXMLRepository, notaXMLRepository);

        validStudentId = "valid id";
        validStudentName = "valid name";
        validStudentGroup = 931;

        validAssignmentId = "valid assignment";
        validAssignmentDescription = "valid description";
        validAssignmentDeadline = 10;
        getValidAssignmentStartLine = 5;
    }

    // Test Add Student features
    @Test
    public void addStudent(){

        if(studentXMLRepository.findOne(validStudentId)!=null)
            studentXMLRepository.delete(validStudentId);

        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Grupa invalida!");
        assertEquals(1, service.saveStudent(validStudentId, validStudentName, -100));

    }

    @Test
    public void addAssignment(){

        if(studentXMLRepository.findOne(validStudentId)!=null)
            studentXMLRepository.delete(validStudentId);


        assertEquals(1, service.saveStudent(validStudentId, validStudentName, validStudentGroup));

        if(temaXMLRepository.findOne(validAssignmentId)!=null)
            temaXMLRepository.delete(validAssignmentId);

        assertEquals(1, service.saveTema(validAssignmentId, validAssignmentDescription, 11, 10));

    }

    @Test
    public void addGrade(){
        if(studentXMLRepository.findOne(validStudentId)!=null)
            studentXMLRepository.delete(validStudentId);

        assertEquals(1, service.saveStudent(validStudentId, validStudentName, validStudentGroup));

        if(temaXMLRepository.findOne(validAssignmentId)!=null)
            temaXMLRepository.delete(validAssignmentId);

        assertEquals(1, service.saveTema(validAssignmentId, validAssignmentDescription,
                validAssignmentDeadline, getValidAssignmentStartLine));
        if(notaXMLRepository.findOne(new Pair<>(validStudentId, validAssignmentId))!=null)
            notaXMLRepository.delete(new Pair<>(validStudentId, validAssignmentId));


        assertEquals(1, service.saveNota(validStudentId, validAssignmentId, 11, getValidAssignmentStartLine, "dunnow"));

        // same grade
        assertEquals(0, service.saveNota(validStudentId, validAssignmentId, 10, getValidAssignmentStartLine, "dunnow"));

    }
}
