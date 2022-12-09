package com.example.studentrating.controllers;

import com.example.studentrating.lib.Session;
import com.example.studentrating.models.*;
import com.example.studentrating.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Random;

@RestController
@RequestMapping(path = "/activitiesRest")
public class ActivitiesRestController {

    private static final Logger log = LoggerFactory.getLogger(ActivitiesRestController.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private DisputeRepository disputeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @PostMapping("/addNew")
    public String addNew(String title, int cost, String login, HttpSession session) {
        try {
            if (Session.isAuthorize(session).equals("TEACHER")) {
                Activity activity = new Activity();

                activity.setTitle(title);
                activity.setCost(cost);
                activity.setAuthor(teacherRepository.findById(Session.getSessionId(session)).get());
                activity.setStudent(studentRepository.findByLogin(login));
                activity.setDisputed(false);

                activityRepository.save(activity);

                Notification notification = new Notification();

                notification.setStudent(studentRepository.findByLogin(login));
                notification.setTitle(title);
                notification.setCost(cost);

                notificationRepository.save(notification);

                Student student = studentRepository.findByLogin(login);
                student.setPoints(student.getPoints() + cost);

                studentRepository.save(student);

                log.info("Activity with id:{} was created", activity.getId());

                return "success";
            } else {
                return "У вас нет прав на данное действие";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping("/deleteById")
    public String deleteById(Long activityId, HttpSession session) {
        try {
            if (Session.isAuthorize(session).equals("TEACHER")) {

                Activity activity = activityRepository.findById(activityId).get();

                Student student = activity.getStudent();

                student.setPoints(student.getPoints() - activity.getCost());

                Notification notification = new Notification();

                notification.setStudent(student);
                notification.setTitle(activity.getTitle() + " - УДАЛЕНА");
                notification.setCost(activity.getCost() * -1);

                notificationRepository.save(notification);
                studentRepository.save(student);
                activityRepository.deleteById(activityId);

                log.info("Activity with id:{} was deleted", activityId);

                return "success";
            } else {
                return "У вас нет прав на данное действие";
            }
        } catch (
                Exception e) {
            return e.getMessage();
        }
    }

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

                    Dispute dispute = new Dispute();

                    dispute.setSum(0);
                    dispute.setActivity(activity);

                    Teacher teacher = teacherRepository.findRandom();
                    dispute.setTeacher(teacher);

                    Student student = studentRepository.findRandomCouncil(activity.getStudent().getId());
                    dispute.setStudent(student);

                    disputeRepository.save(dispute);

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
