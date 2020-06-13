import client.Client;
import org.apache.commons.cli.*;
import server.Server;

/**
 * @author zy
 */
public class ApplicationStart {
    public static void main(String[] args) throws ParseException {
        int port = 3456;
        Options options = new Options();
        options.addOption("h", true, "listen port");
        options.addOption("s", false, "start server");
        options.addOption("p", true, "server listen port");
        options.addOption("c", true, "start client to specific server");

        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine cmd = commandLineParser.parse(options, args);
        if (cmd.hasOption('h')) {
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp("Options", options);
        } else {
            if (cmd.hasOption('s')) {
                if (cmd.hasOption('p')) {
                    port = Integer.parseInt(cmd.getOptionValue('p'));
                }
                new Server().start(port);
            }
            if (cmd.hasOption('c')) {
                var host = cmd.getOptionValue('c');
                port = Integer.parseInt(cmd.getOptionValue('p'));
                new Client().start(host, port);
            }
        }
    }
}
