var express = require('express');
var path = require('path');
// ---- Kinds of Routers ----
var autozipRouter = require('./apis/autozipApi.js');
var productRouter = require('./apis/productsApi.js');
var detailsRouter = require('./apis/detailsApi.js');
var photosRouter = require('./apis/photosApi.js');
var similarsRouter = require('./apis/similarsApi.js');
// --------------------------
var app = express();
app.use(express.static("./hw8RL"));
app.get('/',(req,res) => {
    res.sendFile("./hw8RL/src/index.html",{root:__dirname});
});

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', autozipRouter);
app.use('/', productRouter);
app.use('/', detailsRouter);
app.use('/', photosRouter);
app.use('/', similarsRouter);

module.exports = app;
var server = app.listen(8081,function () {
  console.log("loading");
});
