package raisetech.StudentManagement.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.dto.StudentSearchCondition;
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
  public List<StudentDetail> getStudentList(StudentSearchCondition condition) {
    List<Student> studentList = repository.getStudentList(condition);
    List<StudentCourse> studentCourseList = repository.getStudentCourseList();
    List<CourseStatus> courseStatusList = repository.getCourseStatusList();

    return converter.convertStudentDetails(studentList, studentCourseList, courseStatusList);
  }

  /**
   * 受講生詳細検索です。 IDに紐づく受講生情報を取得した後、コース情報も取得して設定します。
   *
   * @param id 受講生ID
   * @return 受講生詳細
   */
  public StudentDetail searchStudentDetail(int id) {
    Student student = repository.searchStudentById(id);

    if (student == null) {
      throw new StudentNotFoundException("このIDの受講生は存在しません。");
    }

    List<StudentCourse> studentCourse = repository.searchStudentCourseByStudentId(
        student.getId());

    List<CourseStatus> courseStatus = repository.searchCourseStatus(id);
    return new StudentDetail(student, studentCourse, courseStatus);
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
      initStudentsCourse(studentCourses, student.getId());
      repository.registerStudentCourse(studentCourses);

      CourseStatus courseStatus = new CourseStatus();
      courseStatus.setCourseId(studentCourses.getId());
      courseStatus.setStatus("仮申込");
      repository.registerCourseStatus(courseStatus);
    });
    return studentDetail;
  }

  /**
   * コース情報を登録する際の初期情報を設定する。
   *
   * @param studentCourses コース情報
   * @param id             受講生ID
   */
  void initStudentsCourse(StudentCourse studentCourses, int id) {
    LocalDate now = LocalDate.now();

    studentCourses.setStudentId(id);
    studentCourses.setStartDate(now);
    studentCourses.setEndDate(now.plusYears(1));
  }

  public List<StudentDetail> searchStudentsByCondition(String name, String mailAddress,
      Integer age) {
    List<Student> students = repository.searchByCondition(name, mailAddress, age);
    List<StudentCourse> courses = repository.getStudentCourseList();
    List<CourseStatus> courseStatuses = students.stream()
        .flatMap(student -> repository.searchCourseStatus(student.getId()).stream())
        .toList();
    return converter.convertStudentDetails(students, courses, courseStatuses);
  }

  /**
   * 受講生詳細の更新を行います。 受講生とそのコース情報をそれぞれ更新します。
   *
   * @param studentDetail 受講生詳細
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    studentDetail.getStudentCourseList().forEach(studentCourse -> {
      studentCourse.setStudentId(studentDetail.getStudent().getId());
      repository.updateStudentCourse(studentCourse);

      studentDetail.getCourseStatusList().stream()
          .filter(status -> status.getCourseId() == studentCourse.getId())
          .findFirst()
          .ifPresent(
              status -> repository.updateCourseStatus(status.getCourseId(), status.getStatus()));
    });
  }

  public void updateCourseStatus(int courseId, String status) {
    repository.updateCourseStatus(courseId, status);
  }

  public List<CourseStatus> getAllCourseStatus() {
    return repository.getCourseStatusList();
  }
}