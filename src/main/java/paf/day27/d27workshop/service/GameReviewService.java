package paf.day27.d27workshop.service;

import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import paf.day27.d27workshop.model.Review;
import paf.day27.d27workshop.repo.GameRepo;
import paf.day27.d27workshop.repo.ReviewRepo;

@Service
public class GameReviewService {

    @Autowired
    GameRepo gameRepo;

    @Autowired
    ReviewRepo reviewRepo;

    public void setReviewId(Review r){
        String hexAlphabet = RandomStringUtils.randomAlphabetic(4);
        String hexNum = RandomStringUtils.randomNumeric(4);
        StringBuilder sb = new StringBuilder();
        String reviewId = sb.append(hexAlphabet).append(hexNum).toString();
        r.setId(reviewId);
    }
    
    public Document getGamebyId(Integer id){
        return gameRepo.getGamebyId(id);
    }

    public Document insertReview(Review review) {
        Document reviewDocument = new Document();
        reviewDocument.append("_id", review.getId());
        reviewDocument.append("user", review.getUser());
        reviewDocument.append("rating", review.getRating());
        reviewDocument.append("comment", review.getComment());
        reviewDocument.append("gameId", review.getGameId());
        reviewDocument.append("posted", review.getPosted().toString());
        reviewDocument.append("gameName", review.getGameName());

        return reviewRepo.insertReview(reviewDocument);
    }

    public Boolean doesReviewExist(String id){
        return reviewRepo.doesReviewExist(id);
    }

    public Boolean updateReview(String id,Document update){
        update.append("posted", new Date().toString());
        System.out.println("UPDATE DOCUMENT LOOKS LIKE THIS NOW" + update);
        return reviewRepo.updateReview(id, update);
    }

    public Document getReviewById (String id){
        return reviewRepo.getReviewByID(id);
    }

    public Boolean isReviewEdited(Document d){
        return reviewRepo.isReviewEdited(d);
    }

}
