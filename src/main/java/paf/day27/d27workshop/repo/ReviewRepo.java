package paf.day27.d27workshop.repo;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;

import paf.day27.d27workshop.model.Review;

@Repository
public class ReviewRepo {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Document insertReview(Document d){
        return mongoTemplate.insert(d, "review"); 
    }

    public Boolean doesReviewExist(String id){
        return mongoTemplate.exists(Query.query(Criteria.where("_id").is(id)), "review");
    }

    public Boolean updateReview(String id, Document up){
        Query q = new Query();
        q.addCriteria(Criteria.where("_id").is(id));

        Document getReviewDoc = mongoTemplate.findOne(q, Document.class, "review");
        // Review getReviewDoc = mongoTemplate.findOne(q, Review.class);

        if (getReviewDoc.getList("edited", Document.class) ==null){
            List<Document> addEdited = new ArrayList<>();
            addEdited.add(up);
            // getReviewDoc.append("edited", addEdited);
            Update update = new Update()
                .set("comment", up.getString("comment"))
                .set("rating", up.getInteger("rating"))
                .push("edited").each(List.of(addEdited).toArray());

            UpdateResult result = mongoTemplate.upsert(q, update, "review");
            System.out.printf("Docs updated: %b", result.wasAcknowledged());
            
        } else{
            List<Document> addToHistory = getReviewDoc.getList("edited", Document.class);
            addToHistory.add(up);
            // getReviewDoc.append("edited", addToHistory);

            Update update = new Update()
                .set("comment", up.getString("comment"))
                .set("rating", up.getInteger("rating"))
                // .push("edited").value(addToHistory);
                .push("edited").each(List.of(addToHistory).toArray());

            UpdateResult result = mongoTemplate.upsert(q, update, "review");
            System.out.printf("Docs updated: %b", result.wasAcknowledged());
            return result.wasAcknowledged();
        }
        return false;
    }

    public Document getReviewByID(String reviewID) {
        Document d = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(reviewID)), Document.class, "review");
        System.out.println("CLR>>>> "+ d);
        return d;
    }

    public Boolean isReviewEdited(Document review) {
        List<Document> d = review.getList("edited", Document.class);
        System.out.println("LIST EXIST?" + d);

        if (d == null) {
            System.out.println("EDITED? FALSE");
            return false;
        } else {
            System.out.println("EDITED? TRUE");
            return true;
        }
    }

    
}
