public class Resultado
{
	int ra;
	int codigo;
	float nota;
	float freq;
	
	//Construtor
	public Resultado(int esseRa, int esseCod, float essaNota, float essaFreq)throws Exception
	{
		this.setRa(esseRa);
		this.setCod(esseCod);
		this.setNota(essaNota);
		this.setFreq(essaFreq);
	}
	public Resultado(){}
	//Setters
	public void setRa(int esseRa)throws Exception
	{
		if (esseRa <= 0)
            throw new Exception ("[ERROR] RA invalido.");
            
        this.ra = esseRa;
	}
	public void setCod(int esseCod)throws Exception
	{
		if (esseCod <= 0)
            throw new Exception ("[ERROR] Codigo invalido.");
            
        this.codigo = esseCod;
	}
	public void setNota(float essaNota)throws Exception
	{
		if (essaNota <= 0)
            throw new Exception ("[ERROR] Nota invalida.");
            
        this.nota = essaNota;
	}
	public void setFreq(float essaFreq)throws Exception
	{
		if (essaFreq <= 0)
            throw new Exception ("[ERROR] Frequencia invalida.");
            
        this.freq = essaFreq;
	}
	
	//Getters
	public int getRa()
	{
		return ra;
	}
	public int getCod()
	{
		return codigo;
	}
	public float getNota()
	{
		return nota;
	}
	public float getFreq()
	{
		return freq;
	}
	
	
	public String toString()
	{
		String ret="";
		ret+="...................... \n";
		ret+="RA........: "+this.ra+"\n";
        ret+="Codigo....: "+this.codigo +"\n";
        ret+="Nota......: "+this.nota +"\n";
		ret+="Frequencia: "+this.freq +"\n\n";
        return ret;
	}
	public boolean equals (Object obj)
	{ 
		if (this==obj)
            return true;

        if (obj==null)
            return false;

        if (!(obj instanceof Resultado))
            return false;

        Resultado res = (Resultado)obj;
		
		if(this.ra != res.ra)
          return false;
        
		if(this.codigo != res.codigo)
          return false;
          
		if(this.nota != res.nota)
          return false;
          
		if(this.freq != res.freq)
          return false;  
          
        return true;
	}
	public int hashCode ()
	{ 

		int ret = 666; 
		ret = 7*ret + new Integer(this.ra).hashCode();
		ret = 7*ret + new Integer(this.codigo).hashCode();        
		ret = 7*ret + new Float(this.nota).hashCode();
		ret = 7*ret + new Float(this.freq).hashCode();

		return ret;
	}
	public Resultado (Resultado modelo) throws Exception
	{
		this.ra = modelo.ra;
		this.codigo = modelo.codigo;
		this.nota = modelo.nota;
		this.freq = modelo.freq;
	}
	public Object clone ()
    { 
		Resultado ret = null;
		try
        {
            ret = new Resultado(this);
        }
        catch (Exception erro)
        {}
		return ret;
	}
}

