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
const authServiceProxy = httpProxy("http://localhost:5000/auth/login", {
  proxyReqBodyDecorator: function (bodyContent, srcReq) {
    try {
      const loginRequest = {
        email: bodyContent.email,
        senha: bodyContent.senha,
      };
      console.log("Login request:", loginRequest);
      return loginRequest; 
    } catch (e) {
      console.log("- ERROR: " + e);
    }
    return bodyContent; 
  },
  proxyReqOptDecorator: function (proxyReqOpts, srcReq) {
    proxyReqOpts.headers["Content-Type"] = "application/json";
    proxyReqOpts.method = "POST";
    return proxyReqOpts;
  },
  userResDecorator: function (proxyRes, proxyResData, userReq, userRes) {
    const responseString = proxyResData.toString('utf8');
    console.log("Received response from Auth MS:", responseString);

    try {
      const jsonResponse = JSON.parse(responseString);
      return jsonResponse; 
    } catch (error) {
      console.log("Response is not valid JSON:", error);
      return responseString;
    }
  }
});

app.post("/login", (req, res, next) => {
  authServiceProxy(req, res, next);
});


  

const voosServiceProxy = httpProxy("http://localhost:5001");
const reservasServiceProxy = httpProxy("http://localhost:5002");
const clientesServiceProxy = httpProxy("http://localhost:5003");
const FuncionarioServiceProxy = httpProxy("http://localhost:5007");

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
// MS-VOOS
// Rota para listar todos os voos (GET)
app.get("/voos", (req, res, next) => {
  // TODO: Implementar a verificação do token JWT (verifyJWT) na chamada
  voosServiceProxy(req, res, next);
});

// Rota para listar um voo pelo ID (GET)
app.get("/voos/:id", (req, res, next) => {
  voosServiceProxy(req, res, next, {
    proxyReqPathResolver: (req) => `/voos/${req.params.id}`,
  });
});

// Rota para inserir um novo voo (POST)
app.post("/voos", (req, res, next) => {
  voosServiceProxy(req, res, next, {
    proxyReqBodyDecorator: (bodyContent) => bodyContent,
  });
});

// Rota para editar um voo (PUT)
app.put("/voos/:id", (req, res, next) => {
  voosServiceProxy(req, res, next, {
    proxyReqPathResolver: (req) => `/voos/${req.params.id}`,

    proxyReqBodyDecorator: (bodyContent) => bodyContent,
  });
});

// Rota para remover um voo (DELETE)
app.delete("/voos/:id", (req, res, next) => {
  voosServiceProxy(req, res, next, {
    proxyReqPathResolver: (req) => `/voos/${req.params.id}`,
  });
});

// MS-RESERVAS
app.get("/reservas", verifyJWT, (req, res, next) => {
  reservasServiceProxy(req, res, next);
});

// MS-CLIENTES
app.get("/clientes", verifyJWT, (req, res, next) => {
  clientesServiceProxy(req, res, next);
});

// MS-FUNCIONARIOS
  // registrar novo funcionario
  // TODO: Implementar a verificação do token JWT (verifyJWT) na chamada das reqs
app.post("/employee/register", verifyJWT, (req, res, next) => {
  FuncionarioServiceProxy(req, res, next);
});
  // listar todos os funcionario
app.get("/employee", verifyJWT, (req, res, next) => {
  FuncionarioServiceProxy(req, res, next);
});
  // listar func p/ id
app.get("/employee/{id}", (req, res, next) => {
  FuncionarioServiceProxy(req, res, next, {
    proxyReqPathResolver: (req) => `/employee/${req.params.id}`,
  });
});
  // update do funcionario
app.put("/employee/edit/{id}", (req, res, next) => {
  FuncionarioServiceProxy(req, res, next, {
    proxyReqPathResolver: (req) => `/employee/edit/${req.params.id}`,

    proxyReqBodyDecorator: (bodyContent) => bodyContent,
  });
});
// remover funcionario
app.delete("/employee/delete/{id}", (req, res, next) => {
  FuncionarioServiceProxy(req, res, next, {
    proxyReqPathResolver: (req) => `/employee/delete/${req.params.id}`,
  });
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


