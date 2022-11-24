package com.dws.web.Review;

import com.dws.web.Event.Event;
import com.dws.web.Event.EventRepository;
import lombok.NoArgsConstructor;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@NoArgsConstructor
public class ReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    private EntityManager entityManager;


    public void addReviewToThisEvent(Event e, Review r){
        r.assignEvent(e);
        e.addReviewToThisEvent(r);

        //XSS//
        PolicyFactory policy= Sanitizers.FORMATTING.and(Sanitizers.LINKS);
        r.setMessage(policy.sanitize(r.getMessage()));

        this.reviewRepository.saveAndFlush(r);
    }

    public Review deleteReviewFromAnEvent(Event e, Review r){
        e.deleteReviewOfThisEvent(r);
        r.unassignEvent();
        r.unassignCustomer();
        reviewRepository.delete(r);
        return r;
    }

    public void addUpdatedReviewToThisEvent(long idReview, Review r){
        //XSS//
        if (reviewRepository.findById(idReview).isPresent()) {
            Review aux=reviewRepository.findById(idReview).get();
            PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
            aux.setMessage(policy.sanitize(r.getMessage()));
            reviewRepository.save(aux);
        }
    }

    public Collection<Review> getAllReviewsOfAnEvent(Event e){
        return this.reviewRepository.findByEvent(e);
    }

    public Review getReview(Event e, long idReview){
        Optional<Review> r=this.reviewRepository.findById(idReview);
        if(r.isPresent()){
            return r.get();
        }
        else{
            return null;
        }
    }

    public List<Review> getAllReviews(){
        return this.reviewRepository.findAll();
    }


    public Collection<Review> filterReview(Event event, String userName) {

        TypedQuery<Review> query = entityManager.createQuery("SELECT r FROM Review r WHERE r.userName = :userName AND r.event = :event", Review.class);
        return query.setParameter("userName", userName).setParameter("event", event).getResultList();
    }

}