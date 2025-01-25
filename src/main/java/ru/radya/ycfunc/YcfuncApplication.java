package ru.radya.ycfunc;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Date;
import com.google.gson.Gson;

public class YcfuncApplication {

	public static class KeyInfo {

		public String id;
		public String service_account_id;
		public String private_key;
		public Date created_at;
		public String key_algorithm;
		public String public_key;
	}

	public static void main(String[] args) throws Exception {
		myFunc();
//		System.out.println(changeJwt());
	}

	private static String getToken() throws Exception {

		String content = new String(Files.readAllBytes(Paths.get("authorized_key.json")));
		KeyInfo keyInfo = (new ObjectMapper()).readValue(content, KeyInfo.class);

		String privateKeyString = keyInfo.private_key;
		String serviceAccountId = keyInfo.service_account_id;
		String keyId = keyInfo.id;

		PemObject privateKeyPem;
		try (PemReader reader = new PemReader(new StringReader(privateKeyString))) {
			privateKeyPem = reader.readPemObject();
		}

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyPem.getContent()));

		Instant now = Instant.now();

		// Формирование JWT.
		String encodedToken = Jwts.builder()
				.claim("kid", keyId)
				.setHeaderParam("kid", keyId)
				.setIssuer(serviceAccountId)
				.setAudience("https://iam.api.cloud.yandex.net/iam/v1/tokens")
				.setIssuedAt(Date.from(now))
				.setExpiration(Date.from(now.plusSeconds(3600)))
				.signWith(privateKey, SignatureAlgorithm.PS256)
				.compact();
		return encodedToken;
	}

	private static String changeJwt() throws Exception {
		URI uri = URI.create("https://iam.api.cloud.yandex.net/iam/v1/tokens");

		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.POST(HttpRequest.BodyPublishers.ofString("{\"jwt\": \"" + getToken() + "\"}"))
				.header("Content-Type", "application/json")
				.build();
		HttpClient client = HttpClient.newHttpClient();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		Gson gson = new Gson();

		IamEntity iamEntity = gson.fromJson(response.body(), IamEntity.class);

		return iamEntity.getIamToken();
	}

	private static void myFunc() throws Exception {
		URI uri = URI.create("https://functions.yandexcloud.net/d4e60pkj532g5refdc6k?name=radya100");//&integration=raw");

		HttpRequest request = HttpRequest.newBuilder()
				.GET()
				.uri(uri)
				.version(HttpClient.Version.HTTP_1_1)
				.header("Accept", "text/html")
//				.header("Authorization", "Bearer t1.9euelZrIm5OKzoyUys-WkZaTycyUk-3rnpWajpKQmIqenpWOzJ6OnpfKmpjl8_dHTzBD-e9cMXQn_t3z9wd-LUP571wxdCf-zef1656Vmo6dlMeNm5yRx56ZzIrIjIqP7_zF656Vmo6dlMeNm5yRx56ZzIrIjIqP.pdNKJg49-M6-VIW6GwEgISkR-iN26w8e95aPRi0ucNaRW5FWKXACfFGUR5suR3tc9hHYBx84BfRZxDnRU_RFAw")
				.header("Authorization", "Bearer " + changeJwt())
				.build();

		HttpClient client = HttpClient.newHttpClient();

		// получаем стандартный обработчик тела запроса с конвертацией содержимого в строку
		HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

		// отправляем запрос и получаем ответ от сервера
		HttpResponse<String> response = client.send(request, handler);

		// выводим код состояния и тело ответа
		System.out.println("Код ответа: " + response.statusCode());
		System.out.println("Тело ответа: " + response.body());
		System.out.println("Заголовки: " + response.headers().toString());
	}

}



