package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

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
  List<Student> getStudentList();

  /**
   * IDから受講生の検索を行います。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  Student searchStudentById(String id);

  /**
   * 受講生のコース情報の全件検索を行います。
   *
   * @return 受講生のコース情報(全件)
   */
  List<StudentCourse> getStudentCourseList();

  /**
   * 受講生IDからその受講生の受講コース情報を検索します。
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づくコース情報
   */
  List<StudentCourse> searchStudentCourseByStudentId(String studentId);

  /**
   * 受講生を新規登録します。 IDは自動採番されます。
   *
   * @param student 受講生
   */
  void registerStudent(Student student);

  /**
   * 受講生コース情報を新規登録します。 IDは自動採番されます。
   *
   * @param studentCourse 受講生コース情報
   */
  void registerStudentCourse(StudentCourse studentCourse);

  /**
   * 受講生情報を更新します。
   *
   * @param student 受講生情報
   */
  void updateStudent(Student student);

  /**
   * コース情報のコース名を更新します。
   *
   * @param studentCourse コース情報のコース名
   */
  void updateStudentCourse(StudentCourse studentCourse);
}