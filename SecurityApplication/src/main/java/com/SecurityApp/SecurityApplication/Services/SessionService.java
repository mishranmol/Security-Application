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

        //firstly get all the sessions mapped to this particular user , we will not use refreshToken to find the list of sessions
        //because the refreshToken for every user logged In will be different . example -> I have shared my login credentials of netflix
        // with akshat and max 2 people can use netflix at a time so when I will login my refreshToken will be different and when akshat
        // will login using my credentials his refreshToken will be different but in DB it will show 2 active session mapped to my Id.
        //InShort -> User1 and User2 using the same credentials to login = two separate sessions → two distinct refresh tokens.
        //findByUser(user) this method will be created inside the session repository
        List<Session> userSessions = sessionRepository.findByUser(user);

        //checking the size of userSessions list, we have taken 2 because we want only 2 sessions active per user at a time .
        // == 2 means we have reached the max.limit already so we have to remove the least recently loggedIn session
        if(userSessions.size() == SESSION_LIMIT){

        //for finding the least recently used session sort the sessions list
        //sorting the session list using getLastUsedAt .
            // This line -> userSessions.sort(Comparator.comparing(Session::getLastUsedAt));
            // sorts the userSessions list in-place, ordering Session objects by their lastUsedAt timestamp in ascending order.
            //Comparator.comparing(Session::getLastUsedAt) creates a comparator that extracts the lastUsedAt field (which must be Comparable) from each session.
            //.sort(...) applies this comparator to reorder the list accordingly.

            //we can also write this way = userSessions.sort(Comparator.comparing( x -> x.getLastUsedAt() ) ;
            userSessions.sort(Comparator.comparing(Session::getLastUsedAt));


            //fetching the least recently used Session
            Session leastRecentlyUsedSession = userSessions.get(0);
            //.get(0) -> my code
            //we could have also used userSessions.getFirst(); -> anuj bhaiya code

            //deleting the least recently used session from the DB
            sessionRepository.delete(leastRecentlyUsedSession);

        }



        //creating a new Session , this is my method of creating a session
        Session newSession = new Session(user,refreshToken);
        sessionRepository.save(newSession);

        //this is anujbhaiya way of creating a session , and also we have to add @builder annotation above sessionEntity to build session
       // Session newSession = Session.
         //       builder()
           //     .User(user)
             //   .refreshToken(refreshToken)
               // .build;

    }

    //inside this we will check that is there is a session according this refreshToken inside our DB or not .
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
