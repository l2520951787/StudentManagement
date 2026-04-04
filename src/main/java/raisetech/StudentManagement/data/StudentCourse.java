package raisetech.StudentManagement.data;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"id", "studentId", "course", "startDate", "endDate", "deleted"})
public class StudentCourse {

  private String id;
  private String studentId;
  private String course;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private boolean deleted;
}
