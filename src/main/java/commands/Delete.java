package commands;

import Sql.SQLFunctions;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Delete implements MessageCreateListener {
    String prefix;
    SQLFunctions functions;
    boolean waitForConfirm;
    MessageAuthor user;
    DiscordApi api;

    public Delete(String prefix, SQLFunctions functions, DiscordApi api){
        this.prefix = prefix;
        this.functions = functions;
        this.api = api;
    }
    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        Scanner scanner = new Scanner(event.getMessageContent());
        long userID;
        if(event.getMessageAuthor().isUser()) {
            if (scanner.hasNext()) {
                String arg = scanner.next();
                if (arg.equalsIgnoreCase(prefix + "deleteNation") && scanner.hasNext()) {
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
                                functions.deleteNation(userID, event.getServer().get().getId());
                                event.getChannel().sendMessage("Deleted <@!" + userID + ">'s Nation. The " + results.getString("NATION_NAME") + " is no more!");
                            } else {
                                event.getChannel().sendMessage("<@!" + userID + "> doesnt have a nation to delete");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (arg.equalsIgnoreCase(prefix + "deleteAll")) {
                    waitForConfirm = true;
                    user = event.getMessageAuthor();
                    event.getChannel().sendMessage("Are you sure you want to delete the whole database. This cannot be reversed!");
                    event.getChannel().sendMessage("Type \"" + prefix + "confirm\" to confirm this action");
                    System.out.println(arg + " || " + user + " || " + waitForConfirm + " || " + event.getMessageAuthor());
                } else if (arg.equalsIgnoreCase(prefix + "confirm") && waitForConfirm && user.getIdAsString().equalsIgnoreCase(event.getMessageAuthor().getIdAsString())) {
                    functions.deleteNation(null, event.getServer().get().getId());
                    waitForConfirm = false;
                    user = null;
                    new MessageBuilder()
                            .append("The database has been killed! We are back to blank")
                            .setEmbed(new EmbedBuilder()
                                    .setImage("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Feverythingmarvel.net%2Fwp-content%2Fuploads%2F2020%2F05%2Fgone-reduced-to-atoms.jpg&f=1&nofb=1"))
                            .send(event.getChannel());
                }
            }
        }
    }
}
