package com.softserve.itacademy.service.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.service.UserService;

@Service
public class ToDoServiceImpl implements ToDoService {

    private final UserService userService;
    private final List<ToDo> toDoList = new ArrayList<>();

    @Autowired
    public ToDoServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ToDo addTodo(User user, ToDo todo) {
        if(user == null || todo == null) {return null;}

        todo.setOwner(user);

        boolean duplicate = toDoList.contains(todo);
        boolean userExists = userService.getAll().contains(user);
        if (duplicate || !userExists) {
            return  null;
        }

        toDoList.add(todo);
        return todo;
    }

    @Override
    public ToDo updateTodo(ToDo todo) {
        if(todo == null) {return null;}

        int updateIndex = toDoList.indexOf(todo);

        if(updateIndex == -1) {
            return null;
        }

        toDoList.set(updateIndex, todo);
        return todo;
    }

    @Override
    public void deleteTodo(ToDo todo) {
        if(todo == null) return;

        toDoList.remove(todo);
    }

    @Override
    public List<ToDo> getAll() {
        return new ArrayList<>(toDoList);
    }

    @Override
    public List<ToDo> getByUser(User user) {
        if(user == null) return Collections.emptyList();

        List<ToDo> userToDos = toDoList.stream().
                filter(t -> t.getOwner().equals(user))
                .toList();

        return new ArrayList<>(userToDos);
    }

    @Override
    public ToDo getByUserTitle(User user, String title) {
        if (user == null || title == null) return null;
        String processedTitle = title.trim();

        for(ToDo toDo: toDoList) {
            if(toDo.getOwner().equals(user) && toDo.getTitle().equals(processedTitle)) {
                return toDo;
            }
        }

        return null;
    }

}
