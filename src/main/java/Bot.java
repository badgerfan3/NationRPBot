import Sql.SQLFunctions;
import commands.*;
import commands.SlashCommands.PingSlash;
import io.github.cdimascio.dotenv.Dotenv;
import listeners.onJoin;
import listeners.onLeave;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;

public class Bot {
    public static void main(String[] args){
        Dotenv dotenv = Dotenv.configure()
                .directory("./misc")
                .load();

        String token = dotenv.get("TOKEN");
        String prefix = dotenv.get("PREFIX");

        SQLFunctions sqlFunctions = new SQLFunctions();
        sqlFunctions.getConnection();

        DiscordApi api = new DiscordApiBuilder().setToken(token).setAllIntents().login().join();
        api.addListener(new onJoin(sqlFunctions));
        api.addListener(new onLeave(sqlFunctions));
        api.addListener(new Ping(prefix));
        api.addListener(new Help(prefix,api));
        api.addListener(new Info(prefix, sqlFunctions, api));
        api.addListener(new CreateNation(prefix, sqlFunctions, api));
        api.addListener(new Delete(prefix, sqlFunctions, api));
        api.addListener(new ChangeVal(prefix, sqlFunctions));
        api.addListener(new PingSlash());

    }
}
