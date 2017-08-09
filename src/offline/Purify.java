
package offline;

import java.io.BufferedReader;
import java.io.FileReader;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.UpdateOptions;

import db.mongodb.MongoDBUtil;

public class Purify {
	public static void main(String[] args) {
		MongoClient mongoClient = new MongoClient();
		MongoDatabase db = mongoClient.getDatabase(MongoDBUtil.DB_NAME);
		String fileName = "/Users/guyuanguo/Documents/ratings_Musical_Instruments.csv";
		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine()) != null) {
				String[] values = line.split(",");
				IndexOptions indexOptions = new IndexOptions().unique(true);
				db.getCollection("ratings").createIndex(new Document().append("user", 1).append("item", 1), indexOptions);
				UpdateOptions updateOptions = new UpdateOptions();
				updateOptions.upsert(true);
				db.getCollection("ratings")
						.updateOne(new Document().append("user", values[0]).append("item", values[1]),
								new Document().append("$set", new Document()
										.append("user", values[0])
										.append("item", values[1])
										.append("rating",
												Double.parseDouble(values[2]))), updateOptions);
			}
			System.out.println("Import Done!");
			bufferedReader.close();
			mongoClient.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
