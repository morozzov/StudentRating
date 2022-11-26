package com.example.studentrating.controllers;

import com.example.studentrating.models.Activity;
import com.example.studentrating.models.Notification;
import com.example.studentrating.models.Respond;
import com.example.studentrating.repositories.ActivityRepository;
import com.example.studentrating.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path = "/activitiesRest")
public class ActivitiesRestController {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @PostMapping("/disputeActivity")
    public String disputeActivity(Long activityId, HttpSession session) {
        try {
            Activity activity = activityRepository.findById(activityId).get();
            if (activity.getStudent().getId().equals(getSessionId(session))) {
                if (!activity.isDisputed()) {
                    activity.setDisputed(true);
                    activityRepository.save(activity);
                    Notification notification = new Notification();
                    notification.setCost(0);
                    notification.setTitle(activity.getTitle() + " - ОСПАРИВАЕТСЯ");
                    notification.setStudent(activity.getStudent());
                    notificationRepository.save(notification);

                    //TODO: realize logic for dispute to mentors sending

                    return "success";
                } else {
                    return "Спор уже создан";
                }
            } else {
                return "Вы не откликались на данное поручение";
            }
        } catch (Exception e) {
            return "e.getMessage()";
        }
    }

    public Long getSessionId(HttpSession request) {
        return (Long) request.getAttribute("id");
    }
}
