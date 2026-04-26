package raisetech.StudentManagement.data;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講コース情報")
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
