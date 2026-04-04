package raisetech.StudentManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("不正なリクエストです：" + e.getMessage());
  }

  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity<String> handleNullPointerException(NullPointerException e) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("サーバー内部でエラーが発生しました");
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleException(Exception e) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("予期しないエラーが発生しました：" + e.getMessage());
  }

  @ExceptionHandler(StudentNotFoundException.class)
  public ResponseEntity<String> handleStudentNotFoundException(StudentNotFoundException e) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(e.getMessage());
  }

  @ExceptionHandler(TestException.class)
  public ResponseEntity<String> handleTestException(TestException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }
}
