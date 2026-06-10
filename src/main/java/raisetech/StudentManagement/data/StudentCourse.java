package raisetech.StudentManagement.data;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講コース情報")
@Getter
@Setter
@JsonPropertyOrder({"id", "studentId", "course", "startDate", "endDate"})
public class StudentCourse {

  private int id;
  private int studentId;
  private String course;
  private LocalDate startDate;
  private LocalDate endDate;


}
