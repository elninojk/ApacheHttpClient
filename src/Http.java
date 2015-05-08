import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

/**
 * Created by jeril on 2015/05/07.
 */
public class Http
{
    static String url = "http://api.icflix.com/tv/catalogue/search?section=kids&";
    String USER_AGENT = "Mozilla/5.0";
    public static void main(String[] args) throws IOException
    {

        System.setProperty("http.proxyHost", "10.100.9.115");
        System.setProperty("http.proxyPort", "8080");

        Authenticator.setDefault(new Authenticator()
        {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication("za\\jeril.kuruvila", "bellerin11{}".toCharArray());
            }
        });



        Http obj = new Http();
        for (int i = 0; i <100 ; i++)
        {
            obj.measureSpeed();
        }

        System.err.println("Avg time taken for Http:"+ totaltime/100);


    }
    static long totaltime = 0;
    void measureSpeed() throws IOException
    {
        long start = System.currentTimeMillis();

        URL obj = null;
        try
        {
            obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null)
            {
                response.append(inputLine);
            }
            in.close();
            ;
//            System.err.println("response body"+ response.toString().replace("\\/", "/"));
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        long end=System.currentTimeMillis();

        long timetook = end-start;
        totaltime= totaltime+ timetook;
//        System.err.println("TIme took "+ (end-start));
    }
}
