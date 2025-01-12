package ru.radya.ycfunc;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class YcfuncApplication {

	public static void main(String[] args) throws IOException, InterruptedException {
		URI uri = URI.create("https://functions.yandexcloud.net/d4e60pkj532g5refdc6k?name=radya100");//&integration=raw");

		HttpRequest request = HttpRequest.newBuilder()
				.GET()
				.uri(uri)
				.version(HttpClient.Version.HTTP_1_1)
				.header("Accept", "text/html")
				.header("Authorization", "Bearer t1.9euelZrMiZScyc6QnZWJkJPKlJeKzO3rnpWajpKQmIqenpWOzJ6OnpfKmpjl8_d-CXJD-e9yQ0Z5_N3z9z44b0P573JDRnn8zef1656VmpuSzombz8iZisfPj5eLnIud7_zF656VmpuSzombz8iZisfPj5eLnIud._Rws2SY1w1E0H-ikqmaK5csAqo-mofiH0cAPOMV_Q8fgYc0iHYoXMAryHR0I6yPdw_oibinPJ7QGhA71BnrSCA")
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
