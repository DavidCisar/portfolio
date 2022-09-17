package io.dcisar.backend.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@AllArgsConstructor
public class HttpResponse {

    private int httpStatusCode;
    private HttpStatus httpStatus;
    private String reason;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-mm-yyyy hh:mm:ss", timezone = "Europe/Paris")
    private Date timeStamp;
}
