package raisetech.StudentManagement.data;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生")
@Getter
@Setter
@JsonPropertyOrder({"id", "name", "ruby", "nickname", "mailAddress", "area", "age", "gender",
    "remark", "deleted"})
public class Student {

  @NotBlank
  @Pattern(regexp = "^\\d+$")
  private String id;

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

  private int age;

  @NotBlank
  private String gender;

  private String remark;
  private boolean deleted;
}
