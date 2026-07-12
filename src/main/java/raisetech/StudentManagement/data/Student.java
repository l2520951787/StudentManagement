package raisetech.StudentManagement.data;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生")
@Getter
@Setter
@JsonPropertyOrder({"id", "name", "ruby", "nickname", "mailAddress", "area", "age", "gender",
    "remark", "deleted"})
public class Student {

  private Integer id;

  @NotBlank
  private String name;

  @NotBlank
  private String ruby;

  @NotBlank
  private String nickname;

  @NotBlank
  @Email
  private String mailAddress;

  @NotBlank
  private String area;

  private Integer age;

  @NotBlank
  private String gender;

  private String remark;
  private boolean deleted;
}
