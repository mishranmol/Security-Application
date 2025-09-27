package com.SecurityApp.SecurityApplication.Services;

import com.SecurityApp.SecurityApplication.Entities.Session;
import com.SecurityApp.SecurityApplication.Entities.User;
import com.SecurityApp.SecurityApplication.Repositories.SessionRepository;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class SessionService {



    private final SessionRepository sessionRepository;
    final int SESSION_LIMIT = 2 ;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }


    //creating a new Session
    public void generateNewSession(User user , String refreshToken){

 
        List<Session> userSessions = sessionRepository.findByUser(user);


        if(userSessions.size() == SESSION_LIMIT){


        userSessions.sort(Comparator.comparing(Session::getLastUsedAt));


        Session leastRecentlyUsedSession = userSessions.get(0);


        sessionRepository.delete(leastRecentlyUsedSession);

        }

        //Creating a new Session 
        Session newSession = new Session(user,refreshToken);
        sessionRepository.save(newSession);

    }

    //In this we will check that is there a session according to this refreshToken inside the DB or not .
    public void validateSession(String refreshToken){

        //if there is a session for this refreshToken means this is a valid session
        Session session = sessionRepository.findByRefreshToken(refreshToken).
                orElseThrow( () -> new SessionAuthenticationException("Session for this refreshToken not found"));

        //updating lastUsedAt time everytime we are using the session to refresh a new token
        session.setLastUsedAt(LocalDateTime.now());

        sessionRepository.save(session);

    }

//    public Session getSessionByRefreshToken(String refreshToken){
//        Session session = sessionRepository.findByRefreshToken(refreshToken)
//                .orElseThrow( () -> new SessionAuthenticationException("Session for this refreshToken not found"));
//
//        return session;
//    }
}
