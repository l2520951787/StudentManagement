package raisetech.StudentManagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.dto.StudentSearchCondition;
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
    StudentSearchCondition condition = new StudentSearchCondition();
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    List<CourseStatus> courseStatusList = new ArrayList<>();
    when(repository.getStudentList(any(StudentSearchCondition.class))).thenReturn(studentList);
    when(repository.getStudentCourseList()).thenReturn(studentCourseList);
    when(repository.getCourseStatusList()).thenReturn(courseStatusList);

    // 実行
    List<StudentDetail> actual = sut.getStudentList(condition);

    // 検証
    verify(repository, times(1)).getStudentList(any(StudentSearchCondition.class));
    verify(repository, times(1)).getStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList,
        courseStatusList);
  }

  @Test
  void 検索条件がリポジトリへ渡されること() {

    // 事前準備
    StudentSearchCondition condition = new StudentSearchCondition();

    condition.setName("赤坂浩二");
    condition.setCourse("Javaコース");

    when(repository.getStudentList(any(StudentSearchCondition.class))).thenReturn(List.of());

    when(repository.getStudentCourseList()).thenReturn(List.of());

    when(repository.getCourseStatusList()).thenReturn(List.of());

    when(converter.convertStudentDetails(any(), any(), any())).thenReturn(List.of());

    // 実行
    sut.getStudentList(condition);

    ArgumentCaptor<StudentSearchCondition> captor =
        ArgumentCaptor.forClass(StudentSearchCondition.class);

    verify(repository).getStudentList(captor.capture());

    StudentSearchCondition actual = captor.getValue();

    // 検証
    assertThat(actual.getName())
        .isEqualTo("赤坂浩二");

    assertThat(actual.getCourse())
        .isEqualTo("Javaコース");
  }

  @Test
  void 受講生詳細検索_リポジトリの処理が適切に呼び出されていること() {
    // 事前準備
    int id = 99;
    Student student = new Student();
    student.setId(id);
    List<StudentCourse> courses = List.of(new StudentCourse());
    List<CourseStatus> statuses = List.of(new CourseStatus());
    when(repository.searchStudentById(id)).thenReturn(student);
    when(repository.searchStudentCourseByStudentId(id)).thenReturn(courses);
    when(repository.searchCourseStatus(id)).thenReturn(statuses);

    StudentDetail expected = new StudentDetail(student, courses, statuses);

    // 実行
    StudentDetail actual = sut.searchStudentDetail(id);

    // 検証
    verify(repository, times(1)).searchStudentById(id);
    verify(repository, times(1)).searchStudentCourseByStudentId(id);
    verify(repository, times(1)).searchCourseStatus(id);

    assertThat(actual.getStudent().getId()).isEqualTo(expected.getStudent().getId());
    assertThat(actual.getStudentCourseList()).isEqualTo(expected.getStudentCourseList());
    assertThat(actual.getCourseStatusList()).isEqualTo(expected.getCourseStatusList());
  }

  @Test
  void 受講生詳細の登録_リポジトリの処理が適切に呼び出されていること() {
    // 事前準備
    Student student = new Student();
    StudentCourse studentCourse = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    List<CourseStatus> courseStatusList = new ArrayList<>();
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList, courseStatusList);

    // 実行
    sut.registerStudent(studentDetail);

    // 検証
    verify(repository, times(1)).registerStudent(student);
    verify(repository, times(1)).registerStudentCourse(studentCourse);
    verify(repository, times(1)).registerCourseStatus(any(CourseStatus.class));
  }

  @Test
  void 受講生詳細の登録_初期化処理が行われること() {
    int id = 99;
    Student student = new Student();
    student.setId(id);
    StudentCourse studentCourse = new StudentCourse();

    sut.initStudentsCourse(studentCourse, student.getId());

    assertThat(studentCourse.getStudentId()).isEqualTo(id);
    assertThat(studentCourse.getStartDate()).isEqualTo(LocalDate.now());
    assertThat(studentCourse.getEndDate().getYear()).isEqualTo(
        LocalDate.now().plusYears(1).getYear());
  }

  @Test
  void 受講生詳細の更新_リポジトリの処理が適切に呼び出されていること() {
    // 事前準備
    int id = 99;
    String name = "テスト";
    int courseId = 99;
    String status = "本申込";

    Student student = new Student();
    student.setId(id);
    student.setName(name);

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId(courseId);
    List<StudentCourse> studentCourseList = new ArrayList<>();
    studentCourseList.add(studentCourse);

    CourseStatus courseStatus = new CourseStatus();
    courseStatus.setCourseId(courseId);
    courseStatus.setStatus(status);
    List<CourseStatus> courseStatusList = new ArrayList<>();
    courseStatusList.add(courseStatus);

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourseList(studentCourseList);
    studentDetail.setCourseStatusList(courseStatusList);

    // 実行
    sut.updateStudent(studentDetail);

    // 検証
    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(1)).updateStudentCourse(studentCourse);
    verify(repository, times(1)).updateCourseStatus(courseId, status);
  }

  @Test
  void 条件指定で受講生を検索して詳細が返ってくること() {
    Student student = new Student();
    student.setId(99);
    List<Student> studentList = List.of(student);

    when(repository.searchByCondition(null, null, null)).thenReturn(studentList);
    when(repository.getStudentCourseList()).thenReturn(new ArrayList<>());
    when(repository.searchCourseStatus(99)).thenReturn(new ArrayList<>());
    when(converter.convertStudentDetails(anyList(), anyList(), anyList())).thenReturn(
        new ArrayList<>());

    sut.searchStudentsByCondition(null, null, null);

    verify(repository, times(1)).searchByCondition(null, null, null);
    verify(repository, times(1)).getStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, new ArrayList<>(),
        new ArrayList<>());
  }

  @Test
  void 条件指定の受講生検索で入力チェックをすること() {
    String[] names = {null, "", " "};
    String[] emails = {null, "", "invalid_email"};
    Integer[] ages = {null, -1, 999};

    for (String name : names) {
      for (String mailAddress : emails) {
        for (Integer age : ages) {
          reset(repository);
          when(repository.searchByCondition(name, mailAddress, age)).thenReturn(new ArrayList<>());
          when(repository.getStudentCourseList()).thenReturn(new ArrayList<>());

          sut.searchStudentsByCondition(name, mailAddress, age);

          verify(repository, times(1)).searchByCondition(name, mailAddress, age);
          verify(repository, times(1)).getStudentCourseList();
        }
      }
    }
  }

  @Test
  void 申込状況が更新できること() {
    sut.updateCourseStatus(99, "受講中");
    verify(repository, times(1)).updateCourseStatus(99, "受講中");
  }

  @Test
  void すべての受講生の申込状況を確認できること() {
    List<CourseStatus> courseStatuses = new ArrayList<>();
    CourseStatus Test1 = new CourseStatus();
    Test1.setId(1);
    Test1.setStatus("受講中");
    CourseStatus Test2 = new CourseStatus();
    Test2.setId(2);
    Test2.setStatus("仮申込");
    courseStatuses.add(Test1);
    courseStatuses.add(Test2);

    when(repository.getCourseStatusList()).thenReturn(courseStatuses);

    List<CourseStatus> result = sut.getAllCourseStatus();

    assertThat(result.size()).isEqualTo(2);
    assertThat(result.get(0).getStatus()).isEqualTo("受講中");
    assertThat(result.get(1).getStatus()).isEqualTo("仮申込");

    verify(repository, times(1)).getCourseStatusList();
  }
}

