package paf.day27.d27workshop.model;

import java.util.Date;
import java.util.List;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    String id;
    String user;
    Integer rating;
    String comment; 
    Integer gameId;
    Date posted;
    String gameName;
    List<Document> editedHistory;

    public JsonObject toJson(){
        return Json.createObjectBuilder()
            .add("user", getUser())
            .add("rating", getRating())
            .add("comment", getComment())
            .add("gameId", getGameId())
            .add("posted", getPosted().toString())
            .add("gameName", getGameName())
            .build();
    }
    
}
