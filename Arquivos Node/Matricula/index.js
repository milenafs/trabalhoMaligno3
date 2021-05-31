function BD ()
{
    process.env.ORA_SDTZ = 'UTC-3';

    this.getConexao = async function ()
    {
        if (global.conexao)
            return global.conexao;

        const oracledb = require('oracledb');
        const dbConfig = require('./dbconfig.js');
        
        try
        {
            global.conexao = await oracledb.getConnection(dbConfig);
        }
        catch (erro)
        {
            console.log ('[ERROR] Não foi possível estabelecer conexão com o BD!');
            process.exit(1);
        }

        return global.conexao;
    }

    this.estrutureSe = async function ()
    {
        try
        {
            const conexao = await this.getConexao();
            const sql     = 'CREATE TABLE Matriculas (RA NUMBER(5), Cod NUMBER(3), PRIMARY KEY (RA,Cod), FOREIGN KEY (RA) REFERENCES Alunos(RA), FOREIGN KEY (Cod) REFERENCES Disciplinas(cod)) ';
            await conexao.execute(sql);
        }
        catch (erro)
        {} 
    }

}

function middleWareGlobal (req, res, next)
{
    console.time('Requisição'); 
    console.log('Método: '+req.method+'; URL: '+req.url);

    next(); 

    console.log('Finalizou'); 

    console.timeEnd('Requisição');
}

function Matriculas (bd)
{
    this.bd = bd;

    this.recupereTodos = async function ()
    {
        const conexao = await this.bd.getConexao();
        
        const sql = 'SELECT * FROM Matriculas';
        const ret =  await conexao.execute(sql);

        return ret.rows;
    }

    this.recupereUm = async function (esseRa, esseCod)
    {
        const conexao = await this.bd.getConexao();
        
        const sql   = 'SELECT * FROM Matriculas WHERE RA=:0 and Cod=:1'
        const dados = [esseRa, esseCod];
        const ret   =  await conexao.execute(sql,dados);
        
        return ret.rows;
    }

	this.remova = async function (esseRa, esseCod)
    {
        const conexao = await this.bd.getConexao();
        
        const sql1 = 'DELETE FROM Matriculas WHERE ra=:0 and cod=:1';
        const dados = [esseRa, esseCod];
        await conexao.execute(sql1,dados);
        
        const sql2 = 'COMMIT';
        await conexao.execute(sql2);
    }
}

function Matricula (esseRa,esseCod)
{
    this.ra = esseRa;
    this.cod = esseCod;
}

function Comunicado (codigo,mensagem,descricao)
{
    this.codigo    = codigo;
    this.mensagem  = mensagem;
    this.descricao = descricao;
}

async function recuperacaoDeTodos (req, res)
{
    if (req.body.ra || req.body.codigo)
    {
        const erro = new Comunicado ('JSP','JSON sem propósito','Foram disponibilizados dados em um JSON sem necessidade');
        return res.status(422).json(erro);
    }
    
    let rec;
    try
    {
        rec = await global.matriculas.recupereTodos();
    }    
    catch(erro)
    {}

    if (rec.length==0)
    {
        return res.status(200).json([]);
    }
    else
    {
        const ret=[];

        for (i=0;i<rec.length;i++) ret.push (new Matricula (rec[i][0],rec[i][1]));

        return res.status(200).json(ret);
    }
}

async function recuperacaoUm (req, res)
{
    if (req.body.ra || req.body.codigo)
    {
        const erro = new Comunicado ('JSP','JSON sem propósito','Foram disponibilizados dados em um JSON sem necessidade');
        return res.status(422).json(erro);
    }

    const ra = req.params.ra;
    const cod = req.params.cod;
    
    let ret;
    try
    {
        ret = await global.matriculas.recupereUm(ra, cod);
    }    
    catch(erro)
    {}

    if (ret.length==0)
    {
        const erro2 = new Comunicado ('MNE','Matricula inexistente','Não há matricula cadastrado com o código ou nome informados');
        return res.status(404).json(erro2);
    }
    else
    {
        ret = ret[0];
        ret = new Matricula (ret[0],ret[1]);
        return res.status(200).json(ret);
    }
}

async function remocao (req, res)
{
    if (req.body.ra || req.body.codigo)
    {
        const erro = new Comunicado ('JSP','JSON sem propósito','Foram disponibilizados dados em um JSON sem necessidade');
        return res.status(422).json(erro);
    }
    
    const ra = req.params.ra;
    const cod = req.params.cod;
    
    let ret;
    try
    {
        ret = await global.matriculas.recupereUm(ra, cod);
    }    
    catch(erro)
    {}

    if (ret.length==0)
    {
        const erro2 = new Comunicado ('MNE','Matricula inexistente','Não há matricula cadastrado com o código ou nome informados');
        return res.status(404).json(erro2);
    }
    else
    {
        try
        {
            await global.matriculas.remova(ra, cod);
        }    
        catch(erro)
        {}

        const sucesso = new Comunicado ('RBS','Remoção bem sucedida','A matricula foi removida com sucesso');
        return res.status(200).json(sucesso);
    }
}


async function ativacaoDoServidor ()
{
    const bd = new BD ();
    await bd.estrutureSe();
    global.matriculas = new Matriculas (bd);

    const express = require('express');
    const app     = express();
    
    app.use(express.json());  
    app.use(middleWareGlobal);

    app.get   ('/matriculas'    , recuperacaoDeTodos);
    app.get   ('/matriculas/:cod/:ra', recuperacaoUm);
    app.delete('/matriculas/:cod/:ra', remocao);

    console.log ('Servidor ativo na porta 3000...');
    app.listen(3000);
}

ativacaoDoServidor();
