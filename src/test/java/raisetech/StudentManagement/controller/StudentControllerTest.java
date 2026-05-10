package raisetech.StudentManagement.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private StudentService service;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void 受講生詳細の一覧検索が実行できて空のリストが返ってくること() throws Exception {
    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).getStudentList();
  }

  @Test
  void 受講生詳細の受講生情報で適切な値を入力したときに入力チェックに異常が発生しないこと() {
    Student student = new Student();
    student.setId("99");
    student.setName("テスト太郎");
    student.setRuby("テストタロウ");
    student.setNickname("テスト");
    student.setMailAddress("test@example.com");
    student.setArea("東京");
    student.setGender("男");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生詳細の受講生情報でIDに数字以外を入力したときに入力チェックに掛かること() {
    Student student = new Student();
    student.setId("testID");
    student.setName("テスト太郎");
    student.setRuby("テストタロウ");
    student.setNickname("テスト");
    student.setMailAddress("test@example.com");
    student.setArea("東京");
    student.setGender("男");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message")
        .containsOnly("数字のみ入力してください。");
  }

  @Test
  void 受講生IDから受講生詳細の検索ができること() throws Exception {
    String id = "999";
    mockMvc.perform(get("/student/{id}", id))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudentDetail(id);
  }

  @Test
  void 受講生詳細の登録が実行できて空で返ってくること() throws Exception {
    mockMvc.perform(post("/registerStudent").contentType(MediaType.APPLICATION_JSON).content(
            """
                {
                    "student": {
                        "name": "赤坂浩二",
                        "ruby": "アカサカコウジ",
                        "nickname": "コウジ",
                        "mailAddress": "koujiakasaka@gmail.com",
                        "area": "兵庫",
                        "age": 35,
                        "gender": "男",
                        "remark": ""
                    },
                    "studentCourseList": [
                        {
                            "course": "Javaコース"
                        }
                    ]
                }
                """
        ))
        .andExpect(status().isOk());

    verify(service, times(1)).registerStudent(any());
  }

  @Test
  void 受講生詳細の更新ができること() throws Exception {
    mockMvc.perform(put("/updateStudent").contentType(MediaType.APPLICATION_JSON).content(
            """
                {
                    "student": {
                        "id": "1",
                        "name": "赤坂浩二",
                        "ruby": "アカサカコウジ",
                        "nickname": "コウジ",
                        "mailAddress": "koujiakasaka@gmail.com",
                        "area": "兵庫",
                        "age": 35,
                        "gender": "男",
                        "remark": ""
                    },
                    "studentCourseList": [
                        {
                            "id": "1",
                            "studentId": "1",
                            "course": "Javaコース",
                            "startDate": "2025-10-27T00:00:00",
                            "endDate": "2026-04-27T00:00:00"
                        }
                    ]
                }
                """
        ))
        .andExpect(status().isOk());

    verify(service, times(1)).registerStudent(any());
  }
}