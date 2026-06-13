package raisetech.StudentManagement.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

/**
 * 受講生とコース情報・受講生詳細を相互に変換するコンバーター。
 */
@Component
public class StudentConverter {

  /**
   * 受講生に紐づくコース情報をマッピングする。 コース情報は受講生に対して複数存在するのでループを回して詳細情報を組み立てる。
   *
   * @param studentList       受講生一覧
   * @param studentCourseList 受講生のコース情報のリスト
   * @return 受講生詳細情報のリスト
   */
  public List<StudentDetail> convertStudentDetails(List<Student> studentList,
      List<StudentCourse> studentCourseList, List<CourseStatus> courseStatusList) {
    List<StudentDetail> studentDetails = new ArrayList<>();
    studentList.stream()
        .forEach(student -> {
          StudentDetail studentDetail = new StudentDetail();
          studentDetail.setStudent(student);

          List<StudentCourse> convertStudentCourseList = studentCourseList.stream()
              .filter(course -> Objects.equals(course.getStudentId(), student.getId()))
              .toList();
          studentDetail.setStudentCourseList(convertStudentCourseList);

          Set<Integer> courseId = convertStudentCourseList.stream()
              .map(StudentCourse::getId)
              .collect(Collectors.toSet());

          List<CourseStatus> relatedStatus = courseStatusList.stream()
              .filter(status -> courseId.contains(status.getCourseId()))
              .toList();
          studentDetail.setCourseStatusList(relatedStatus);

          studentDetails.add(studentDetail);
        });
    return studentDetails;
  }

}
