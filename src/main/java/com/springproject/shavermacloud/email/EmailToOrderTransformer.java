package com.springproject.shavermacloud.email;

import com.springproject.shavermacloud.email.dao.EmailIngredient;
import com.springproject.shavermacloud.email.dao.EmailOrder;
import com.springproject.shavermacloud.email.dao.EmailProduct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.util.MimeMessageParser;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.jsoup.Jsoup;
import org.springframework.integration.mail.transformer.AbstractMailMessageTransformer;
import org.springframework.integration.support.AbstractIntegrationMessageBuilder;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

                /*
                //get data
                Object content = mailMessage.getContent();
                String contentReturn = null;

                if (content instanceof String) {
                    contentReturn = (String) content;
                    System.out.println(contentReturn);
                } else {

                    mailMessage.writeTo(System.out);
                    ByteArrayOutputStream baos = null;
                    try {
                        baos = new ByteArrayOutputStream();
                        mailMessage.writeTo(baos);
                    } catch (Exception ignored) {
                    } finally {
                        try {
                            Objects.requireNonNull(baos).close();
                        } catch (Exception e) {
                            log.error("Couldn't close byte array output stream");
                        }
                    }
                    String s = baos.toString(StandardCharsets.UTF_8);
                    System.out.println(s);
                }*/
                String incontent = getTextFromMessage(mailMessage);
                return parseEmailToOrder(email, incontent);
            }
        } catch (MessagingException e) {
            log.error("MessagingException: {%s}", e);
        } catch (IOException e) {
            log.error("IOException: {%s}", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getTextFromMessage(Message message) throws Exception {
        String result = "";
        MimeMessage mp = (MimeMessage) message;

        if (message.isMimeType("text/plain")) {
            String data = new MimeMessageParser(mp).parse().getPlainContent();
            result = message.getContent().toString();
        }

        if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            mimeMultipart.writeTo(System.out);
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) {
        try {
            for (int i = 0; i < mimeMultipart.getCount(); i++) {
                BodyPart bodyPart = mimeMultipart.getBodyPart(i);
                String type = bodyPart.getContentType(); //text/plain; charset="UTF-8
                if (type.contains("text/plain")) { //text/plain; charset="UTF-8
                    //mimeMultipart.writeTo(System.out);
                    ByteArrayOutputStream baos = null;
                    try {
                        baos = new ByteArrayOutputStream();
                        mimeMultipart.writeTo(baos);
                    } catch (Exception ignored) {
                    } finally {
                        try {
                            Objects.requireNonNull(baos).close();
                        } catch (Exception e) {
                            log.error("Couldn't close byte array output stream");
                        }
                    }
                    return "";
                } else if (bodyPart.getContent() instanceof MimeMultipart) {
                    return "";
                }
            }
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
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



