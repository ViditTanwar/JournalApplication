
package com.vidittanwar.journalApp.Entity;


import lombok.*;
        import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "journal_entries")
@Data
@NoArgsConstructor
public class JournalEntry {
    @Id
    private ObjectId id;
    public String getId(){
        return id!=null ? id.toHexString():null;
    }
    @NonNull
    private String title;
    private String  content;
}