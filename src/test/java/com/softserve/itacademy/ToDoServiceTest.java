package com.softserve.itacademy;

import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.service.UserService;
import com.softserve.itacademy.service.impl.ToDoServiceImpl;
import com.softserve.itacademy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ToDoServiceTest {

    private AnnotationConfigApplicationContext context;
    private ToDoService toDoService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        context = new AnnotationConfigApplicationContext();
        context.register(UserServiceImpl.class);
        context.register(ToDoServiceImpl.class);
        context.refresh();
        userService = context.getBean(UserService.class);
        toDoService = context.getBean(ToDoService.class);
    }

    @AfterEach
    void tearDown() {
        if (context != null) {
            context.close();
        }
    }

    @Test
    void addTodo_shouldAttachToOwner_andBeReturnedByGetByUser() {
        User user1 = new User("Michael", "Jackson", "babu@gmail.com", "well_1234", new ArrayList<>());
        ToDo toDo = new ToDo("list", null, null, null);

        userService.addUser(user1);
        toDoService.addTodo(user1, toDo);
        var toDoList = toDoService.getByUser(user1);

        assertEquals(user1, toDo.getOwner());
        assertTrue(toDoList.contains(toDo));
    }

    @Test

    void addTodo_shouldReturnNull_whenInvalid() {
        User user1 = new User("Michael", "Jackson", "babu@gmail.com", "well_1234", new ArrayList<>());
        ToDo toDo = new ToDo("list", null, null, null);

        assertNull(toDoService.addTodo(user1, toDo));
        userService.addUser(user1);
        assertNull(toDoService.addTodo(null, new ToDo()));
        assertNull(toDoService.addTodo(new User(), null));
        assertNull(toDoService.addTodo(null, null));
    }

    @Test

    void addTodo_shouldEnforceUniqueTitle_perOwner() {
        User user1 = new User(null, null, "babu@gmail.com", null, new ArrayList<>());
        ToDo toDo = new ToDo("list", null, user1, null);
        userService.addUser(user1);

        assertEquals(toDo, toDoService.addTodo(user1, toDo));
        assertNull(toDoService.addTodo(user1, toDo));
    }

    @Test

    void updateTodo_shouldReplaceExistingByTitleWithinOwner() {
        User user1 = new User(null, null, "babu@gmail.com", null, new ArrayList<>());
        ToDo toDo1 = new ToDo("list", null, user1, null);
        ToDo toDo2 = new ToDo("list", LocalDateTime.now(), user1, null);

        userService.addUser(user1);
        toDoService.addTodo(user1, toDo1);
        toDoService.updateTodo(toDo2);
        var updatedToDo = toDoService.getByUserTitle(user1, "list");

        assertEquals(toDo2.getCreatedAt(), updatedToDo.getCreatedAt());
    }

    @Test
    void updateTodo_shouldReturnNull_whenNotFound_orInvalid() {
        User user1 = new User(null, null, "babu@gmail.com", null, new ArrayList<>());
        ToDo toDo = new ToDo("list", null, user1, null);

        assertNull(toDoService.updateTodo(toDo));
        assertNull(toDoService.updateTodo(null));
    }

    @Test
    void deleteTodo_shouldRemoveFromOwnerList() {
        User user1 = new User(null, null, "babu@gmail.com", null, new ArrayList<>());
        ToDo toDo = new ToDo("list", null, user1, null);

        userService.addUser(user1);
        toDoService.addTodo(user1, toDo);
        toDoService.deleteTodo(toDo);

        assertFalse(toDoService.getAll().contains(toDo));
    }

    @Test
    void getAll_shouldReturnCopy() {
        List<ToDo> toDoList1 = toDoService.getAll();
        List<ToDo> toDoList2 = toDoService.getAll();

        assertNotSame(toDoList1, toDoList2);
    }

    @Test
    void getByUser_shouldReturnCopy_orEmptyWhenNone() {
        User user1 = new User(null, null, "babu@gmail.com", null, new ArrayList<>());
        ToDo toDo = new ToDo("list", null, user1, null);
        List<ToDo> toDoList = toDoService.getByUser(user1);

        assertEquals(0, toDoList.size());

        userService.addUser(user1);
        toDoService.addTodo(user1, toDo);
        assertTrue(toDoService.getByUser(user1).contains(toDo));
        assertNotSame(toDoService.getByUser(user1), toDoService.getByUser(user1));
    }

    @Test
    void getByUserTitle_shouldReturnToDo_orNull() {
        User user1 = new User(null, null, "babu@gmail.com", null, new ArrayList<>());
        ToDo toDo = new ToDo("list", null, user1, null);

        userService.addUser(user1);
        toDoService.addTodo(user1, toDo);

        assertEquals(toDo, toDoService.getByUserTitle(user1, "list"));
        assertNull(toDoService.getByUserTitle(null, "list"));
        assertNull(toDoService.getByUserTitle(user1, null));
        assertNull(toDoService.getByUserTitle(null, null));
        assertNull(toDoService.getByUserTitle(user1, "wrong name"));
    }
}
