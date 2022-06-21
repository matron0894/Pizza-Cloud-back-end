package com.springproject.prodcloud.email;

import com.springproject.prodcloud.email.dao.EmailIngredient;
import com.springproject.prodcloud.email.dao.EmailOrder;
import com.springproject.prodcloud.email.dao.EmailProduct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.integration.mail.transformer.AbstractMailMessageTransformer;
import org.springframework.integration.support.AbstractIntegrationMessageBuilder;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Handles email content as taco orders where...</p>
 * <li> The order's email is the sender's email</li>
 * <li> The email subject line *must* be "PRODUCT ORDER" or else it will be ignored</li>
 * <li> Each line of the email starts with the name of a taco design, followed by a colon,
 * followed by one or more ingredient names in a comma-separated list.</li>
 *
 * <p>The ingredient names are matched against a known set of ingredients using a LevenshteinDistance
 * algorithm. As an example "beef" will match "GROUND BEEF" and be mapped to "GRBF"; "corn" will
 * match "Corn Tortilla" and be mapped to "COTO".</p>
 *
 * <p>An example email body might look like this:</p>
 *
 * <code>
 * Corn Carnitas: corn, carnitas, lettuce, tomatoes, cheddar<br/>
 * Veggielicious: flour, tomatoes, lettuce, salsa
 * </code>
 *
 * <p>This will result in an order with two tacos where the names are "Corn Carnitas" and "Veggielicious".
 * The ingredients will be {COTO, CARN, LETC, TMTO, CHED} and {FLTO,TMTO,LETC,SLSA}.</p>
 */

@Slf4j
@Component
public class EmailToOrderTransformer extends AbstractMailMessageTransformer<EmailOrder> {

    private static final String SUBJECT_KEYWORDS = "ORDER";


    //удобный базовый класс для обработки сообщений, чья полезная нагрузка - это электронная почта.
    // Он заботится о извлечении информации электронной почты из входящего сообщения в объект Message
    @Override
    protected AbstractIntegrationMessageBuilder<EmailOrder> doTransform(Message mailMessage) throws Exception {
        EmailOrder emailOrder = processPayload(mailMessage);
        assert emailOrder != null;
        return MessageBuilder.withPayload(emailOrder);
    }

    private EmailOrder processPayload(Message mailMessage) {
        try {
            String subject = mailMessage.getSubject();
            if (subject.toUpperCase().contains(SUBJECT_KEYWORDS)) {

                /// get user's name
                String email = ((InternetAddress) mailMessage.getFrom()[0]).getPersonal(); //Мария Батырева
                if (email == null) email = ((InternetAddress) mailMessage.getFrom()[0]).getAddress();

                String content = getTextFromMessage(mailMessage);
                return parseEmailToOrder(email, content);
            }
        } catch (MessagingException | IOException e) {
            log.error("MessagingException: {%s}", e);
        }
        return null;
    }

    private String getTextFromMessage(Message message) throws IOException, MessagingException{
        Object content = message.getContent();
        if (content instanceof String) {
            return message.getContent().toString();
        } else if (content instanceof MimeMultipart) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            return getTextFromMimeMultipart(mimeMultipart);
        }
        return "";
    }

    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) {
        try {
            int count = mimeMultipart.getCount();
            for (int i = 0; i < count; i++) {
                BodyPart content = mimeMultipart.getBodyPart(i);

                if (content.getContentType().contains("text/plain")) { //text/plain; charset="UTF-8
                    System.out.println("=========  multipart/* 2 ====================");
                    return content.getContent().toString();
                } else if (content.getContent() instanceof MimeMultipart) {
                    return "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    private EmailOrder parseEmailToOrder(String email, String content) {
        EmailOrder emailOrder = new EmailOrder(email);
        String[] lines = content.split("\\r?\\n");

        for (String line : lines) {
            if (line.trim().length() > 0 && line.contains(":")) {
                String[] lineSplit = line.split(":");

                String productName = lineSplit[0].trim();
                String ingredients = lineSplit[1].trim();

                String[] ingredientsSplit = ingredients.split(",");
                List<String> ingredientCodes = new ArrayList<>();

                for (String ingredientName : ingredientsSplit) {
                    String code = lookupIngredientCode(ingredientName.trim());
                    if (code != null) {
                        ingredientCodes.add(code);
                    }
                }
                EmailProduct product = new EmailProduct(productName);
                product.setIngredients(ingredientCodes);
                emailOrder.addTaco(product);
            }
        }
        return emailOrder;
    }

    private String lookupIngredientCode(String ingredientName) {
        for (EmailIngredient ingredient : ALL_INGREDIENTS) {
            String ucIngredientName = ingredientName.toUpperCase();
            if (LevenshteinDistance.getDefaultInstance().apply(ucIngredientName, ingredient.getName()) < 3 ||
                    ucIngredientName.contains(ingredient.getName()) ||
                    ingredient.getName().contains(ucIngredientName)) {
                return ingredient.getCode();
            }
        }
        return null;
    }


    private static EmailIngredient[] ALL_INGREDIENTS = new EmailIngredient[]{
            new EmailIngredient("FLTO", "FLOUR TORTILLA"),
            new EmailIngredient("COTO", "CORN TORTILLA"),
            new EmailIngredient("GRBF", "GROUND BEEF"),
            new EmailIngredient("CARN", "CARNITAS"),
            new EmailIngredient("TMTO", "TOMATOES"),
            new EmailIngredient("LETC", "LETTUCE"),
            new EmailIngredient("CHED", "CHEDDAR"),
            new EmailIngredient("JACK", "MONTERREY JACK"),
            new EmailIngredient("SLSA", "SALSA"),
            new EmailIngredient("SRCR", "SOUR CREAM")
    };
}


//    private static Ingredient[] ALL_INGREDIENTS = new Ingredient[]{
//            new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP),
//            new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP),
//            new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN),
//            new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN),
//            new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES),
//            new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES),
//            new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE),
//            new Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE),
//            new Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE),
//            new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE)
//    };



