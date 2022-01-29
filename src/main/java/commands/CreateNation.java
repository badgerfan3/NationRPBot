package commands;

import Sql.SQLFunctions;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CreateNation implements MessageCreateListener {
    String prefix;
    SQLFunctions functions;
    public CreateNation(String prefix, SQLFunctions functions){
        this.prefix = prefix;
        this.functions = functions;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        Scanner scanner = new Scanner(event.getMessageContent());
        long userID;
        if(event.getMessageAuthor().isUser()) {
            if (scanner.hasNext() && scanner.next().equalsIgnoreCase(prefix + "createNation") && scanner.hasNext()) {
                String userString = scanner.next();
                System.out.println(userString.length());
                if (userString.length() == 22 && userString.substring(0, 3).equalsIgnoreCase("<@!")) {
                    userString = userString.substring(3, 21);
                    System.out.println(userString);

                    try {
                        userID = Long.parseLong(userString);

                        ResultSet results = functions.returnNation(userID);

                        if (results.next()) {
                            event.getChannel().sendMessage("<@!" + userID + "> already has a nation!");
                        } else if (scanner.hasNext()) {
                            String nationName = scanner.next();
                            if (scanner.hasNext()) {
                                String flagURL = scanner.next();

                                functions.createNation(userID, nationName, flagURL);

                                TextChannel channel = event.getChannel();

                                new MessageBuilder()
                                        .append("Created a new nation for <@!" + userID + "> !")
                                        .setEmbed(new EmbedBuilder()
                                                .setTitle(nationName)
                                                .setDescription("Starting stats for " + nationName)
                                                .addInlineField("Population Score:", "0.0")
                                                .addInlineField("Population:", "15k")
                                                .addField("Economy Score:", "0")
                                                .addInlineField("Stability:", "0.0")
                                                .setImage(flagURL)
                                                .setColor(Color.ORANGE)
                                                .setAuthor("The Brain"))
                                        .send(event.getChannel());
                            }
                        }
                    } catch (NumberFormatException | SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
