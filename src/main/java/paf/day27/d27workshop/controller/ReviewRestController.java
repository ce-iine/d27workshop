package paf.day27.d27workshop.controller;

import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import paf.day27.d27workshop.model.Review;
import paf.day27.d27workshop.service.GameReviewService;

@RestController
@RequestMapping
public class ReviewRestController {

    @Autowired
    GameReviewService grSvc;

    @PostMapping("/review")
    public ResponseEntity<String> insertReview(@ModelAttribute Review review) {
        review.setPosted(new Date());
        grSvc.setReviewId(review); 
        Document game = grSvc.getGamebyId(review.getGameId());
        // game.getString("name");
        review.setGameName(game.getString("name"));
        System.out.println("REVIEW LOOKS LIKE THIS NOWWWW>>>>>" + review);

        grSvc.insertReview(review);
        JsonObject result = null;
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("review", review.toJson());
        result = objectBuilder.build();

        if (game.getInteger("gid") == null) {
            ResponseEntity.status(404).body("not found");
        }

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.toString());
    }

    @PutMapping("/review/{id}")
    public ResponseEntity<String> updateReview(@PathVariable String id, @RequestBody Document update) {
        if (!grSvc.doesReviewExist(id)) {
            JsonObject notFound = Json.createObjectBuilder()
                    .add("error", "No review with ID " + id + " found")
                    .build();
            return new ResponseEntity<String>(notFound.toString(), HttpStatus.NOT_FOUND);
        }

        if (update.getInteger("rating") > 10 || update.getInteger("rating") < 1) {
            JsonObject errorJson = Json.createObjectBuilder()
                    .add("error", update.getInteger("rating") + " is not a valid rating between 1 - 10.")
                    .build();
            return new ResponseEntity<String>(errorJson.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (grSvc.updateReview(id, update)) {
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("reviewUpdated");
        }
        JsonObject errorJson = Json.createObjectBuilder()
                .add("error", "There has been an error updating your review. Please try again.")
                .build();

        return new ResponseEntity<String>(errorJson.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/review/{id}")
    public ResponseEntity<String> displayReview(@PathVariable String id) {
        Document d = grSvc.getReviewById(id);

        if (d == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("review does not exist");
        }

        if (grSvc.isReviewEdited(d)) {
            Date date = new Date();
            JsonObject withEdit = Json.createObjectBuilder()
                    .add("user", d.getString("user"))
                    .add("rating", d.getInteger("rating"))
                    .add("comment", d.getString("comment"))
                    .add("gameId", d.getInteger("gameId"))
                    .add("posted", d.getString("posted"))
                    .add("gameName", d.getString("gameName"))
                    .add("edited", grSvc.isReviewEdited(d))
                    .add("timestamp", date.toString())
                    .build();
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(withEdit.toString());
        }
        
        Date date = new Date();
        JsonObject withoutEdit = Json.createObjectBuilder()
                .add("user", d.getString("user"))
                .add("rating", d.getInteger("rating"))
                .add("comment", d.getString("comment"))
                .add("gameId", d.getInteger("gameId"))
                .add("posted", d.getString("posted"))
                .add("gameName", d.getString("gameName"))
                .add("edited", grSvc.isReviewEdited(d))
                .add("timestamp", date.toString())
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(withoutEdit.toString());
    }

    @GetMapping("/review/{id}/history")
    public ResponseEntity<String> editHistory(@PathVariable String id) {

        if (!grSvc.doesReviewExist(id)) {
            JsonObject errorJson = Json.createObjectBuilder()
                    .add("error", "No review found with ID " + id + ".")
                    .build();

            return new ResponseEntity<String>(errorJson.toString(), HttpStatus.NOT_FOUND);
        }

        Document getReviewDoc = grSvc.getReviewById(id);

        List<Document> editedHistory = getReviewDoc.getList("edited", Document.class);
        JsonArrayBuilder displayHistory = Json.createArrayBuilder();

        for (Document d : editedHistory) {
            JsonObject comment = Json.createObjectBuilder()
                    .add("comment", d.getString("comment"))
                    .add("rating", d.getInteger("rating"))
                    .add("posted", d.getString("posted"))
                    .build();

            displayHistory.add(comment);
        }

        JsonObject reviewJson = Json.createObjectBuilder()
                .add("user", getReviewDoc.getString("user"))
                .add("rating", getReviewDoc.getInteger("rating"))
                .add("comment", getReviewDoc.getString("comment"))
                .add("gameId", getReviewDoc.getInteger("gameId"))
                .add("posted", getReviewDoc.getString("posted"))
                .add("gameName", getReviewDoc.getString("gameName"))
                .add("edited", displayHistory.build()) //jsonArray build
                .add("timestamp", new Date().toString())
                .build();

        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(reviewJson.toString());
    }
}
