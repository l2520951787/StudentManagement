package raisetech.StudentManagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
import raisetech.StudentManagement.exception.StudentNotFoundException;
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
  void 受講生詳細の単一検索_正常系_単一の受講生詳細が取得できること() {
    // 事前準備
    String id = "999";
    Student student = new Student();
    student.setId(id);
    when(repository.searchStudentById(id)).thenReturn(student);
    when(repository.searchStudentCourseByStudentId(id)).thenReturn(new ArrayList<>());

    StudentDetail expected = new StudentDetail(student, new ArrayList<>());

    // 実行
    StudentDetail actual = sut.searchStudentDetail(id);

    // 検証
    verify(repository, times(1)).searchStudentById(id);
    verify(repository, times(1)).searchStudentCourseByStudentId(id);
    assertEquals(expected.getStudent().getId(), actual.getStudent().getId());
  }

  @Test
  void 正常系_コース情報が0件でも返ること() {
    // 事前準備
    Student student = new Student();
    student.setId("1");
    student.setName("赤坂浩二");
    when(repository.searchStudentById("1")).thenReturn(student);
    when(repository.searchStudentCourseByStudentId("1")).thenReturn(List.of());

    // 実行
    StudentDetail result = sut.searchStudentDetail("1");

    // 検証
    assertNotNull(result);
    assertTrue(result.getStudentCourseList().isEmpty());
  }

  @Test
  void 異常系_受講生が存在しない場合に例外を投げること() {
    // 事前準備
    when(repository.searchStudentById("999")).thenReturn(null);

    // 実行
    assertThrows(StudentNotFoundException.class, () -> {
      sut.searchStudentDetail("999");
    });

    // 検証
    verify(repository, never()).searchStudentCourseByStudentId(any());
  }

  @Test
  void 受講生詳細が正常に登録されること() {
    // 事前準備
    Student student = new Student();
    StudentCourse course = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(course);
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    // 実行
    sut.registerStudent(studentDetail);

    // 検証
    verify(repository, times(1)).registerStudent(student);
    verify(repository, times(1)).registerStudentCourse(course);
  }

  @Test
  void 受講生詳細の登録_初期化処理が行われること() {
    // 事前準備
    String id = "999";
    Student student = new Student();
    student.setId(id);
    StudentCourse studentCourse = new StudentCourse();

    // 実行
    sut.initStudentsCourses(studentCourse, student.getId());

    // 検証
    assertEquals(id, studentCourse.getStudentId());
    assertEquals(LocalDateTime.now().getHour(), studentCourse.getStartDate().getHour());
    assertEquals(LocalDateTime.now().plusYears(1).getYear(), studentCourse.getEndDate().getYear());
  }

  @Test
  void 受講生詳細が更新されること() {
    // 事前準備
    Student student = new Student();
    StudentCourse course = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(course);
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    // 実行
    sut.updateStudent(studentDetail);

    // 検証;
    verify(repository, times(1)).registerStudent(student);
    verify(repository, times(1)).registerStudentCourse(course);
  }

  @Test
  void コースが空でもエラーにならないこと() {
    // 事前準備
    Student student = new Student();
    student.setId("1L");

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourseList(List.of());

    // 実行
    sut.updateStudent(studentDetail);

    // 検証
    verify(repository).updateStudent(student);
    verify(repository, never()).updateStudentCourse(any());
  }
}