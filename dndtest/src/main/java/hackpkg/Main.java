package hackpkg;

import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bson.Document;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;

public class Main {
	
	public static String getImageUrl(String url){
		return url;
	}

	public static void main(String[] args) {
		
		java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
		
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		
		MongoDatabase db = mongoClient.getDatabase("dnd");
		
		MongoCollection<Document> collection = db.getCollection("monsters");
		
		final ClarifaiClient client = new ClarifaiBuilder("ZRGRVhSvL-ns5ZSLMRlr2rB8l_hgwrL1OuQBiukg", "xRjh5V5UwPoxBhhtW5DhJWKVla7pCFYHWvWfzmqh").buildSync();

		client.getToken();
				
		String url = getImageUrl(args[0].toString());
		//test String url = getImageUrl("http://cdn.quotationof.com/images/ape-quotes-5.jpg");
		
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
		
		String[] arrayOfKeys = new String[18];
	    int i = 0;
		while(matcher.find() && i < 18){
			arrayOfKeys[i] = matcher.group(1);
			arrayOfKeys[i] = arrayOfKeys[i].substring(0, 1).toUpperCase() + arrayOfKeys[i].substring(1);
			i++;
			
		}
		
		Document myDoc = collection.find().first();
		
		for(int k = 0; k < arrayOfKeys.length; k++){

			myDoc = collection.find(eq("name", arrayOfKeys[k])).first();
			
			if(myDoc != null) {
				System.out.println(myDoc);
			}
			
		}
		
		mongoClient.close();
		System.exit(0);
		
	}

}
