import java.lang.reflect.*;

public class ListaDuplamenteLigadaDesordenada <X>
{
    private class No
    {
        private X  info;
        private No prox;
        private No antes;

        public No (X i, No p, No a)
        {
            this.info = i;
            this.prox = p;
            this.antes = a;
        }

        public No (X i)
        {
            this.info = i;
            this.prox = null;
            this.antes = null;
        }

        public X getInfo ()
        {
            return this.info;
        }

        public No getProx ()
        {
            return this.prox;
        }
        
        public No getAntes ()
        {
            return this.antes;
        }

        public void setInfo (X i)
        {
            this.info = i;
        }

        public void setProx (No p)
        {
            this.prox = p;
        }
        
        public void setAntes (No a)
        {
            this.antes = a;
        }
    } //Fim classe No;

    private No primeiro, ultimo;

    public ListaDuplamenteLigadaDesordenada ()
    {
		//instancia os dois atributos nulos;
        this.primeiro=this.ultimo=null;
    }

    private X meuCloneDeX (X x)
    {
        X ret=null;

        try
        {
            Class<?> classe         = x.getClass();
            Class<?>[] tipoDosParms = null;
            Method metodo           = classe.getMethod("clone",tipoDosParms);
            Object[] parms          = null;
            ret                     = (X)metodo.invoke(x,parms);
        }
        catch(NoSuchMethodException erro)
        {}
        catch(IllegalAccessException erro)
        {}
        catch(InvocationTargetException erro)
        {}

        return ret;
    }

    public void guardeUmItemNoInicio (X i) throws Exception 
    {
        if (i==null) //verifica se o parâmetro passado é nulo;
            throw new Exception ("[ERROR] Informacao ausente");

        X oqInserir = null;
        
        if (i instanceof Cloneable) // se o parâmetro for cloneable, 
		{  						    //oqInserir vai receber uma cópia de 'i'
            oqInserir = (X)meuCloneDeX(i);
		}
        else
        {
			//Se 'i' não for Cloneable, oqInserir recebe 'i' direto;
			oqInserir = i;
        }   
        
        this.primeiro = new No (oqInserir, this.primeiro, null);

        if (this.ultimo==null)
            this.ultimo=this.primeiro;
    }

    public void guardeUmItemNoFinal (X i) throws Exception 
    {
       if (i==null) //verifica se o parâmetro passado é nulo;
            throw new Exception ("[ERROR] Informacao ausente");

		// Vai guardar a info de 'i' em oqInserir, seja por cópia ou 
		// diretamente
        X oqInserir = null;
        if (i instanceof Cloneable)
            oqInserir = (X)meuCloneDeX(i);
        else
            oqInserir = i;
            
        if (this.ultimo==null) //vê se a lista está vazia
        {
            this.ultimo   = new No (oqInserir); //instancia um No com prox e antes nulos;
            this.primeiro = this.ultimo; 
        }
        else
        {
            this.ultimo.setProx (new No (oqInserir));
            No aux = new No(this.ultimo.getInfo(), this.ultimo.getProx(), this.ultimo.getAntes()); 
            this.ultimo = this.ultimo.getProx();
            this.ultimo.setAntes(aux);
        }
    }
    
    public X recupereItemDoInicio () throws Exception
    {
        if (this.primeiro==null/*&&this.fim==null)*/)
            throw new Exception ("[ERROR] Nada a obter");

		// Pega a info do inicio MAS retorna uma cópia para o user!
        X ret = this.primeiro.getInfo();
        if (ret instanceof Cloneable)
            ret = meuCloneDeX (ret);
            
        return ret;
    }

    public X recupereItemDoFinal () throws Exception
    {
        if (this.primeiro==null/*&&this.ultimo==null)*/)
            throw new Exception ("[ERROR] Nada a obter");

		// Pega a info do fim MAS retorna uma cópia para o user!
        X ret = this.ultimo.getInfo();
        if (ret instanceof Cloneable)
            ret = meuCloneDeX (ret);
            
        return ret;
    }

    public void removaItemDoInicio () throws Exception 
    {
        if (this.primeiro==null /*&& this.ultimo==null*/)
            throw new Exception ("[ERROR] Nada a remover");

        if (this.primeiro==this.ultimo)//vê se a lista só tem 1 elemento
        {
            this.primeiro=this.ultimo=null;
            return;
        }

        this.primeiro = this.primeiro.getProx();
        this.primeiro.setAntes(null);
    }
    
    public void removaItemDoFinal () throws Exception //alterar
    {
        if (this.primeiro==null/*&&this.ultimo==null*/)
            throw new Exception ("[ERROR] Nada a remover");

        if (this.primeiro==this.ultimo)//vê se a lista só tem 1 elemento
        {
            this.primeiro=this.ultimo=null;
            return;
        }

        No atual;
        for (atual=this.primeiro;
             atual.getProx()!=this.ultimo;
             atual=atual.getProx())
             /*comando vazio*/;

        atual.setProx(null);
        No aux = new No(this.ultimo.getInfo(), this.ultimo.getProx(), this.ultimo.getAntes());
        this .ultimo=atual;
        this. ultimo.setAntes(aux);
    }
    
	public void removaItemIndicado (X i) throws Exception
	{
        if (i==null)
            throw new Exception ("[ERROR] Informacao ausente");

        boolean removeu=false;

        for(;;) // FOR EVER (repete até break)
        {
            if (this.primeiro==null/*&&this.ultimo==null*/)
                break;

            if (!i.equals(this.primeiro.getInfo()))
                break;
                
            if (this.ultimo==this.primeiro)
                this.ultimo=null;

            this.primeiro=this.primeiro.getProx();

            removeu=true;
        }

        if (this.primeiro!=null/*&&this.ultimo!=null*/)
        {
            No atual=this.primeiro;

            forever:for(;;) // repete ate break
            {
                if (atual.getProx()==null)
                    break;

                while (i.equals(atual.getProx().getInfo()))
                {
                    if (this.ultimo==atual.getProx())
                        this.ultimo=atual;

                    atual.setProx(atual.getProx().getProx());

                    removeu=true;

                    if (atual==this.ultimo)
                        break forever;
                }

                atual=atual.getProx();
            }
        }

        if (!removeu)
            throw new Exception ("[ERROR] Informacao inexistente");
	}

	public int getQuantidade ()
    {
        No  atual=this.primeiro;
        int ret  =0;

        while (atual!=null)
        {
            ret++;                
            atual = atual.getProx();
        }
        
        return ret;
    }

    public boolean tem (X i) throws Exception
    {
        if (i==null)
            throw new Exception ("[ERROR] Informacao ausente");
		
        No atual=this.primeiro;

        while (atual!=null)
        {
            if (i.equals(atual.getInfo()))
                return true;
                
            atual = atual.getProx();
        }
        
        return false;
	}
	
    public boolean isVazia ()
    {
        return this.primeiro==null/*&&this.ultimo==null*/;
    }
    
    public String toString ()
    {
        String ret="[";

        No atual=this.primeiro;

        while (atual!=null)
        {
            ret=ret+atual.getInfo();

            if (atual!=this.ultimo)
                ret=ret+",";

            atual=atual.getProx();
        }

        return ret+"]";
    }

    public boolean equals (Object obj)
    {
        if (this==obj)
            return true;

        if (obj==null)
            return false;

        if (this.getClass()!=obj.getClass())
            return false;

        ListaDuplamenteLigadaDesordenada<X> lista = (ListaDuplamenteLigadaDesordenada<X>) obj;

        No atualThis =this .primeiro;
        No atualLista=lista.primeiro;

        while (atualThis!=null && atualLista!=null)
        {
            if (!atualThis.getInfo().equals(atualLista.getInfo()))
                return false;

            atualThis  = atualThis .getProx();
            atualLista = atualLista.getProx();
        }

        if (atualThis!=null  /* && atualLista==null */)
            return false;

        if (atualLista!=null /* && atualThis ==null */)
            return false;

        // atualThis==null && atualLista==null
        return true;
    }
    
    public int hashCode ()
    {
        final int PRIMO = 13;
        
        int ret=666; 

        for (No atual=this.primeiro;
             atual!=null;
             atual=atual.getProx())
             ret = PRIMO*ret + atual.getInfo().hashCode();

        if (ret<0) ret = -ret;

        return ret;
    }
    
    public ListaDuplamenteLigadaDesordenada (ListaDuplamenteLigadaDesordenada<X> modelo) throws Exception
    {
        if (modelo==null)
            throw new Exception ("[ERROR] Modelo ausente");

        if (modelo.primeiro==null)
            return; // sai do construtor, pq this.primeiro ja é null

        this.primeiro = new No (modelo.primeiro.getInfo());

        No atualDoThis   = this.primeiro;
        No atualDoModelo = modelo.primeiro.getProx();

        while (atualDoModelo!=null)
        {
            atualDoThis.setProx (new No (atualDoModelo.getInfo()));
            No teste = new No(atualDoThis.getInfo(), atualDoThis.getProx(), atualDoThis.getAntes());
            atualDoThis   = atualDoThis  .getProx ();
            atualDoThis.setAntes(teste);
            atualDoModelo = atualDoModelo.getProx ();
        }

        this.ultimo = atualDoThis;
    }

    public Object clone ()
    {
        ListaDuplamenteLigadaDesordenada<X> ret=null;

        try
        {
            ret = new ListaDuplamenteLigadaDesordenada (this);
        }
        catch (Exception erro)
        {} // sei que this NUNCA � null e o contrutor de copia da erro quando seu parametro � null

        return ret;
    }
    
}
