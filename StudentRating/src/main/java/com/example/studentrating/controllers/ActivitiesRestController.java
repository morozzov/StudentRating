package com.example.studentrating.controllers;

import com.example.studentrating.lib.Session;
import com.example.studentrating.models.Activity;
import com.example.studentrating.models.Notification;
import com.example.studentrating.models.Respond;
import com.example.studentrating.repositories.ActivityRepository;
import com.example.studentrating.repositories.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path = "/activitiesRest")
public class ActivitiesRestController {

    private static final Logger log = LoggerFactory.getLogger(ActivitiesRestController.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @PostMapping("/disputeActivity")
    public String disputeActivity(Long activityId, HttpSession session) {
        try {
            Activity activity = activityRepository.findById(activityId).get();
            if (activity.getStudent().getId().equals(Session.getSessionId(session))) {
                if (!activity.isDisputed()) {
                    activity.setDisputed(true);
                    activityRepository.save(activity);
                    Notification notification = new Notification();
                    notification.setCost(0);
                    notification.setTitle(activity.getTitle() + " - ОСПАРИВАЕТСЯ");
                    notification.setStudent(activity.getStudent());
                    notificationRepository.save(notification);

                    //TODO: realize logic for dispute to mentors sending

                    log.info("Activity with id:{} was disputed", activity.getId());
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
}
