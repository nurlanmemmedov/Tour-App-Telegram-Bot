package com.example.telegrambotapi.dtos;

import com.example.telegrambotapi.models.Session;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto implements Serializable {
    private String uuid;
    private Map<String, String> answers;

    public RequestDto(Session session){
        this.uuid = session.getUuid();
        this.answers = session.getData();
    }
}
