var express = require('express'); 
var router = express.Router();

router.get('/:googleFlag/:googleF/:productTitle', function(req, res, next) { 
    var productTitle = encodeURIComponent(req.params.productTitle); 
    res.header("Access-Control-Allow-Origin", "*"); 
    res.header('Access-Control-Allow-Methods', 'PUT, GET, POST, DELETE, OPTIONS'); 
    res.header("Access-Control-Allow-Headers", "X-Requested-With"); 
    res.header('Access-Control-Allow-Headers', 'Content-Type'); 
    const https = require('https'); 
    apiURL = "https://www.googleapis.com/customsearch/v1?q=" + productTitle + "&cx=016676074107705355785:5joum8ylums";
    apiURL += "&imgSize=huge&imgType=news&num=8&searchType=image&key=AIzaSyDq4bGxq5ITwFyxYUW1lYjKhxD6bCTixuo";
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