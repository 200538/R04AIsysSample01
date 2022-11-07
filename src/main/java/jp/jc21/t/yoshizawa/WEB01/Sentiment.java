package jp.jc21.t.yoshizawa.WEB01;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class Sentiment{

	public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
		Sentimental message = getLanguage("今日はいい天気！");
		if (message != null) {
			System.out.println("negative：" + message.documents[0].confidenceScores.negative);
			System.out.println("newtral：" +message.documents[0].confidenceScores.newtral);
			System.out.println("positive：" +message.documents[0].confidenceScores.positive);
		}
	}

	static Sentimental getLanguage(String s) throws IOException, URISyntaxException, InterruptedException {
		Gson gson = new Gson();

		String url = "https://r04jk3a02-text.cognitiveservices.azure.com/"+ "text/analytics/v3.0/sentiment";
		Map<String, String> map = new HashMap<>();
		
		map.put("Ocp-Apim-Subscription-Key", "c1ea252280db440897b6449cb530be36");

		Doc doc = new Doc();
		doc.id = "1";
		doc.text = s;

		Sourc src = new Sourc();
		src.documents = new Doc[1];
		src.documents[0] = doc;

		String jsonData = new Gson().toJson(src);

		InetSocketAddress proxy = new InetSocketAddress("172.17.0.2", 80);
		
		JsonReader reader = WebApiConnector.postJsonReader(url, proxy, map, jsonData);
		//JsonReader reader = WebApiConnector.postJsonReader(url,map,jsonData);
		Sentimental message = null;
		if (reader != null) {
			message = gson.fromJson(reader,Sentimental.class);
			reader.close();
		}
		return message;
	}

}

class Sentimental{
	Document[] documents;
	String[] errors;
	String modelVersion;
}

class Document{
	confidenceScores confidenceScores;
	String id;
}

class confidenceScores {
	float negative;
	float newtral;
	float positive;
}

class Sourc {
	Doc[] documents;
}

class Doc {
	String id;
	String text;
}
