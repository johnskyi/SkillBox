
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.Arrays;
import java.util.Collections;

import static com.mongodb.client.model.Filters.eq;


public class MbLoader {


    private final MongoClient mongoClient = new MongoClient("127.0.0.1", 27017);

    private final MongoDatabase database = mongoClient.getDatabase("local");

    private final MongoCollection<Document> collectionShop = database.getCollection("shops");
    private final MongoCollection<Document> collectionProducts = database.getCollection("products");

    public MbLoader() {
        database.drop();
        collectionShop.drop();
        collectionProducts.drop();

    }

    //Добавление магазина в базу
    public void addShop(String shop) {
        collectionShop.insertOne(new Document().append("name", shop).append("products", Collections.emptyList()));
    }

    //Добавление продукта в базу
    public void addProduct(String product, int price) {
        collectionProducts.insertOne(new Document().append("name", product).append("price", price));
    }

    //Добавление товара в магазин
    public void addInShop(String shop, String product) {
            Bson updateShop = collectionShop.find(eq("name", shop)).first();
            Bson updateValue = new Document("products", product);
            Bson updateOp = new Document("$addToSet", updateValue);
            collectionShop.updateOne(updateShop, updateOp);
            System.out.println("Выставлен товар " + "\"" + product + "\" в магазин " + "\""
                    + shop + "\"");

    }

    // СТАТИСТИКА ПО ТОВАРАМ

    public void getStat() {
        getProductsCount();
        getCheapestAndExpensive();
        getCountProductsCheaperThan100();
    }

    // Количество товаров
    public void getProductsCount() {
        AggregateIterable<Document> result = collectionShop.aggregate(Arrays.asList(
                new Document("$lookup", new Document("from", "products")
                        .append("localField", "products.name")
                        .append("foreignField", "products.name")
                        .append("as", "productsList")),
                new Document("$unwind", new Document("path", "$products")),
                new Document("$group", new Document("_id", "$name")
                        .append("productsCount", new Document("$sum", 1L))),
                new Document("$sort", new Document("_id", -1L))));

        for (Document d : result) {
            System.out.println(d);
        }
    }

    // Среднее, Минимальное, Максимальное
    public void getCheapestAndExpensive() {
        AggregateIterable<Document> result1 = collectionProducts.aggregate(Arrays.asList(
                new Document("$lookup", new Document("from", "shops")
                        .append("localField", "name")
                        .append("foreignField", "products")
                        .append("as", "shopsList")),
                new Document("$unwind", new Document("path", "$shopsList")),
                new Document("$sort", new Document("price", -1L)),
                new Document("$group", new Document("_id", "$shopsList.name")
                        .append("averagePrice", new Document("$avg", "$price"))
                        .append("maxPriseGoods", new Document("$first", "$name"))
                        .append("minPriseGoods", new Document("$last", "$name"))),
                new Document("$sort", new Document("_id", -1L))));

        for (Document d : result1) {
            System.out.println(d);
        }
    }

    //количество товаров дешевле 100 рублей
    public void getCountProductsCheaperThan100() {
        AggregateIterable<Document> result2 = collectionProducts.aggregate(Arrays.asList(
                new Document("$lookup", new Document("from", "shops")
                        .append("localField", "name")
                        .append("foreignField", "products")
                        .append("as", "shopsList")),
                new Document("$unwind", new Document("path", "$shopsList")),
                new Document("$match", new Document("price", new Document("$lt", 100L))),
                new Document("$group", new Document("_id", "$shopsList.name")
                        .append("lt100GoodsCount", new Document("$sum", 1L))),
                new Document("$sort", new Document("_id", -1L))));

        for (Document d : result2) {
            System.out.println(d);
        }
    }
}
