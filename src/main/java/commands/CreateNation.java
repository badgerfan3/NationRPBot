package commands;

import Sql.SQLFunctions;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CreateNation implements MessageCreateListener {
    String prefix;
    SQLFunctions functions;
    DiscordApi api;
    long userID;
    String flagURL;
    public CreateNation(String prefix, SQLFunctions functions, DiscordApi api){
        this.prefix = prefix;
        this.functions = functions;
        this.api = api;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        Scanner scanner = new Scanner(event.getMessageContent());
        if(event.getMessageAuthor().isUser()) {
            if (scanner.hasNext() && scanner.next().equalsIgnoreCase(prefix + "createNation") && scanner.hasNext()) {
                String userString = scanner.next();
                System.out.println(userString.length());
                if (userString.length() == 22 && userString.substring(0, 3).equalsIgnoreCase("<@!")) {
                    userString = userString.substring(3, 21);
                    System.out.println(userString);

                    try {
                        userID = Long.parseLong(userString);
                        ResultSet results = null;
                        if(event.getServer().isPresent()) {
                            results = functions.returnNation(userID, event.getServer().get().getId());
                        }

                        assert results != null;
                        if (results.next()) {
                            event.getChannel().sendMessage("<@!" + userID + "> already has a nation!");
                        } else if (scanner.hasNext()) {
                            flagURL = scanner.next();
                            URL url = new URL(flagURL);
                            if (scanner.hasNext()) {
                                StringBuilder nationName = new StringBuilder();
                                while (scanner.hasNext()){
                                    nationName.append(scanner.next()).append(" ");
                                }
                                functions.createNation(userID, nationName.toString(), flagURL, event.getServer().get().getId());

                                TextChannel channel = event.getChannel();


                                System.out.println("Didnt fail");
                                new MessageBuilder()
                                        .append("Created a new nation for <@!" + userID + "> !")
                                        .setEmbed(new EmbedBuilder()
                                                .setTitle(nationName.toString())
                                                .setDescription("Starting stats for " + nationName)
                                                .addInlineField("Population Score:", "0.0")
                                                .addInlineField("Population:", "15k")
                                                .addField("Economy Score:", "0")
                                                .addInlineField("Stability:", "0.0")
                                                .setImage(url.toString())
                                                .setColor(Color.ORANGE)
                                                .setAuthor("The Brain"))
                                        .send(event.getChannel());
                            }
                        }
                    } catch (NumberFormatException | SQLException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        if(!event.getMessageAttachments().isEmpty()){
                            StringBuilder nationName = new StringBuilder(flagURL);
                            if(scanner.hasNext()){
                                while(scanner.hasNext()) {
                                    nationName.append(" ").append(scanner.next());
                                }
                                String flagURL = event.getMessageAttachments().get(0).getUrl().toString();
                                try {
                                    functions.createNation(userID, nationName.toString(), flagURL, event.getServer().get().getId());
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }

                                new MessageBuilder()
                                        .append("Created a new nation for <@!" + userID + "> !")
                                        .setEmbed(new EmbedBuilder()
                                                .setTitle(nationName.toString())
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
                    }
                }
            }
        }
    }
}
