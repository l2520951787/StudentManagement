package raisetech.StudentManagement.domain;

import jakarta.validation.Valid;
import java.util.List;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

public class CourseDetail {

  private Student student;
  private List<StudentCourse> studentCourses;
  @Valid
  private List<CourseStatus> courseStatusList;
}
