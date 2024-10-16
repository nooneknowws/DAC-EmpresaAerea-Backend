require("dotenv-safe").config();
const jwt = require('jsonwebtoken');
var http = require('http');
const express = require('express');
const httpProxy = require('express-http-proxy')
const app = express()
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser')
var logger = require('morgan');
const helmet = require('helmet');

app.use( bodyParser.urlencoded({ extended: false }))

app.use ( bodyParser.json() );


const voosServiceProxy = httpProxy('http://localhost:5000');
const reservasServiceProxy = httpProxy('http://localhost:5001');
const clientesServiceProxy = httpProxy('http://localhost:5002');

function verifyJWT(req, res, next) {
    const token = req.headers['x-access-token'];
    if (!token) 
        return res.status(401).json({ auth: false, message: 'token não fornecido'});
    
    jwt.verify(token, process.env.SECRET, function(err,decoded) {
        if (err)
            return res.status(500).jjson({ auth: false, message: 'falha ao autenticar token.' });

        req.userId = decoded.id;
        next();
    });
}

app.post('/login', express.urlencoded({ extended: false }), (req, res, next) => {
    //isso aqui tem q ser feito invocando o serviço de autenticação futuramente
    if (req.body.user === 'admin' && req.body.password === 'admin') {
        const id = 1;//isso aqui viria do serviço
        const token = jwt.sign({ id }, process.env.SECRET, {
            expiresIn: 300
        });
        return res.json({ auth: true, token: token});
    }
    res.status(500).json({message: 'Login inválido!'})
})

app.post('/logout', function(req, res){
    //isso aqui só anula o login, bem simples
    res.json({ auth: false, token: null });
})

//aqui vai os HTTP da vida, que comunica com os MS->
app.get ('/voos', verifyJWT, (req, res, next) => {
    voosServiceProxy(req,res,next);
})

app.get ('/reservas', verifyJWT, (req, res, next) => {
    reservasServiceProxy(req,res,next);
})

app.get ('/clientes', verifyJWT, (req, res, next) => {
    clientesServiceProxy(req,res,next);
});

//configurações
app.use(logger('dev'));
app.use(helmet());
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());

//criar o servidor na porta 3000
var server = http.createServer(app);
server.listen(3000);

//pra testar no postman
//manda um post pra localhost:3000/login no body vc coloca x-www-form-urlencoded como tipo de dado e manda 2 key, user e password ambas admin
//ai ele vai gerar um token, vc passa esse token no get pra localhost:3000/xxxx <- depende do MS q tu quer acessar
//jogar o token no HEADER como x-access-token na key e o value tu copia o token q foi gerado no login.
//ele vai retornar o conteudo do DB.json dos MS em json-server, não esquece de executar os db.json com  json-server --watch db.json --port 5002
//só abrir um console na pasta do MS e digitar a porta de acordo com o MS, a gente tem q padronizar isso dps.