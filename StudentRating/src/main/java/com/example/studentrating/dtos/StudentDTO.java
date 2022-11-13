package com.example.studentrating.dtos;

import com.example.studentrating.models.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentDTO {

    private String name;
    private String surname;
    private String patronymic;
    private String groupName;

    private int points;

    public StudentDTO(Student entity) {
        this.name = entity.getName();
        this.surname = entity.getSurname();
        this.patronymic = entity.getPatronymic();
        this.groupName = entity.getGroupName();
        this.points = entity.getPoints();
    }
}
