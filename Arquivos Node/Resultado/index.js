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
            const sql     = 'CREATE TABLE Resultados (RA NUMBER(5), Cod NUMBER(3),Nota NUMBER(4,1) NOT NULL,Freq NUMBER(5,1) NOT NULL,PRIMARY KEY (RA,Cod),FOREIGN KEY (RA)REFERENCES Alunos(RA),FOREIGN KEY (Cod) REFERENCES Disciplinas(cod))';
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

function Resultados (bd)
{
    this.bd = bd;

    this.recupereTodos = async function ()
    {
        const conexao = await this.bd.getConexao();
        
        const sql = 'SELECT * FROM Resultados';
        const ret =  await conexao.execute(sql);

        return ret.rows;
    }

    this.inclua = async function (resultado)
    {
        const conexao = await this.bd.getConexao();
        
        const sql1 = 'INSERT INTO Resultados VALUES (:0,:1,:2,:3)';
        const dados = [resultado.ra, resultado.cod, resultado.nota, resultado.freq];
        await conexao.execute(sql1,dados);

        const sql2 = 'COMMIT';
        await conexao.execute(sql2);    
    }    
}

function Resultado (ra,cod,nota,freq)
{
    this.ra = ra;
    this.cod = cod;
    this.nota = nota;
    this.freq = freq;
}

function Comunicado (codigo,mensagem,descricao)
{
    this.codigo    = codigo;
    this.mensagem  = mensagem;
    this.descricao = descricao;
}

async function recuperacaoDeTodos (req, res)
{
    if (req.body.ra || req.body.codigo || req.body.freq || req.body.nota)
    {
        const erro = new Comunicado ('JSP','JSON sem propósito','Foram disponibilizados dados em um JSON sem necessidade');
        return res.status(422).json(erro);
    }
    
    let rec;
    try
    {
        rec = await global.resultados.recupereTodos();
    }    
    catch(erro)
    {}

    if (rec.length == 0)
    {
        return res.status(200).json([]);
    }
    else
    {
        const ret=[];

        for (i=0;i<rec.length;i++) ret.push (new Resultado (rec[i][0], rec[i][1], rec[i][2], rec[i][3]));

        return res.status(200).json({resultado:ret});
    }
}

async function inclusao (req, res)
{
    if (!req.body.ra || !req.body.cod || !req.body.freq || !req.body.nota)
    {
        const erro1 = new Comunicado ('DeI','Dados incompletos','Não foram informados todos os dados do resultado');
        return res.status(422).json(erro1);
    }
    
    const resultado = new Resultado (req.body.ra, req.body.cod, req.body.nota, req.body.freq);

    try
    {
        await  global.resultados.inclua(resultado);

        const  sucesso = new Comunicado ('IBS','Inclusão bem sucedida','O resultado foi incluído com sucesso');
        return res.status(201).json(sucesso);
    }
    catch (erro)
    {
        const  erro2 = new Comunicado ('RJE','Resultado existente','Já há resultado cadastrado com o código e ra informados');
        return res.status(409).json(erro2);
    }
}

async function ativacaoDoServidor ()
{
    const bd = new BD ();
    await bd.estrutureSe();
    global.resultados = new Resultados (bd);

    const express = require('express');
    const app     = express();
    
    app.use(express.json());  
    app.use(middleWareGlobal);

    app.get('/resultados', recuperacaoDeTodos);
    app.post('/resultados', inclusao);

    console.log ('Servidor ativo na porta 5000...');
    app.listen(5000);
}

ativacaoDoServidor();
