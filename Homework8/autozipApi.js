var express = require('express'); 
var router = express.Router();

router.get('/:zipcode', function(req, res, next) { 
    var zipcode = encodeURIComponent(req.params.zipcode); 
    res.header("Access-Control-Allow-Origin", "*"); 
    res.header('Access-Control-Allow-Methods', 'PUT, GET, POST, DELETE, OPTIONS'); 
    res.header("Access-Control-Allow-Headers", "X-Requested-With"); 
    res.header('Access-Control-Allow-Headers', 'Content-Type'); 
    const https = require('http'); 
    apiURL = "http://api.geonames.org/postalCodeSearchJSON?postalcode_startsWith=" + zipcode; 
    apiURL += "&username=lurh950826&country=US&maxRows=5";

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