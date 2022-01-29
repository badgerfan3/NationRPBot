package commands;

import com.google.gson.Gson;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.awt.*;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

public class Help implements MessageCreateListener {
    String prefix;
    public Help(String prefix){
        this.prefix = prefix;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        boolean argEmpty = true;

        String command = "";
        String arg = "";

        if(event.getMessageAuthor().isUser()) {
            Scanner scanner = new Scanner(event.getMessageContent());
            if (scanner.hasNext())
                command = scanner.next();

            if (command.equalsIgnoreCase(prefix + "help")) {
                if (scanner.hasNext()) {
                    argEmpty = false;
                    arg = scanner.next();
                }
                Gson gson = new Gson();

                Reader reader;
                try {
                    reader = Files.newBufferedReader(Paths.get("./misc/help.json"));
                    Map<?, ?> map = gson.fromJson(reader, Map.class);
                    reader.close();

                    if (argEmpty) {
                        MessageBuilder sendMessage = new MessageBuilder();
                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setColor(Color.WHITE);
                        for (Map.Entry<?, ?> entry : map.entrySet()) {
                            embed.addField(entry.getKey().toString(), entry.getValue().toString());
                        }
                        sendMessage.setEmbed(embed);
                        sendMessage.send(event.getChannel());
                    } else {
                        if (map.containsKey(arg)) {
                            event.getChannel().sendMessage(arg + " : " + map.get(arg));
                        } else {
                            event.getChannel().sendMessage(arg + " is not a valid command. Use \"" + prefix + "help\" to list all the commands you can use");
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
