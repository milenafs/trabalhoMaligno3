//Classe para listar todos os resultados 

import java.util.List;

public class ListaResultados 
{
	
	private List<Resultado> resultado;
	
	public List<Resultado> getResultado()
	{
		return resultado;
	}
	
	public void setResultado(List<Resultado> resul)
	{
		this.resultado = resul;
	}
	
	
	public ListaResultados(List<Resultado> resul)
	{
		this.resultado = resul;
	}
	
	public ListaResultados()
	{
		
	}
	
	public String toString()
	{
		/*String ret = "";
		
		int size = resultado.size();
		
		
		for(int i=0; i< size; i++)
		{
			Resultado resultadoVez = resultado.get(i);
			ret += resultadoVez;
		}*/
		
		return "teste";
	}
}
