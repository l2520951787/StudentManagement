package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students WHERE deleted = false")
  List<Student> searchStudent();

  @Select("SELECT * FROM students_courses WHERE id = #{id}")
  List<StudentsCourses> searchStudentsCourses();

  @Select("SELECT * FROM students WHERE id = #{id}")
  Student searchStudentById(String id);

  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentsCourses> searchStudentsCoursesByStudentId(String studentId);

  @Insert(
      "INSERT INTO students(name, ruby, nickname, mail_address, area, age, gender, remark, is_deleted)"
          + " VALUES(#{name}, #{ruby}, #{nickname}, #{mailAddress}, #{area}, #{age}, #{gender}, #{remark}, false)")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudent(Student student);

  @Insert("INSERT INTO students_courses(student_id, course, start_date, end_date)"
      + "VALUES(#{studentId}, #{course}, #{startDate}, #{endDate})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudentsCourses(StudentsCourses studentsCourses);

  @Update("UPDATE students SET "
      + "name = #{name},"
      + "ruby = #{ruby}, "
      + "nickname = #{nickname},"
      + "mail_address = #{mailAddress},"
      + "area = #{area},"
      + "age = #{age},"
      + "gender = #{gender},"
      + "remark = #{remark},"
      + "deleted = #{deleted} "
      + "WHERE id = #{id}")
  void updateStudent(Student student);

  @Update("UPDATE students_courses SET course = #{course} WHERE id = #{id}")
  void updateStudentsCourses(StudentsCourses studentsCourses);

  @Update("UPDATE students_courses SET deleted = true WHERE id = #{id}")
  void logicDeleteStudentsCourses(String id);
}
