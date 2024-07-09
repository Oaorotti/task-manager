package com.example.meister_test.service;

import com.example.meister_test.model.Task;
import com.example.meister_test.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task taskDetails) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RestClientException("Task not found"));
        if (task.getStatus() == Task.Status.PENDING) {
            task.setStatus(taskDetails.getStatus());
            return taskRepository.save(task);
        } else {
            throw new IllegalArgumentException("Only pending tasks can be updated");
        }
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RestClientException("Task not found"));

        if (task.getCreationDate().isBefore(LocalDate.now().minusDays(5))) {
            taskRepository.delete(task);
        } else {
            throw new IllegalArgumentException("Only pending tasks created more than 5 days ago can be deleted");
        }
    }

}
