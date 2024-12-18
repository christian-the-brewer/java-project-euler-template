import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;

public class Problem {
    private String title, prompt;
    private StringBuilder content = new StringBuilder();
    private int number;


    public Problem(int number) {
        this.number = number;
    }
    //METHODS

    //Setters
    public void appendContent(String content) {
        this.content.append(content);
    }

    public void setContent(String content) {
        this.content = new StringBuilder(content);
    }
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
                BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;


                while ((inputLine = input.readLine()) != null) {
                    appendContent(inputLine);
                }

                input.close();
                connection.disconnect();

                String processedContent = content.toString()
                        .replace("&", "&amp;") // Escapes all ampersands
                        .replace("&amp;lt;", "<") // Corrects double escaping for <, if present
                        .replace("&amp;gt;", ">") // Corrects double escaping for >
                        .replace("&amp;quot;", "\"") // Corrects double escaping for "
                        .replace("&amp;apos;", "'") // Corrects double escaping for '
                        .replaceAll("<(link\\b[^>]*?)(?<!/)>", "<$1 />") // Fix <link> tags
                        .replaceAll("<(img\\b[^>]*?)(?<!/)>", "<$1 />")  // Fix <img> tags
                        .replaceAll("<(meta\\b[^>]*?)(?<!/)>", "<$1 />") // Fix <meta> tags
                        .replaceAll("<(br\\b[^>]*?)(?<!/)>", "<$1 />")   // Fix <br> tags
                        .replaceAll("<(hr\\b[^>]*?)(?<!/)>", "<$1 />")  // Fix <hr> tags
                        .replaceAll("<(input\\b[^>]*?)(?<!/)>", "<$1 />") // Fix <input> tags
                        .replaceAll("\\basync\\b", "async=\"true\"");

                // Update the content
                content = new StringBuilder(processedContent);

            } else {
                System.out.println("ERROR: " + responseCode);
            }

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    private String removeDollarSigns(String content){
        return content.replaceAll("[$]", "");
    }

    public void parsePage() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.toString().getBytes());

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputStream);

            NodeList h2Nodes = doc.getElementsByTagName("h2");
            if (h2Nodes.getLength() > 0) {
                setTitle(h2Nodes.item(0).getTextContent());
            }

            NodeList divNodes = doc.getElementsByTagName("div");
            for (int i = 0; i < divNodes.getLength(); i++) {
                Element div = (Element) divNodes.item(i);
                if ("problem_content".equals(div.getAttribute("class"))) {
                    String problemContent = div.getTextContent();
                    setPrompt(removeDollarSigns(problemContent));
                }
            }

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

}
