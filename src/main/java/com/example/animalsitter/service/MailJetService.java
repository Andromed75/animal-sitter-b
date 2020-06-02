package com.example.animalsitter.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.example.animalsitter.domain.User;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MailJetService {

	public static final String SUBJECT_CREATION = "Bienvenue sur Animal Sitter !";
	
		public void sendMail(User user, String message, String subject) throws MailjetException, MailjetSocketTimeoutException {
		log.info("sendCreatedAccountMail ");
		MailjetClient client;
		MailjetRequest request;
		MailjetResponse response;
		client = new MailjetClient("c373d4017704d54b48dfe448e695b126", "4db4d06eb9c77b7b6924ade63c8aa632", new ClientOptions("v3.1"));
		request = new MailjetRequest(Emailv31.resource).property(Emailv31.MESSAGES,
				new JSONArray().put(new JSONObject()
						.put(Emailv31.Message.FROM, new JSONObject().put("Email", "ae.de-donno@groupeonepoint.com")
								.put("Name", "Animal Sitter"))
						.put(Emailv31.Message.TO,
								new JSONArray()
										.put(new JSONObject().put("Email", user.getEmail()).put("Name",
												user.getPseudo())))
						.put(Emailv31.Message.SUBJECT, subject)
						.put(Emailv31.Message.TEXTPART, "My first Mailjet email")
						.put(Emailv31.Message.HTMLPART, message)
						.put(Emailv31.Message.CUSTOMID, "AppGettingStartedTest")));
		response = client.post(request);
		System.out.println(response.getStatus());
		System.out.println(response.getData());
		log.info("Mailjet Request status : {}", response.getStatus());
		log.info("Mailjet Request data : {}", response.getData());
	}
		
	public String accountCreationMessage(String pseudo) {
		return "<div>\r\n" + 
				"  <h1 style=\"text-align: center; margin-top: 20px;\">BIENVENUE CHEZ ANIMAL SITTER !</h1><br>\r\n" + 
				"  <img style=\"margin: 0 auto;\r\n" + 
				"  display: block;\r\n" + 
				"  margin-bottom: 40px;\r\n" + 
				"  max-height: 200px;\" src=\"https://www.fourchette-et-bikini.fr/sites/default/files/styles/full_670x350/public/animal-compagnie-signe-astro.jpg?itok=4WY4oeP6\" alt=\"\">\r\n" + 
				"<p style=\"padding-left: 25px;\">" + pseudo +" vous faites maintenant partie de la famille Animal Sitter</p><br>\r\n" + 
				"<p style=\"padding-left: 25px;\">Vous pouvez maintenant créer une annonce pour vos animaux, ou consulter les annonces des autres utilisateurs !</p><br>\r\n" + 
				"<p style=\"padding-left: 25px;\">Retrouvez nous sur <a href=\"https://animal-502d9.firebaseapp.com/\">animal-sitter</a></p><br><br>\r\n" + 
				"<hr><br><br>\r\n" + 
				"<p style=\"padding-left: 25px;\">Toute l'équipe vous remercie pour la confiance que vous nous avez apporté</p><br><br>\r\n" + 
				"<p style=\"padding-left: 25px;\">Animal Sitter</p>\r\n" + 
				"</div>";
	}
}