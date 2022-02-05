import Sql.SQLFunctions;
import commands.*;
import io.github.cdimascio.dotenv.Dotenv;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class Bot {
    public static void main(String[] args){
        Dotenv dotenv = Dotenv.configure()
                .directory("./misc")
                .load();

        String token = dotenv.get("TOKEN");
        String prefix = dotenv.get("PREFIX");

        SQLFunctions sqlFunctions = new SQLFunctions();
        sqlFunctions.getConnection();

        DiscordApi api = new DiscordApiBuilder()
                .addListener(new Ping(prefix))
                .addListener(new Help(prefix))
                .addListener(new Info(prefix, sqlFunctions))
                .addListener(new CreateNation(prefix, sqlFunctions))
                .addListener(new Delete(prefix, sqlFunctions))
                .addListener(new ChangeVal(prefix, sqlFunctions))
                .setToken(token)
                .login()
                .join();

    }
}
