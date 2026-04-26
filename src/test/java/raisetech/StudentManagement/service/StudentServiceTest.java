package raisetech.StudentManagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
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
    Student student = new Student();
    student.setId("1");
    student.setName("赤坂浩二");
    List<StudentCourse> courses = List.of(new StudentCourse());
    when(repository.searchStudentById("1")).thenReturn(student);
    when(repository.searchStudentCourseByStudentId("1")).thenReturn(courses);

    // 実行
    StudentDetail actual = sut.searchStudentDetail("1");

    // 検証
    assertNotNull(actual);
    assertEquals(student, actual.getStudent());
    assertEquals(courses, actual.getStudentCourseList());

    verify(repository).searchStudentById("1");
    verify(repository).searchStudentCourseByStudentId("1");
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
  void 受講生とコースが正常に登録されること() {
    // 事前準備
    Student student = new Student();
    student.setName("テスト");

    StudentCourse course = new StudentCourse();

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourseList(List.of(course));

    doAnswer(invocation -> {
      Student s = invocation.getArgument(0);
      s.setId("1L");
      return null;
    }).when(repository).registerStudent(any());

    LocalDateTime before = LocalDateTime.now();

    // 実行
    sut.registerStudent(studentDetail);
    LocalDateTime after = LocalDateTime.now();

    // 検証
    assertEquals("1L", course.getStudentId());
    assertTrue(!course.getStartDate().isBefore(before) && !course.getStartDate().isAfter(after));
    assertEquals(course.getStartDate().plusYears(1), course.getEndDate());

    verify(repository, times(1)).registerStudent(any());
    verify(repository, times(1)).registerStudentCourse(any());
  }

  @Test
  void 受講生詳細が更新されること() {
    // 事前準備
    Student student = new Student();
    student.setId("1L");
    student.setName("テスト");

    StudentCourse course1 = new StudentCourse();
    course1.setId("100L");
    course1.setStudentId("1L");

    StudentCourse course2 = new StudentCourse();
    course2.setId("101L");
    course2.setStudentId("1L");

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourseList(List.of(course1, course2));

    // 実行
    sut.updateStudent(studentDetail);

    // 検証
    verify(repository).updateStudent(student);
    verify(repository).updateStudentCourse(course1);
    verify(repository).updateStudentCourse(course2);
    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(2)).updateStudentCourse(any());
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