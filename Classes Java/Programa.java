
public class Programa
{
    public static void main (String[] args)
    {
		//1º parte: usuário digitar quantas vezes quiser e guardar tudo
		//em uma lista de resultados
		char opcao = 'f';
		Resultado auxResultado = null;
		Fila<Resultado> filaResultados = new Fila<Resultado>();;
		System.out.println();
		System.out.println("**********************************************************************");
		System.out.println ("Grupo:\nGiovana Mendonca Zambanini  (20178) \nIsabela Clementino Ponciano (20138) \nMilena Furuta Shishito      (20148)");
		do
		{
			try
			{
				System.out.println();
				System.out.println("**********************************************************************");
				System.out.println("(a) Adicionar o resultado de um aluno.");
				System.out.println("(f) Finalizar cadastro dos resultados.");
				System.out.println("**********************************************************************");
				System.out.println();
				System.out.print  (" > Opcao: ");
				opcao = Character.toLowerCase(Teclado.getUmChar());
			}
			catch(Exception err)
			{
				System.out.println(err.getMessage());
			}
			
			if(opcao == 'a')
			{
				try
				{
					System.out.print("\n> RA: ");
					int raDigitado  = Teclado.getUmInt();
					
					System.out.print("> Codigo da Disciplina: ");
					int codDigitado = Teclado.getUmInt();
					
					System.out.print("> Nota (0 - 10): ");
					float notaDigitado = Teclado.getUmFloat();
					
					System.out.print("> Frequencia (0.0 - 1.0): ");
					float freqDigitado = Teclado.getUmFloat();
					
					auxResultado = new Resultado(raDigitado, codDigitado, notaDigitado, freqDigitado);
					
					
					filaResultados.guardeUmItem(auxResultado);
				}
				catch(Exception err) 
				{
					System.out.println(err.getMessage());
				}
			}

		}while(opcao != 'f');
			
				
		//2º parte:Recupere todas as instâncias da fila de Resultado
		// e, com seus dados, aciona os WEB-Services
		if(opcao == 'f')
		{
			while(!filaResultados.isVazia())
			{
				Resultado resultadoAtual = null;
				try
				{
					resultadoAtual = filaResultados.recupereUmItem();

					ClienteWS.deleteObjeto(Resultado.class, "http://localhost:3000/Matriculas/" + resultadoAtual.getCod() + "/" + resultadoAtual.getRa());
					
					ClienteWS.postObjeto(resultadoAtual, Resultado.class, "http://localhost:5000/resultados");
					
					filaResultados.removaUmItem();
				}
				catch(Exception err)
				{
					System.err.println("[ERRO]");
				}
			}

			// Printa tudo da fila de Resultado
			ListaResultados resultado = (ListaResultados)ClienteWS.getObjeto(ListaResultados.class, "http://localhost:5000/resultados");
			System.out.println (resultado);
		}

	}
}
