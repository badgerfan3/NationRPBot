package commands;

import Sql.SQLFunctions;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Info implements MessageCreateListener {
    String prefix;
    SQLFunctions functions;
    DiscordApi api;

    public Info(String prefix, SQLFunctions functions, DiscordApi api){this.prefix=prefix; this.functions = functions; this.api = api;}

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        Scanner scanner = new Scanner(event.getMessageContent());
        long userID;
        String arg = "";

        if(event.getMessageAuthor().isUser()) {
            if(scanner.hasNext())
                arg = scanner.next();

            if (arg.equalsIgnoreCase(prefix + "info") && scanner.hasNext()) {
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
                            String nationName = results.getString("NATION_NAME");
                            float popScore = results.getFloat("POP_SCORE");
                            int pop = results.getInt("POP");
                            int economy = results.getInt("ECONOMY_SCORE");
                            float stability = results.getFloat("STABILITY");
                            String flagURL = results.getString("FLAG");

                            new MessageBuilder()
                                    .append("<@!" + userID + ">'s Nation Stats!")
                                    .setEmbed(new EmbedBuilder()
                                            .setTitle(nationName)
                                            .addInlineField("Population Score:", String.valueOf(popScore))
                                            .addInlineField("Population:", String.valueOf(pop))
                                            .addField("Economy Score:", String.valueOf(economy))
                                            .addInlineField("Stability:", String.valueOf(stability))
                                            .setImage(flagURL)
                                            .setColor(Color.BLUE)
                                            .setAuthor("The Brain"))
                                    .send(event.getChannel());
                        } else {
                            event.getChannel().sendMessage("<@!" + userID + "> does not own a nation!");
                        }
                    } catch (NumberFormatException | SQLException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Attempted to get the Info of USER: " + userString);
            } else if (arg.equalsIgnoreCase(prefix + "info")) {
                ResultSet results = functions.returnNation(null, event.getServer().get().getId());
                boolean resultsF = false;
                try {
                        while (results.next()) {
                            resultsF = true;
                            String nationName = results.getString("NATION_NAME");
                            float popScore = results.getFloat("POP_SCORE");
                            int pop = results.getInt("POP");
                            int economy = results.getInt("ECONOMY_SCORE");
                            float stability = results.getFloat("STABILITY");
                            String flagURL = results.getString("FLAG");
                            userID = results.getLong("ID");

                            new MessageBuilder()
                                    .append("<@!" + userID + ">'s Nation Stats!")
                                    .setEmbed(new EmbedBuilder()
                                            .setTitle(nationName)
                                            .addInlineField("Population Score:", String.valueOf(popScore))
                                            .addInlineField("Population:", String.valueOf(pop))
                                            .addField("Economy Score:", String.valueOf(economy))
                                            .addInlineField("Stability:", String.valueOf(stability))
                                            .setImage(flagURL)
                                            .setColor(Color.BLUE)
                                            .setAuthor("The Brain"))
                                    .send(event.getChannel());

                        }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (!resultsF) {
                    event.getChannel().sendMessage("The database is empty! You have no users!");
                }
            }
        }
    }
}
