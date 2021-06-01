

public class Teste {
	
	public static void main (String[] args) 
	{
		ListaResultados resultadoA = null;
		try
		{
			//resultadoA = (ListaResultados)ClienteWS.getObjeto(ListaResultados.class, "http://localhost:5000/resultados");
			System.out.print(">" + ClienteWS.getObjeto(ListaResultados.class, "http://localhost:5000/Resultados"));
		}
		catch(Exception err)
			{
				System.err.println("[ERRO]------------- " + err.getMessage());
			}
		
	}
}

