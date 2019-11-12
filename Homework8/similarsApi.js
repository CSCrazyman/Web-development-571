var express = require('express'); 
var router = express.Router();

router.get('/:sF1/:sF2/:sF3/:itemId', function(req, res, next) { 
    var itemId = encodeURIComponent(req.params.itemId); 
    res.header("Access-Control-Allow-Origin", "*"); 
    res.header('Access-Control-Allow-Methods', 'PUT, GET, POST, DELETE, OPTIONS'); 
    res.header("Access-Control-Allow-Headers", "X-Requested-With"); 
    res.header('Access-Control-Allow-Headers', 'Content-Type'); 
    const https = require('http'); 
    apiURL = "http://svcs.ebay.com/MerchandisingService?OPERATION-NAME=getSimilarItems&SERVICE-NAME=MerchandisingService&SERVICE-VERSION=1.1.0";
    apiURL += "&CONSUMER-ID=RuihuiLu-RuihuiLu-PRD-716e081a6-236635ef&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD";
    apiURL += "&itemId=" + itemId + "&maxResults=20";

    const request = https.get(apiURL, function(response) { 
        var size = 0;
        var chunks = [];
        response.on('data', function(chunk){ 
            size += chunk.length;
            chunks.push(chunk);
        });

        response.on('end', function(){ 
            data = Buffer.concat(chunks, size).toString('utf8'); 
            res.end(data); 
        }); 
    }).on('error', function(e) { 
        console.log("Got error: " + e.message); 
    }); 
}); 

module.exports = router;