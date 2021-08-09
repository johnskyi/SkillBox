import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) throws IOException {

        MongoClient mongoClient = new MongoClient("127.0.0.1", 27017);

        MongoDatabase database = mongoClient.getDatabase("local");

        MongoCollection<Document> collection = database.getCollection("Students");
        collection.drop();

        String[] fragments = new String[2];
        List<String> lines = Files.readAllLines(Paths.get("src/test/resources/mongo.csv"));
        List<Document> documentList = new ArrayList<>();

        for (String line : lines) {
            fragments = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", 3);
            fragments[2] = fragments[2].replace('"',' ').trim();
            String[] courses = fragments[2].split(",");

            collection.insertOne(new Document()
                    .append("name",fragments[0])
                    .append("age",Integer.parseInt(fragments[1]))
                    .append("courses", Arrays.asList(courses)));
        }
        //Всего студентов
        System.out.println("Студентов в базе: " + collection.countDocuments());

        //Студентов старше 40
        BsonDocument queryGt40 = BsonDocument.parse("{ age: {$gt: 40}}");
        System.out.println("Студентов в базе старше 40: " + collection.countDocuments(queryGt40));

        //Самый молодой студент
        BsonDocument queryYoungest = BsonDocument.parse("{ age: 1}");
        final String[] name = new String[1];
        FindIterable<Document> iterable = collection.find()
                .sort(queryYoungest).limit(1);
        iterable.forEach((Consumer<Document>)document -> name[0] = document.get("name").toString());
        System.out.println("Имя самого молодого студента: " + name[0]);

        //Список курсов самого старшего студента

        BsonDocument queryOldest = BsonDocument.parse("{ age: -1}");
        final String[] courses = new String[1];
        FindIterable<Document> iterable1 = collection
                .find().sort(queryOldest).limit(1);
        iterable1.forEach((Consumer<Document>)document -> courses[0] =
                document.get("courses").toString());
        System.out.println("Список курсов самого старшего студента: " + courses[0]);


    }
}
