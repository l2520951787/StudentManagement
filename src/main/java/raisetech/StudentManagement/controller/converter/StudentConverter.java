package raisetech.StudentManagement.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.studentCourse;
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
      List<studentCourse> studentCourseList) {
    List<StudentDetail> studentDetails = new ArrayList<>();
    studentList.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<studentCourse> convertStudentCourseList = studentCourseList.stream()
          .filter(studentCourse -> student.getId().equals(studentCourse.getStudentId()))
          .collect(Collectors.toList());

      studentDetail.setStudentCourseList(convertStudentCourseList);
      studentDetails.add(studentDetail);
    });
    return studentDetails;
  }

}
