package in.co.greenwave.jobapi.globalexceptionhandler;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLSyntaxErrorException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle SQL Constraint Violations (e.g., PK, FK, UNIQUE constraints)
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<String> handleIntegrityConstraintViolation(SQLIntegrityConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Database constraint violated.");
    }

    // Handle SQL Syntax Errors without exposing details
    @ExceptionHandler(SQLSyntaxErrorException.class)
    public ResponseEntity<String> handleSQLSyntaxError(SQLSyntaxErrorException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid database query.");
    }

    // Handle Cases Where No Object is Found (e.g., SELECT returning no results)
    @ExceptionHandler(SQLDataException.class)
    public ResponseEntity<String> handleSQLDataError(SQLDataException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Requested data not found.");
    }

    // Handle Database Connection Issues
    @ExceptionHandler(SQLNonTransientConnectionException.class)
    public ResponseEntity<String> handleDatabaseConnectionError(SQLNonTransientConnectionException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database service is currently unavailable.");
    }

    // General SQL Exception Handler (Catches any SQL-related errors)
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> handleGeneralSQLException(SQLException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("A database error occurred.");
    }
}
