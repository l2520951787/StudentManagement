package raisetech.StudentManagement.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.exception.StudentNotFoundException;
import raisetech.StudentManagement.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービスです。 受講生の検索や登録、更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生詳細の一覧検索です。 全権検索を行うので、条件指定は行いません。
   *
   * @return 受講生詳細一覧(全件)
   */
  public List<StudentDetail> getStudentList() {
    List<Student> studentList = repository.getStudentList();
    
    List<StudentCourse> studentCourseList = repository.getStudentCourseList();
    return converter.convertStudentDetails(studentList, studentCourseList);
  }

  /**
   * 受講生詳細検索です。 IDに紐づく受講生情報を取得した後、コース情報も取得して設定します。
   *
   * @param id 受講生ID
   * @return 受講生詳細
   */
  public StudentDetail searchStudentDetail(String id) {
    Student student = repository.searchStudentById(id);

    if (student == null) {
      throw new StudentNotFoundException("このIDの受講生は存在しません。");
    }

    List<StudentCourse> studentCourse = repository.searchStudentCourseByStudentId(
        student.getId());
    return new StudentDetail(student, studentCourse);
  }

  /**
   * 受講生詳細の登録を行います。 受講生とコースの情報を個別に登録し、コース情報には受講生情報を紐づける値・日付情報(開始・終了日)を設定します。
   *
   * @param studentDetail 受講生詳細
   * @return 登録情報を付与した受講生詳細
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();

    repository.registerStudent(student);
    studentDetail.getStudentCourseList().forEach(studentCourses -> {
      initStudentsCourses(studentCourses, student);
      repository.registerStudentCourse(studentCourses);
    });
    return studentDetail;
  }

  /**
   * コース情報を登録する際の初期情報を設定する。
   *
   * @param studentCourses コース情報
   * @param student        受講生
   */
  private void initStudentsCourses(StudentCourse studentCourses, Student student) {
    LocalDateTime now = LocalDateTime.now();

    studentCourses.setStudentId(student.getId());
    studentCourses.setStartDate(now);
    studentCourses.setEndDate(now.plusMonths(6));
  }

  /**
   * 受講生詳細の更新を行います。 受講生とそのコース情報をそれぞれ更新します。
   *
   * @param studentDetail 受講生詳細
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    studentDetail.getStudentCourseList()
        .forEach(studentCourses -> repository.updateStudentCourse(studentCourses));
  }
}