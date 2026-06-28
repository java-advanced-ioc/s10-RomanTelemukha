package com.softserve.itacademy.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = {"owner", "title"})
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ToDo {

    private String title;
    private LocalDateTime createdAt;
    private User owner;
    private List<Task> tasks;

}
