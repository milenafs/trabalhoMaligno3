
// Igual ao Fila<ListaSimplesDesordenada<X>> mas muda para 
// ListaDuplamenteLigadaDesordenada<X>

import java.lang.reflect.Method;

public class Fila <X>
{
    private ListaDuplamenteLigadaDesordenada<X> elementos;

    public Fila ()
    {
        this.elementos = new ListaDuplamenteLigadaDesordenada<X> ();
    }

    public void guardeUmItem (X x) throws Exception
    {
        if (x==null)
            throw new Exception ("Falta o que guardar");

        this.elementos.guardeUmItemNoFinal (x);
    }

    public X recupereUmItem () throws Exception
    {
        if (this.elementos.isVazia())
            throw new Exception ("Nada a recuperar");

        return this.elementos.recupereItemDoInicio();
    }

    public void removaUmItem () throws Exception
    {
        if (this.elementos.isVazia())
            throw new Exception ("Nada a remover");

        this.elementos.removaItemDoInicio();
    }

    public boolean isVazia ()
    {
        return this.elementos.isVazia();
    }

    public String toString()
    {
		int quantidade=this.elementos.getQuantidade();
        String ret = quantidade + (quantidade==1?" elemento":" elementos");
        
        try
		{
            ret += ", sendo o primeiro "+this.elementos.recupereItemDoInicio();
        }
        catch (Exception erro)
        {} // se der erro, nao quero nada acrescentar ao ret
              
        return ret;
    }

    public boolean equals (Object obj)
    {
        if(this==obj)
            return true;

        if(obj==null)
            return false;

        if(this.getClass()!=obj.getClass())
            return false;

        Fila<X> fil = (Fila<X>) obj;

        return this.elementos.equals (fil.elementos);
    }

    public int hashCode ()
    {
        int ret=666/*qualquer positivo*/;

        ret = ret*7/*primo*/ + this.elementos.hashCode();

        if (ret<0)
            ret=-ret;

        return ret;
    }

    public Fila(Fila<X> modelo) throws Exception
    {
        if(modelo == null)
            throw new Exception("[ERROR] Modelo ausente");

        this.elementos = new ListaDuplamenteLigadaDesordenada<X> (modelo.elementos);
    }

    public Object clone()
    {
        Fila<X> ret = null;

        try
        {
            ret = new Fila(this);
        }
        catch(Exception erro)
        {}

        return ret;
    }
}
