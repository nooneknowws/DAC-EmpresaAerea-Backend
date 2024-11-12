require("dotenv-safe").config();
const amqp = require("amqplib/callback_api");
const jwt = require("jsonwebtoken");
var http = require("http");
const express = require("express");
const httpProxy = require("express-http-proxy");
const app = express();
var cookieParser = require("cookie-parser");
var bodyParser = require("body-parser");
var logger = require("morgan");
const helmet = require("helmet");

app.use(bodyParser.urlencoded({ extended: false }));

app.use(bodyParser.json());

const authServiceProxy = httpProxy("http://localhost:5000", {
  proxyReqBodyDecorator: function (bodyContent, srcReq) {
    try {
      const loginRequest = {
        email: bodyContent.email,
        senha: bodyContent.senha,
      };
      console.log("Login request:", loginRequest);

      // Send login request to RabbitMQ
      amqp.connect("amqp://localhost", (err, connection) => {
        if (err) throw err;

        connection.createChannel((err, channel) => {
          if (err) throw err;

          const queue = "auth.request";

          channel.assertQueue('auth.request', { 
            durable: true, 
            arguments: { 'x-message-ttl': 30000 }  // Set TTL to 30 seconds (30000 ms)
          });
          channel.sendToQueue(queue, Buffer.from(JSON.stringify(loginRequest)));
          console.log("Sent login request to 'auth.request' queue");
        });
      });

      return loginRequest; // return the modified body for logging/debugging purposes
    } catch (e) {
      console.log("- ERROR: " + e);
    }
    return bodyContent; // fallback in case of error
  },
  proxyReqOptDecorator: function (proxyReqOpts, srcReq) {
    proxyReqOpts.headers["Content-Type"] = "application/json";
    proxyReqOpts.method = "POST";
    return proxyReqOpts;
  },
  userResDecorator: function (proxyRes, proxyResData, userReq, userRes) {
    return new Promise((resolve, reject) => {
      amqp.connect("amqp://localhost", (err, connection) => {
        if (err) {
          reject(err);
          return;
        }
  
        connection.createChannel((err, channel) => {
          if (err) {
            reject(err);
            return;
          }
  
          const queue = "auth.reply";
  
          channel.assertQueue('auth.reply', { 
            durable: true, 
            arguments: { 'x-message-ttl': 30000 }
          });
  
          console.log("Waiting for messages in %s", queue);
          channel.consume(queue, (msg) => {
            if (msg !== null) {
              const response = JSON.parse(msg.content.toString());
  
              if (response.status === "success") {
                console.log(
                  `Login successful! Token: ${response.token}, Email: ${response.email}`
                );
                userRes.status(200).json({
                  auth: true,
                  token: response.token,
                  email: response.email,
                  
                });
                channel.ack(msg);
              } else {
                console.log(`Login failed: ${response.message}`);
                userRes.status(401).json({ message: "Invalid login!" });
                channel.ack(msg);
              }
  
              
  
              resolve('Message processed');
            } 
          });
        });
      });
    }, { noAck: false });
  }
  
  
  }
  

);
const voosServiceProxy = httpProxy("http://localhost:5001");
const reservasServiceProxy = httpProxy("http://localhost:5002");
const clientesServiceProxy = httpProxy("http://localhost:5003");

function verifyJWT(req, res, next) {
  const token = req.headers["x-access-token"];
  if (!token)
    return res
      .status(401)
      .json({ auth: false, message: "token não fornecido" });

  jwt.verify(token, process.env.SECRET, function (err, decoded) {
    if (err)
      return res
        .status(500)
        .jjson({ auth: false, message: "falha ao autenticar token." });

    req.userId = decoded.id;
    next();
  });
}

app.post("/login", (req, res, next) => {
  authServiceProxy(req, res, next);
});

app.post("/logout", function (req, res) {
  //isso aqui só anula o login, bem simples
  res.json({ auth: false, token: null });
});

//aqui vai os HTTP da vida, que comunica com os MS->
app.get("/voos", verifyJWT, (req, res, next) => {
  voosServiceProxy(req, res, next);
});

app.get("/reservas", verifyJWT, (req, res, next) => {
  reservasServiceProxy(req, res, next);
});

app.get("/clientes", verifyJWT, (req, res, next) => {
  clientesServiceProxy(req, res, next);
});

//configurações
app.use(logger("dev"));
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
