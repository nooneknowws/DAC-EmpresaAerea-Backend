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
const nodemailer = require("nodemailer");

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

const authServiceProxy = httpProxy("http://localhost:5000", {
  proxyReqPathResolver: function (req) {
      return req.path === '/login' ? '/login' : '/logout';
  },
  proxyReqBodyDecorator: function (bodyContent, srcReq) {
      try {
          if (srcReq.path === '/login') {
              const loginRequest = {
                  email: bodyContent.email,
                  senha: bodyContent.senha,
              };
              console.log("Processing login request");
              return loginRequest;
          }

          if (srcReq.path === '/logout') {
              const logoutRequest = {
                  token: bodyContent.token
              };
              console.log("Processing logout request");
              return logoutRequest;
          }
          
          return bodyContent;
      } catch (e) {
          console.log("Error processing request:", e);
          return bodyContent;
      }
  },
  proxyReqOptDecorator: function (proxyReqOpts, srcReq) {
      proxyReqOpts.headers["Content-Type"] = "application/json";
      proxyReqOpts.method = srcReq.method;
      return proxyReqOpts;
  },
  userResDecorator: function (proxyRes, proxyResData, userReq, userRes) {
      const responseString = proxyResData.toString('utf8');
      console.log(`Received response from Auth service:`, responseString);

      try {
          const jsonResponse = JSON.parse(responseString);
          return jsonResponse;
      } catch (error) {
          console.log("Error parsing response:", error);
          return responseString;
      }
  }
});

const voosServiceProxy = httpProxy("http://localhost:5001");
const reservasServiceProxy = httpProxy("http://localhost:5002");
const clientesServiceProxy = httpProxy("http://localhost:5003");
const FuncionarioServiceProxy = httpProxy("http://localhost:5007");

async function mailServer(req, res, next) {
  try {
    console.log(process.env.EMAIL_PASSWORD)
    
    console.log(process.env.EMAIL_USER)
    const { nome, email } = req.body;
    const senha = Math.floor(1000 + Math.random() * 9000).toString();

    if (!nome || !email) {
      return res.status(400).json({ 
        error: "Nome e email são obrigatórios para cadastro" 
      });
    }

   
    const transporter = nodemailer.createTransport({
      host: "smtp.sendgrid.net",
      port: 587,
      auth: {
        user: process.env.EMAIL_USER,
        pass: process.env.EMAIL_PASSWORD
      }
    });

    const mailOptions = {
      from: "empresaaerea.dac.ufpr@gmail.com",
      to: email,
      subject: "Bem-vindo(a) a nossa companhia de passagens aereas!",
      text: `Olá ${nome},\n\nSeu cadastro foi realizado com sucesso!\n\nSua senha de acesso é: ${senha}\n\nAtenciosamente,\nEquipe 4 DAC - UFPR`
    };
    await transporter.sendMail(mailOptions);

    console.log(`Email enviado para ${email} com a senha: ${senha}`);
    req.body.senha = senha;

    next();
  } catch (error) {
    console.error("Erro ao enviar email:", error);
    return res.status(500).json({
      error: "Erro ao enviar o email de boas-vindas",
      details: error.message
    });
  }
}

function verifyPerfil(req, res, next) {
  const perfil = req.query.perfil;
  const status = req.query.statusFunc;

  if (perfil === "Funcionario" && status === "ATIVO") {
    next();
  } else {
    return res.status(403).json({
      auth: false,
      message: "Você não tem permissão para acessar esses dados."
    });
  }
}

function verifyJWT(req, res, next) {
  const token = req.headers["x-access-token"];
  
  if (!token) {
    return res.status(401).json({ 
      auth: false, 
      message: "Token não fornecido" 
    });
  }

  jwt.verify(token, process.env.SECRET, function (err, decoded) {
    if (err) {
      return res.status(401).json({ 
        auth: false, 
        message: "Falha ao autenticar token." 
      });
    }

    req.userId = decoded.id;
    next();
  });
}

app.post("/login", (req, res, next) => {
  if (!req.body.email || !req.body.senha) {
    return res.status(400).json({ 
      error: "Email e senha são obrigatórios" 
    });
  }
  authServiceProxy(req, res, next);
});

app.post("/logout", (req, res, next) => {
  if (!req.body.token) {
      return res.status(400).json({
          status: "error",
          message: "Token não fornecido no corpo da requisição"
      });
  }
  authServiceProxy(req, res, next);
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
//cadastro
app.post("/clientes/cadastro", mailServer, (req, res, next) => {
  clientesServiceProxy(req, res, next);
});
//busca
app.get("/clientes/busca", verifyJWT, verifyPerfil, (req, res, next) => {
  clientesServiceProxy(req, res, next);
  console.log("Query Params:", req.query);
});


// MS-FUNCIONARIOS
  app.post("/funcionarios/cadastro", mailServer, (req, res, next) => {
    FuncionarioServiceProxy(req, res, next);
  });
app.get("/funcionarios", verifyJWT, (req, res, next) => {
  FuncionarioServiceProxy(req, res, next);
});
app.get("/funcionarios/{id}", (req, res, next) => {
  FuncionarioServiceProxy(req, res, next, {
    proxyReqPathResolver: (req) => `/employee/${req.params.id}`,
  });
});
app.put("/funcionarios/edit/{id}", (req, res, next) => {
  FuncionarioServiceProxy(req, res, next, {
    proxyReqPathResolver: (req) => `/employee/edit/${req.params.id}`,

    proxyReqBodyDecorator: (bodyContent) => bodyContent,
  });
});
app.delete("/funcionarios/delete/{id}", (req, res, next) => {
  FuncionarioServiceProxy(req, res, next, {
    proxyReqPathResolver: (req) => `/employee/delete/${req.params.id}`,
  });
});
app.use(logger("dev"));
app.use(helmet());
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());

//criar o servidor na porta 3000
var server = http.createServer(app);
server.listen(3000);


