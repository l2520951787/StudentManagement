package raisetech.StudentManagement.dto;

import lombok.Data;

@Data
public class StudentSearchCondition {

  private String name;
  private String area;
  private String mailAddress;
  private String course;
  private boolean deleted;
}
