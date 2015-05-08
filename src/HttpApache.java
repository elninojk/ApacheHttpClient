
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Lookup;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.auth.*;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by jeril on 2015/05/06.
 */
public class HttpApache implements ResponseHandler
{
    static HttpClientBuilder httpClientBuilder =null;
    static HttpClient httpClient =null;

    String url = "http://api.icflix.com/tv/catalogue/search?section=kids&";
    static RequestConfig config =null;
    public static void main(String[] args)
    {



//        HttpClient client = HttpClients.createMinimal();
//        System.err.println("Client "+client.);

        CredentialsProvider credsProvider = new BasicCredentialsProvider();

        Lookup<AuthSchemeProvider> authProviders = RegistryBuilder.<AuthSchemeProvider>create()
                .register(AuthSchemes.BASIC, new BasicSchemeFactory())
//                .register(AuthSchemes.DIGEST, new DigestSchemeFactory())
//                .register(AuthSchemes.SPNEGO, new SPNegoSchemeFactory())
//                .register(AuthSchemes.KERBEROS, new KerberosSchemeFactory())
//                .register(AuthSchemes.NTLM, new NTLMSchemeFactory())
                .build();



        AuthScope scope = new AuthScope(AuthScope.ANY_HOST,AuthScope.ANY_PORT);

        HttpHost proxy = new HttpHost("10.100.9.115", 8080);

        config = RequestConfig.custom()
                .setProxy(proxy)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.KERBEROS, AuthSchemes.SPNEGO))
                .build();

         httpClientBuilder = HttpClients.custom();



        credsProvider.setCredentials(
                scope,
                new NTCredentials("za\\jeril.kuruvila", "bellerin11{}","",""));
        httpClientBuilder.setDefaultCredentialsProvider(credsProvider);
        httpClientBuilder.setDefaultAuthSchemeRegistry(authProviders);

//                .setDefaultRequestConfig(config)
        httpClient = httpClientBuilder.build();




        HttpApache obj = new HttpApache();

        for (int i = 0; i <10 ; i++)
        {
            obj.measureTime();
        }

        System.err.println("Avg time taken for Apache:"+ totaltime/10);
    }

    static long totaltime = 0;
    private void measureTime()
    {
        long start = System.currentTimeMillis();
        HttpGet getsome= new HttpGet(url);
        getsome.setConfig(config);
        try
        {
            String responseBody= (String) httpClient.execute(getsome,this);
//            System.err.println("resposnse body"+ responseBody.toString().replace("\\/", "/"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        long timetook = (end-start);
        totaltime= totaltime+ timetook;
        System.err.println("Time taken: "+ (end-start));
    }

    @Override
    public Object handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException
    {
        int status = httpResponse.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300)
                {
                    HttpEntity entity = httpResponse.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                }
                else
                {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
    }
}
