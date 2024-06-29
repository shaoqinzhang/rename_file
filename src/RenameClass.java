import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Objects;

public class RenameClass {

    private static String imageDirsPath;
    private static String imageNamesFilePath;
    private static String outputFileDestPath;
    private static String relativePathStr;

    public static void main(String[] args) throws IOException {
        getConfig();
        File imageDirsPathFile = new File(imageDirsPath);
        File imageNamesFilePathFile = new File(imageNamesFilePath);
        File outputFileDestPathFile = new File(outputFileDestPath);

        File[] files = imageDirsPathFile.listFiles();

        if (files != null) {
            int index = 0;
            try(FileReader fr = new FileReader(imageNamesFilePathFile)) {
                try (FileWriter fw = new FileWriter(outputFileDestPathFile,false)) {
                    BufferedReader br = new BufferedReader(fr);
                    BufferedWriter bw = new BufferedWriter(fw);
                    String line;
                    while ((line = br.readLine()) != null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("- [ ] ");
                        String[] words = line.split(" ");
                        stringBuilder.append(words[0]+ " ");
                        if (words.length > 1) {
                            for (int i = 1; i <= Integer.parseInt(words[1]); i++) {
                                String baseName =words[0] + i;
                                File dest = new File(imageDirsPathFile.getCanonicalPath() + File.separator + baseName + ".jpg");
                                System.out.println(files[index].getName());
                                files[index].renameTo(dest);
                                index++;
                                stringBuilder.append("[" + baseName+"]("+relativePathStr+baseName+".jpg) ");
                            }
                        } else {
                            File dest = new File(imageDirsPathFile.getCanonicalPath() + File.separator + words[0] + ".jpg");
                            files[index].renameTo(dest);
                            index++;
                            stringBuilder.append("[" + dest.getName()+"](" + relativePathStr + dest.getName() + ") ");
                        }
                        bw.write(stringBuilder.toString() +"\r\n");
                    }
                    bw.flush();
                }
            }
        }
    }
    public static void getConfig(){

        File file = new File(RenameClass.class.getClassLoader().getResource("config.json").getPath());

        try(FileReader fr = new FileReader(file)) {
            BufferedReader br = new BufferedReader(fr);
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(sb.toString());

            imageDirsPath = (String) jsonObject.get("imageDirsPath");
            imageNamesFilePath = (String) jsonObject.get("imageNamesFilePath");
            outputFileDestPath = (String) jsonObject.get("outputFileDestPath");
            relativePathStr = (String) jsonObject.get("relativePathStr");
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }

    }
}
