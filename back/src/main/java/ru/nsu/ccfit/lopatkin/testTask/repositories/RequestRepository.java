package ru.nsu.ccfit.lopatkin.testTask.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.ccfit.lopatkin.testTask.models.Request;

import java.time.LocalDateTime;
import java.util.List;


public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByDateTimeGreaterThanAndUserAgentAndIpAddress(LocalDateTime dateTime, String userAgent, String ipAddress);

}
