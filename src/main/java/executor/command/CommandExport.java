package executor.command;

import com.opencsv.CSVWriter;
import duke.exception.DukeException;
import interpreter.Parser;
import storage.StorageManager;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CommandExport extends Command {

    // filepath of either taskData or WalletData
    private String filePath;

    public CommandExport(String userInput) {
        this.userInput = userInput;
        this.commandType = CommandType.EXPORT;
        this.description = "Exports txt into CSV"
                            + "FORMAT : export <wallet or task>";
    }

    @Override
    public void execute(StorageManager storageManager) {

        try{
            String fileUserWants = getWhichFileUserWants(this.userInput);
            convertTxtToCsv(fileUserWants);
            this.infoCapsule.setCodeCli();
            this.infoCapsule.setOutputStr("data.csv has been created");
        } catch (DukeException e) {
            this.infoCapsule.setCodeError();
            this.infoCapsule.setOutputStr(e.getMessage());
        }
    }

    private String getWhichFileUserWants (String userInput) throws DukeException {
        try {
            return Parser.parseForPrimaryInput(this.commandType, userInput);
        } catch (Exception e) {
            throw new DukeException("Please enter a valid choice : either task or wallet\n");
        }
    }

    private void convertTxtToCsv(String dataWanted) throws DukeException {
        if(dataWanted.toLowerCase().equals("wallet")){
            this.filePath = "savedWallet.txt";
        } else {
            this.filePath = "savedTask.txt";
        }
        try{
            File file = new File(this.filePath);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String loadedInput = scanner.nextLine();
                if (loadedInput.equals("")) {
                    break;
                }
                writeCSV(loadedInput);
            }
        } catch (Exception e) {
            throw new DukeException("Please enter a choice either : export <wallet> or <task>");
        }
    }

    private void writeCSV(String line) throws DukeException {
        // first create file object for file placed at location
        // specified by filepath
        File file = new File("data.csv");
        try {
            // create FileWriter object with file as parameter
            FileWriter outputFile = new FileWriter(file);
            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputFile);
            // add data to csv
            String[] eachRowOfData = {line.replace(" ", ",")};
            writer.writeNext(eachRowOfData);
            // closing writer connection
            writer.close();
        }
        catch (IOException e) {
            throw new DukeException("Unable to convert txt into csv");
        }
    }
}
