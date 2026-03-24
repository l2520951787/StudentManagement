package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.studentCourse;

/**
 * 受講生テーブル・コース情報テーブルと紐づくRepositoryです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います。
   *
   * @return 受講生一覧(全件)
   */
  @Select("SELECT * FROM students")
  List<Student> getStudentList();

  /**
   * IDから受講生の検索を行います。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  @Select("SELECT * FROM students WHERE id = #{id}")
  Student searchStudentById(String id);

  /**
   * 受講生のコース情報の全件検索を行います。
   *
   * @return 受講生のコース情報(全件)
   */
  @Select("SELECT * FROM students_courses")
  List<studentCourse> getStudentCourseList();

  /**
   * 受講生IDからその受講生の受講コース情報を検索します。
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づくコース情報
   */
  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<studentCourse> searchStudentCourseByStudentId(String studentId);

  /**
   * 受講生を新規登録します。 IDは自動採番されます。
   *
   * @param student 受講生
   */
  @Insert(
      "INSERT INTO students(name, ruby, nickname, mail_address, area, age, gender, remark, deleted)"
          + " VALUES(#{name}, #{ruby}, #{nickname}, #{mailAddress}, #{area}, #{age}, #{gender}, #{remark}, false)")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudent(Student student);

  /**
   * 受講生コース情報を新規登録します。 IDは自動採番されます。
   *
   * @param studentCourse 受講生コース情報
   */
  @Insert("INSERT INTO students_courses(student_id, course, start_date, end_date)"
      + "VALUES(#{studentId}, #{course}, #{startDate}, #{endDate})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudentCourse(studentCourse studentCourse);

  /**
   * 受講生情報を更新します。
   *
   * @param student 受講生情報
   */
  @Update("UPDATE students SET "
      + "name = #{name}, "
      + "ruby = #{ruby}, "
      + "nickname = #{nickname}, "
      + "mail_address = #{mailAddress}, "
      + "area = #{area}, "
      + "age = #{age}, "
      + "gender = #{gender}, "
      + "remark = #{remark}, "
      + "deleted = #{deleted} "
      + "WHERE id = #{id}")
  void updateStudent(Student student);

  /**
   * コース情報のコース名を更新します。
   *
   * @param studentCourse コース情報のコース名
   */
  @Update("UPDATE students_courses SET course = #{course} WHERE id = #{id}")
  void updateStudentCourse(studentCourse studentCourse);
}