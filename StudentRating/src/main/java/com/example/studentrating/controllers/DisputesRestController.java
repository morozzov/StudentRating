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

@RestController
@RequestMapping(path = "/disputesRest")
public class DisputesRestController {

    private static final Logger log = LoggerFactory.getLogger(DisputesRestController.class);

    @Autowired
    private DisputeRepository disputeRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private RespondRepository respondRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/confirm")
    public String confirm(Long disputeId, HttpSession session) {
        try {
            Dispute dispute = disputeRepository.findById(disputeId).get();

            if (Session.isAuthorize(session).equals("TEACHER")) {
                if (dispute.getTeacher().getId().equals(Session.getSessionId(session))) {
                    dispute.setSum(dispute.getSum() + 1);
                    dispute.setTeacher(null);

                    disputeRepository.save(dispute);
                } else return "ERROR";
            } else {
                if (dispute.getStudent().getId().equals(Session.getSessionId(session))) {
                    dispute.setSum(dispute.getSum() + 1);
                    dispute.setStudent(null);

                    disputeRepository.save(dispute);
                } else return "ERROR";
            }

            if (dispute.getTeacher() == null && dispute.getStudent() == null) {
                Notification notification = new Notification();

                if (dispute.getSum() == 2) {
                    if (dispute.getActivity() == null) {
                        notification.setCost(dispute.getRespond().getStatus().equals("CANCELED") ? dispute.getRespond().getTask().getCost() : dispute.getRespond().getTask().getCost() * -1);
                        notification.setTitle(dispute.getRespond().getTask().getTitle() + " - СПОР ОДОБРЕН");
                        notification.setStudent(dispute.getRespond().getExecutor());

                        Student student = dispute.getRespond().getExecutor();

                        student.setPoints(dispute.getRespond().getStatus().equals("CANCELED") ? student.getPoints() + dispute.getRespond().getTask().getCost() : student.getPoints() - dispute.getRespond().getTask().getCost());

                        studentRepository.save(student);

                        Respond respond = dispute.getRespond();

                        dispute.setRespond(null);

                        disputeRepository.save(dispute);

                        respondRepository.delete(respond);
                    } else {
                        notification.setCost(dispute.getActivity().getCost() * -1);
                        notification.setTitle(dispute.getActivity().getTitle() + " - СПОР ОДОБРЕН");
                        notification.setStudent(dispute.getActivity().getStudent());

                        Student student = dispute.getActivity().getStudent();

                        student.setPoints(student.getPoints() - dispute.getActivity().getCost());

                        studentRepository.save(student);

                        Activity activity = dispute.getActivity();

                        dispute.setActivity(null);

                        disputeRepository.save(dispute);
                        activityRepository.delete(activity);
                    }
                    notificationRepository.save(notification);
                    disputeRepository.deleteById(disputeId);
                } else {
                    notification.setCost(0);
                    notification.setTitle(dispute.getActivity() == null ? dispute.getRespond().getTask().getTitle() + " - СПОР ОТКЛОНЕН" : dispute.getActivity().getTitle() + " - СПОР ОТКЛОНЕН");
                    notification.setStudent(dispute.getActivity() == null ? dispute.getRespond().getExecutor() : dispute.getActivity().getStudent());

                    if (dispute.getActivity() == null) {
                        Respond respond = dispute.getRespond();

                        respond.setDisputed(false);

                        respondRepository.save(respond);
                    } else {
                        Activity activity = dispute.getActivity();

                        activity.setDisputed(false);

                        activityRepository.save(activity);
                    }

                    notificationRepository.save(notification);
                    disputeRepository.deleteById(disputeId);
                    log.info("Dispute with id:{} was deleted", disputeId);
                }
            }

            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping("/disprove")
    public String disprove(Long notDisputeId, HttpSession session) {
        try {
            Dispute dispute = disputeRepository.findById(notDisputeId).get();

            if (Session.isAuthorize(session).equals("TEACHER")) {
                if (dispute.getTeacher().getId().equals(Session.getSessionId(session))) {
                    dispute.setTeacher(null);

                    disputeRepository.save(dispute);
                } else return "ERROR";
            } else {
                if (dispute.getStudent().getId().equals(Session.getSessionId(session))) {
                    dispute.setStudent(null);

                    disputeRepository.save(dispute);
                } else return "ERROR";
            }


            if (dispute.getTeacher() == null && dispute.getStudent() == null) {
                Notification notification = new Notification();


                notification.setCost(0);
                notification.setTitle(dispute.getActivity() == null ? dispute.getRespond().getTask().getTitle() + " - СПОР ОТКЛОНЕН" : dispute.getActivity().getTitle() + " - СПОР ОТКЛОНЕН");
                notification.setStudent(dispute.getActivity() == null ? dispute.getRespond().getExecutor() : dispute.getActivity().getStudent());

                if (dispute.getActivity() == null) {
                    Respond respond = dispute.getRespond();

                    respond.setDisputed(false);

                    respondRepository.save(respond);
                } else {
                    Activity activity = dispute.getActivity();

                    activity.setDisputed(false);

                    activityRepository.save(activity);
                }

                notificationRepository.save(notification);
                disputeRepository.deleteById(notDisputeId);

                log.info("Dispute with id:{} was deleted", notDisputeId);
            }

            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
