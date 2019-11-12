var express = require('express'); 
var router = express.Router();

router.get('/:keyword/:isZip/:zip/:id/:isMax/:maxdistance/:free/:local/:hideup/:isNew/:isUsed/:isUnsp', function(req, res, next) { 
    var keyword = encodeURIComponent(req.params.keyword);
    var isZip = encodeURIComponent(req.params.isZip); 
    var zip = encodeURIComponent(req.params.zip);
    var categoryID = encodeURIComponent(req.params.id);
    var isMax = encodeURIComponent(req.params.isMax);
    var distance = encodeURIComponent(req.params.maxdistance);
    var isFree = encodeURIComponent(req.params.free);
    var isLocal = encodeURIComponent(req.params.local);
    var hideup = encodeURIComponent(req.params.hideup);
    var isNew = encodeURIComponent(req.params.isNew);
    var isUsed = encodeURIComponent(req.params.isUsed);
    var isUnsp = encodeURIComponent(req.params.isUnsp);
    var myArr = [];
    var count = 0;
    var data = "";
    res.header("Access-Control-Allow-Origin", "*"); 
    res.header('Access-Control-Allow-Methods', 'PUT, GET, POST, DELETE, OPTIONS'); 
    res.header("Access-Control-Allow-Headers", "X-Requested-With"); 
    res.header('Access-Control-Allow-Headers', 'Content-Type'); 
    const https = require('http'); 
    apiURL = "http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0";
    apiURL += "&SECURITY-APPNAME=RuihuiLu-RuihuiLu-PRD-716e081a6-236635ef&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD";
    apiURL += "&paginationInput.entriesPerPage=50&keywords=" + keyword; 
    if (isZip === 't') {
        apiURL += "&buyerPostalCode=" + zip;
    }
    if (categoryID !== '-1') {
        apiURL += "&categoryId=" + categoryID; 
    }
    if (isMax === 't') {
        myArr['MaxDistance'] = distance;
    }
    if (isFree === 't') {
        myArr['FreeShippingOnly'] = 'true';
    }
    if (isLocal === 't') {
        myArr['LocalPickupOnly'] = 'true';
    }
    myArr['HideDuplicateItems'] = 'true';
    if (isNew === 't' || isUsed === 't' || isUnsp === 't') {
        myArr['Condition'] = [];
        if (isNew === 't') {
            myArr['Condition'].push('New');
        }
        if (isUsed === 't') {
            myArr['Condition'].push('Used');
        }
        if (isUnsp === 't') {
            myArr['Condition'].push('Unspecified');
        }
    }
    for (let index in myArr) {
        apiURL += "&itemFilter(" + count + ").name=" + index;
        if (index !== 'Condition') {
            apiURL += "&itemFilter(" + count + ").value=" + myArr[index];
        }
        else {
            for (let i = 0 ; i < myArr[index].length ; i ++) {
                apiURL += "&itemFilter(" + count + ").value(" + i + ")=" + myArr[index][i];
            }
        }
        count ++;
    }
    apiURL += "&outputSelector(0)=SellerInfo&outputSelector(1)=StoreInfo";

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