package com.wecode.bookit.dao;

import com.wecode.bookit.domain.Meetings;

import java.util.List;

public interface MeetingDAO {

    void addMeeting(Meetings meeting);
    void removeMeeting(String id);
    List<Meetings> viewAllMeetings();
}
