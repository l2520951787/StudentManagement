package raisetech.StudentManagement.controller.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

class StudentConverterTest {

  private StudentConverter sut;

  @BeforeEach
  void before() {
    sut = new StudentConverter();
  }

  @Test
  void 受講生リストと受講生コース情報のリストを渡して受講生詳細のリストが作成できること() {
    Student student = createStudent();

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId(1);
    studentCourse.setStudentId(1);
    studentCourse.setCourse("Javaコース");
    studentCourse.setStartDate(LocalDate.now());
    studentCourse.setEndDate(LocalDate.now().plusYears(1));
    CourseStatus courseStatus = new CourseStatus();

    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of(studentCourse);

    List<CourseStatus> courseStatusesList = List.of(courseStatus);
    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList,
        courseStatusesList);

    assertThat(actual.get(0).getStudent()).isEqualTo(student);
    assertThat(actual.get(0).getStudentCourseList()).isEqualTo(studentCourseList);

  }

  @Test
  void 受講生リストと受講生リストのリストを渡した時紐づかない受講生コース情報は除外されること() {
    Student student = createStudent();

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId(1);
    studentCourse.setStudentId(2);
    studentCourse.setCourse("Javaコース");
    studentCourse.setStartDate(LocalDate.now());
    studentCourse.setEndDate(LocalDate.now().plusYears(1));

    CourseStatus courseStatus = new CourseStatus();
    courseStatus.setStatus("仮申込");

    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    List<CourseStatus> courseStatuses = List.of(courseStatus);
    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList,
        courseStatuses);
    assertThat(actual.get(0).getStudent()).isEqualTo(student);
    assertThat(actual.get(0).getStudentCourseList()).isEmpty();
  }


  private static Student createStudent() {
    Student student = new Student();
    student.setId(1);
    student.setName("テスト太郎");
    student.setRuby("テストタロウ");
    student.setNickname("テスト");
    student.setAge(23);
    student.setMailAddress("123@gmail.com");
    student.setGender("男");
    student.setArea("東京");
    student.setRemark("");
    student.setDeleted(false);
    return student;
  }
}