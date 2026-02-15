package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> searchStudent();

  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> searchStudentsCourses();

  @Insert("INSERT INTO students (id, name, ruby, nickname, mail_address, area, gender, age) values (#{id}, #{name}, #{ruby}, #{nickname}, #{mailAddress}, #{area}, #{gender}, #{age})")
  void registerStudent(Student student);

  @Insert("INSERT INTO studentsCourses (id, student_id, course, start_date, end_date) values (#{id}, #{studentId}, #{course}, #{startDate}, #{endDate}")
  void registerStudentsCourse(StudentsCourses courses);
}
