package hackpkg;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;

public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		
		MongoDatabase db = mongoClient.getDatabase("dnd");
		
		MongoCollection collection = db.getCollection("monsters");
		
		MongoCursor<Document> cursor = collection.find().iterator();
		try {
		    while (cursor.hasNext()) {
		        System.out.println(cursor.next().toJson());
		    }
		} finally {
		    cursor.close();
		}
						
		mongoClient.close();
		
		final ClarifaiClient client = new ClarifaiBuilder("ZRGRVhSvL-ns5ZSLMRlr2rB8l_hgwrL1OuQBiukg", "xRjh5V5UwPoxBhhtW5DhJWKVla7pCFYHWvWfzmqh").buildSync();

		client.getToken();
		
		String url = "http://vignette1.wikia.nocookie.net/forgottenrealms/images/f/f8/Monster_Manual_5e_-_Bugbear_-_p33.jpg/revision/latest?cb=20141109231300";
		
		final List<ClarifaiOutput<Concept>> predictionResults =
			    client.getDefaultModels().generalModel() // You can also do client.getModelByID("id") to get custom models
			        .predict()
			        .withInputs(
			            ClarifaiInput.forImage(ClarifaiImage.of(url))
			        )
			        .executeSync()
			        .get();
		
		String names = predictionResults.get(0).toString();
		
		Pattern pattern = Pattern.compile("name=(\\w+),");
		
		Matcher matcher = pattern.matcher(names);
		
		while(matcher.find()){
			System.out.println(matcher.group(1));
		}

		
		
	}

}
