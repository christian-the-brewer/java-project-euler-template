import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Problem {
    private String title, prompt;
    private int number;


    public Problem(int number) {
        this.number = number;
    }
    //METHODS

    //Setters
    public void setTitle(String title){
        this.title = title;
    }

    public void setPrompt(String prompt){
        this.prompt = prompt;
    }

    //Getters
    public int getNumber(){
        return number;
    }

    public String getTitle(){
        return title;
    }

    public String getPrompt(){
        return prompt;
    }

    public void fetchPage() {
        try {
            URL url = new URL(String.format("https://projecteuler.net/problem=%d", number));
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

}
