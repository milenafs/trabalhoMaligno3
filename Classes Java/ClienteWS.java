//Igual ao feito pelo professor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;


public class ClienteWS
{
    public static Object getObjeto (Class tipoObjetoRetorno,
                                    String urlWebService,
                                    String... parametros)
    {
        Object objetoRetorno = null;
	
        try
        {
            for (String parametro : parametros)
                urlWebService = urlWebService + "/" + parametro.replaceAll(" ", "%20");
		
            URL url = new URL (urlWebService);
            HttpURLConnection connection =
            (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.connect();

            String responseJson = inputStreamToString(connection.getInputStream());
            connection.disconnect();
		
            objetoRetorno = fromJson (responseJson, tipoObjetoRetorno);
            if(objetoRetorno == null)
            {
				System.out.println(">[ERROR] Ta null!" + responseJson + "/" + tipoObjetoRetorno );
				System.out.println(">[ERROR]" + fromJson(responseJson, tipoObjetoRetorno) );
			}
        }
        catch (Exception erro)
        {
             erro.printStackTrace();
             System.out.println(">***** [ERROR]" + erro.toString() );
        }	

        return objetoRetorno;
    }

    public static Object postObjeto (Object objetoEnvio,
                                     Class tipoObjetoRetorno,
                                     String urlWebService)
    {
        Object objetoRetorno = null;
	
        try
        {
            String requestJson = toJson(objetoEnvio);
		
            URL url = new URL(urlWebService);
            HttpURLConnection connection =
            (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setUseCaches(false);
			connection.setConnectTimeout(15000);
            //connection.setRequestProperty("login", "seulogin");
            //connection.setRequestProperty("senha", "suasenha");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Length", Integer.toString(requestJson.length()));
			
            DataOutputStream stream =
            new DataOutputStream (connection.getOutputStream());
            stream.write (requestJson.getBytes("UTF-8"));
            stream.flush ();
            stream.close ();
            connection.connect ();

            String responseJson = inputStreamToString (connection.getInputStream());
            connection.disconnect();
            objetoRetorno = fromJson (responseJson, tipoObjetoRetorno);
        }
        catch (Exception e)
        {
        	System.out.println("[ERRO AO INSERIR]");
        }

        return objetoRetorno;
    }

    public static Object deleteObjeto (Class tipoObjetoRetorno,
                        String urlWebService,
                        String... parametros)
    {
        Object objetoRetorno = null;

        try
        {
	        for (String parametro : parametros)
	        urlWebService = urlWebService + "/" + parametro.replaceAll(" ", "%20");
	
	        URL url = new URL (urlWebService);
	        HttpURLConnection connection =
	        (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("DELETE");
	        connection.setConnectTimeout(15000);
	        connection.connect();
	        
	        String responseJson = inputStreamToString(connection.getInputStream());
	        connection.disconnect();
	
	        objetoRetorno = fromJson(responseJson, tipoObjetoRetorno);
        }
        catch (Exception erro)
        {
        	System.out.println("[ERRO AO DELETAR]");
        }	

        return objetoRetorno;
    }


    public static String inputStreamToString (InputStream is) throws IOException
    {
        if (is != null)
        {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try
            {
                Reader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1)
                {
                    writer.write(buffer, 0, n);
                }
            }
            finally
            {
                is.close();
            }

            return writer.toString();
        }
        else
        {
            return "";
        }
    }

    public static String toJson(Object objeto) throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        StringWriter jsonValue = new StringWriter();
        mapper.writeValue(new PrintWriter(jsonValue), objeto);
        return jsonValue.toString();
    }

    public static Object fromJson(String json, Class objectClass) throws Exception
    {
    	JsonFactory f = null;
    	try
    	{
    		  f = new MappingJsonFactory();
    	}
    	catch(Exception err)
    	{
    		System.out.println("ERRO1");
    	}
    	
    	JsonParser jp = null;
    	try
    	{
    		jp = f.createJsonParser(json);
    	}
    	catch(Exception err)
    	{
    		System.out.println("ERRO2");
    	}
    	
    	Object obj = null;
    	try
    	{
            obj = jp.readValueAs(objectClass);
    	}
    	catch(Exception err)
    	{
    		System.out.print("");
    	}
      
       
        return obj;
    }
}
