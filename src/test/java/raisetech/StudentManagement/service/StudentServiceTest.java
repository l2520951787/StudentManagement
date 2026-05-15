package raisetech.StudentManagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  @InjectMocks
  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバータの処理が適切に呼び出されていること() {
    // 事前準備
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    when(repository.getStudentList()).thenReturn(studentList);
    when(repository.getStudentCourseList()).thenReturn(studentCourseList);

    // 実行
    List<StudentDetail> actual = sut.getStudentList();

    // 検証
    verify(repository, times(1)).getStudentList();
    verify(repository, times(1)).getStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);
  }

  @Test
  void 受講生詳細検索_リポジトリの処理が適切に呼び出されていること() {
    // 事前準備
    String id = "99";
    Student student = new Student();
    student.setId(id);
    List<StudentCourse> courses = List.of(new StudentCourse());
    when(repository.searchStudentById(id)).thenReturn(student);
    when(repository.searchStudentCourseByStudentId(id)).thenReturn(courses);

    // 実行
    StudentDetail actual = sut.searchStudentDetail(id);

    // 検証
    verify(repository).searchStudentById(id);
    verify(repository).searchStudentCourseByStudentId(id);
  }

  @Test
  void 受講生詳細の登録_リポジトリの処理が適切に呼び出されていること() {
    // 事前準備
    Student student = new Student();
    StudentCourse studentCourse = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    // 実行
    sut.registerStudent(studentDetail);

    // 検証
    verify(repository, times(1)).registerStudent(student);
    verify(repository, times(1)).registerStudentCourse(studentCourse);
  }

  @Test
  void 受講生詳細の登録_初期化処理が行われること() {
    String id = "99";
    Student student = new Student();
    student.setId(id);
    StudentCourse studentCourse = new StudentCourse();

    sut.initStudentsCourse(studentCourse, student.getId());

    assertThat(studentCourse.getStudentId()).isEqualTo(id);
    assertThat(studentCourse.getStartDate().getHour()).isEqualTo(LocalDateTime.now().getHour());
    assertThat(studentCourse.getEndDate().getYear()).isEqualTo(
        LocalDateTime.now().plusYears(1).getYear());
  }

  @Test
  void 受講生詳細の更新_リポジトリの処理が適切に呼び出されていること() {
    // 事前準備
    Student student = new Student();
    StudentCourse studentCourse = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    // 実行
    sut.updateStudent(studentDetail);

    // 検証
    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(1)).updateStudentCourse(studentCourse);
  }
}

