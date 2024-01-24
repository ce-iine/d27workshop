package paf.day27.d27workshop.repo;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepo {

    @Autowired
    MongoTemplate mongoTemplate;

    public Document getGamebyId(Integer gid){
        Query q = new Query();
        q.addCriteria(Criteria.where("gid").is(gid));
        Document game = mongoTemplate.findOne(q, Document.class, "game");
        System.out.println("HEREEEE" + game);
        return game;
    }
    
}
