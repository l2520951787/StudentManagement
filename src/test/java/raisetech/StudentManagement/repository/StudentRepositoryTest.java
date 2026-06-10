package raisetech.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.dto.StudentSearchCondition;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行えること() {
    StudentSearchCondition condition = new StudentSearchCondition();
    List<Student> actual = sut.getStudentList(condition);
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 受講生の検索が行えること() {
    Student actual = sut.searchStudentById(1);
    assertThat(actual.getName()).isEqualTo("赤坂浩二");
  }

  @Test
  void 受講コースの全件検索が行えること() {
    List<StudentCourse> actual = sut.getStudentCourseList();
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 受講生IDに紐づくコース情報の検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentCourseByStudentId(1);
    assertThat(actual).hasSize(1);
    assertThat(actual).extracting(StudentCourse::getCourse).contains("Javaコース");
  }

  @Test
  void 受講生の登録が行えること() {
    Student student = new Student();
    StudentSearchCondition condition = new StudentSearchCondition();
    student.setName("赤坂浩二");
    student.setRuby("アカサカコウジ");
    student.setNickname("コウジ");
    student.setMailAddress("koujiakasaka@gmail.com");
    student.setArea("兵庫");
    student.setAge(35);
    student.setGender("男");
    student.setRemark("");
    student.setDeleted(false);

    sut.registerStudent(student);

    List<Student> actual = sut.getStudentList(condition);
    assertThat(actual.size()).isEqualTo(6);
  }

  @Test
  void 受講コースの登録が行えること() {
    StudentCourse course = new StudentCourse();
    course.setStudentId(1);
    course.setCourse("Javaコース");
    course.setStartDate(LocalDate.now());
    course.setEndDate(LocalDate.now().plusYears(1));

    sut.registerStudentCourse(course);

    List<StudentCourse> actual = sut.getStudentCourseList();
    assertThat(actual.size()).isEqualTo(6);
  }

  @Test
  void 受講生の更新が行えること() {
    Student student = sut.searchStudentById(1);
    student.setAge(36);

    sut.updateStudent(student);

    Student actual = sut.searchStudentById(1);
    assertThat(actual.getAge()).isEqualTo(36);
  }

  @Test
  void 受講コースの更新が行えること() {
    List<StudentCourse> course = sut.searchStudentCourseByStudentId(1);

    StudentCourse updateCourse = course.getFirst();
    updateCourse.setCourse("AWSコース");

    sut.updateStudentCourse(updateCourse);

    List<StudentCourse> actual = sut.searchStudentCourseByStudentId(1);
    assertThat(actual).extracting(StudentCourse::getCourse).contains("AWSコース");
  }
}